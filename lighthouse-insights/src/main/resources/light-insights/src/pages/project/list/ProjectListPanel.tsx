import React, {useEffect, useMemo, useState} from 'react';
import {
    Breadcrumb,
    Button,
    Card,
    Grid,
    Message,
    Notification,
    PaginationProps,
    Radio,
    Space,
    Table,
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import {getColumns} from './constants';
import {requestDeleteById, requestList} from "@/api/project";
import {Department, Project, TreeNode} from "@/types/insights-web";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useSelector} from "react-redux";
import ProjectCreatePanel from "@/pages/project/create";
import ProjectUpdatePanel from "@/pages/project/update";
import Detail from "@/pages/project/list/detail";
import ReverseBindedPanel from "@/pages/metricset/binded/reverse-binded";
import ProjectApplyModal from "@/pages/project/apply";
import {IconHome} from "@arco-design/web-react/icon";
import {ResourceTypeEnum} from "@/types/insights-common";
import {GlobalErrorCodes} from "@/utils/constants";
import {getRandomString} from "@/utils/util";
import {GlobalState} from "@/store";

const BreadcrumbItem = Breadcrumb.Item;

export interface Props {
    formParams?:any,
    from?:string,
    parentLoading?:boolean,
    extend?:any,
}

export default function ProjectListPanel({formParams = {},parentLoading = false,extend = null,from = null}:Props) {
    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const [listData, setListData] = useState<Project[]>([]);
    const [selectedProject,setSelectedProject] = useState<Project>(null);
    const [form] = useForm();
    const [createVisible, setCreateVisible] = React.useState(false);
    const [updateVisible, setUpdateVisible] = React.useState(false);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [bindedVisible,setBindedVisible] = React.useState(false);
    const [applyVisible,setApplyVisible] = React.useState(false);
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);

    const tableCallback = async (record, type) => {
        if(type == 'update'){
            setSelectedProject(record);
            setUpdateVisible(!updateVisible);
        }else if(type == 'delete'){
            await handlerDeleteProject(record.id).then();
        }else if(type == 'binded'){
            setSelectedProject(record);
            await handlerBindedProject().then();
        }else if(type == 'detail'){
            setSelectedProject(record);
            setDetailVisible(!detailVisible);
        }else if(type == 'apply'){
            setSelectedProject(record);
            setApplyVisible(!applyVisible);
        }
    };

    const hideCreateModal = () => {
        setCreateVisible(false);
    };

    const hideUpdateModal = () => {
        setUpdateVisible(false);
    };

    const columns = useMemo(() => getColumns(t, tableCallback), [t,listData]);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const [loading, setLoading] = useState(true);

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    function handleSearch(params) {
        setPagination({ ...pagination, current: 1 });
    }

    function handleReset(){
        form.resetFields();
        handleSearch({});
    }

    const handlerReloadList = () => {
        setReloadTime(Date.now);
    }

    const handlerBindedProject = async () => {
        setBindedVisible(true);
    };

    const handlerDeleteProject = async (id: number) => {
        await requestDeleteById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.delete.submit.success']});
                const updatedList = listData.filter(x => x.id != id);
                setListData(updatedList);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    };

    useEffect(() => {
        fetchData().then().catch(error => {
            console.log(error);
            Message.error(t['system.error']);
        })
    }, [reloadTime,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const {current, pageSize} = pagination;
        console.log("formParams is====:" + JSON.stringify(formParams));
        await requestList({
            queryParams:formParams,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((response) => {
            const {code, data ,message} = response;
            if (code === '0') {
                setListData(data.list);
                setPagination({
                    ...pagination,
                    current,
                    pageSize,
                    total: data.total});
                setLoading(false);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    return (
        <>
            <Table
                rowKey={'id'}
                style={{ marginTop:12}}
                size={"small"}
                loading={loading}
                onChange={onChangeTable}
                pagination={pagination}
                columns={columns}
                data={listData}
            />
            {createVisible && <ProjectCreatePanel allDepartInfo={allDepartInfo} onClose={() => setCreateVisible(false)} onSuccess={handlerReloadList}/>}
            {updateVisible && <ProjectUpdatePanel projectInfo={selectedProject} allDepartInfo={allDepartInfo} onClose={() => setUpdateVisible(false)} onSuccess={handlerReloadList}/>}
            {detailVisible && <Detail projectInfo={selectedProject} onClose={() => setDetailVisible(false)}/>}
            {bindedVisible && <ReverseBindedPanel bindElement={{resourceId:selectedProject?.id,resourceType:ResourceTypeEnum.Project,title:selectedProject?.title}} onClose={() => setBindedVisible(false)}/>}
            {applyVisible && <ProjectApplyModal projectInfo={selectedProject} onClose={() => setApplyVisible(false)}/>}
        </>
    );

}