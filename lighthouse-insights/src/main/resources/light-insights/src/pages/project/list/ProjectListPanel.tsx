import React, {useContext, useEffect, useMemo, useState} from 'react';
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
import {getBindColumns, getColumns} from './constants';
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
import {requestBinded} from "@/api/metricset";
import {MetricSetPreviewContext} from "@/pages/metricset/preview";
import {MetricSetBindListContext} from "@/pages/metricset/binded/list";

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
    const [bindList,setBindList] = useState<number[]>([]);
    const handleMetricBindListReloadCallback = useContext(MetricSetBindListContext);

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
        }else if(type == 'bind'){
            await handlerBind(record.id).then();
        }
    };


    async function handlerBind(id:number){
        const bindParams = {
            bindElements:[{resourceId:id,resourceType:ResourceTypeEnum.Project}],
            metricIds:extend.id,
        }
        await requestBinded(bindParams).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.bind.success']});
                setBindList([...bindList,id])
                handleMetricBindListReloadCallback();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        })
    }

    const hideCreateModal = () => {
        setCreateVisible(false);
    };

    const hideUpdateModal = () => {
        setUpdateVisible(false);
    };

    const handleGetColumns = () => {
        if(from == 'bind'){
            return getBindColumns(t,bindList, tableCallback);
        }else{
            return getColumns(t, tableCallback);
        }
    }

    const columns = useMemo(() => handleGetColumns(), [t,bindList,listData]);
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
        if(extend != null){
            const ids = extend.bindElements.filter(x => x.resourceType == ResourceTypeEnum.Project).map(x => x.resourceId);
            setBindList(ids);
        }
    },[extend])

    useEffect(() => {
        fetchData().then();
    }, [reloadTime,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const {current, pageSize} = pagination;
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
                size={"small"}
                loading={loading}
                onChange={onChangeTable}
                pagination={pagination}
                columns={columns}
                data={listData}
            />
            {updateVisible && <ProjectUpdatePanel projectInfo={selectedProject} allDepartInfo={allDepartInfo} onClose={() => setUpdateVisible(false)} onSuccess={handlerReloadList}/>}
            {detailVisible && <Detail projectInfo={selectedProject} onClose={() => setDetailVisible(false)}/>}
            {bindedVisible && <ReverseBindedPanel bindElement={{resourceId:selectedProject?.id,resourceType:ResourceTypeEnum.Project,title:selectedProject?.title}} onClose={() => setBindedVisible(false)}/>}
            {applyVisible && <ProjectApplyModal projectInfo={selectedProject} onClose={() => setApplyVisible(false)}/>}
        </>
    );

}