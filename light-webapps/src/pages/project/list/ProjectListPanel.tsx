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
import {requestDeleteById, requestList, requestStarById, requestUnStarById} from "@/api/project";
import {Department, MetricSet, Project, TreeNode} from "@/types/insights-web";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useDispatch, useSelector} from "react-redux";
import ProjectUpdatePanel from "@/pages/project/update";
import {ResourceTypeEnum} from "@/types/insights-common";
import {GlobalState} from "@/store";
import {requestBinded} from "@/api/metricset";
import {MetricSetBindListContext, ProjectListContext} from "@/pages/common/context";
import {updateStoreStaredProjectInfo} from "@/store";
import {convertDateToTimestamp, DateFormat, getDayEndTimestamp, getDayStartTimestamp} from "@/utils/date";
import {useUpdateEffect} from "ahooks";
import { useLocation, useHistory } from 'react-router-dom';
import useNavigateTo from "@/pages/common/redirect/useNavigateTo";
const BreadcrumbItem = Breadcrumb.Item;

export interface Props {
    formParams?:any,
    owner?:number,
    from?:string,
    parentLoading?:boolean,
    extend?:any,
    defaultPagination?:PaginationProps,
}

const ProjectListPanel:React.FC<Props> = ({
                                              formParams= {},
                                              owner = 1,
                                              parentLoading = false,
                                              extend = null,
                                              from= null,
                                              defaultPagination= {
                                                  sizeOptions: [15,20,30,50],
                                                  sizeCanChange: true,
                                                  showTotal: true,
                                                  pageSize: 15,
                                                  current: 1,
                                                  pageSizeChangeResetCurrent: true,
                                              }
}) => {
    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const { PRO_ProjectApplyModal = null } = useContext(ProjectListContext) || {};
    const [listData, setListData] = useState<Project[]>([]);
    const [selectedProject,setSelectedProject] = useState<Project>(null);
    const [form] = useForm();
    const navigateTo = useNavigateTo();
    const dispatch = useDispatch();
    const [createVisible, setCreateVisible] = React.useState(false);
    const [updateVisible, setUpdateVisible] = React.useState(false);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [bindedVisible,setBindedVisible] = React.useState(false);
    const [applyVisible,setApplyVisible] = React.useState(false);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [bindList,setBindList] = useState<number[]>([]);
    const {handleMetricBindListReloadCallback} = useContext(MetricSetBindListContext) || {};
    const [reloadTime,setReloadTime] = useState<number>(Date.now());
    const staredProjectInfo = useSelector((state: {staredProjectInfo:Array<Project>}) => state.staredProjectInfo || []);
    const [pagination, setPagination] = useState<PaginationProps>(defaultPagination);
    const history = useHistory();
    const tableCallback = async (record, type) => {
        if(type == 'update'){
            setSelectedProject(record);
            setUpdateVisible(!updateVisible);
        }else if(type == 'delete'){
            await handlerDeleteProject(record.id).then();
        }else if(type == 'star'){
            setSelectedProject(record);
            await handlerStar(record);
        }else if(type == 'unstar'){
            setSelectedProject(record);
            await handlerUnStar(record);
        }else if(type == 'detail'){
            setSelectedProject(record);
            setDetailVisible(!detailVisible);
        }else if(type == 'apply'){
            setSelectedProject(record);
            setApplyVisible(!applyVisible);
        }else if(type == 'bind'){
            await handlerBind(record.id).then();
        }else if(type == 'preview'){
            navigateTo('/project/preview/'+record.id);
        }else if(type == 'manage'){
            navigateTo('/project/manage/'+record.id);
        }
    };

    const handlerDeleteProject = async (id: number) => {
        await requestDeleteById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.delete.submit.success']});
                localStorage.removeItem('cache_stared_projects');
                const currentFixedData = staredProjectInfo?.filter(x => x.id != id);
                dispatch(updateStoreStaredProjectInfo([...currentFixedData]))
                setReloadTime(Date.now());
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    };

    const handlerStar = async (record) => {
        setLoading(true);
        const id = record.id;
        await requestStarById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.star.submit.success']});
                localStorage.removeItem('cache_stared_projects');
                const currentFixedData = staredProjectInfo?.filter(x => x.id != record.id);
                dispatch(updateStoreStaredProjectInfo([record,...currentFixedData]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerUnStar = async (record) => {
        setLoading(true);
        const id = record.id;
        await requestUnStarById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.unstar.submit.success']});
                localStorage.removeItem('cache_stared_projects');
                const currentFixedData = staredProjectInfo?.filter(x => x.id != record.id);
                dispatch(updateStoreStaredProjectInfo([...currentFixedData]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

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
            return getColumns(t,staredProjectInfo, tableCallback);
        }
    }

    const columns = useMemo(() => handleGetColumns(), [t,bindList,staredProjectInfo,listData]);

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

    useEffect(() => {
        if(extend != null){
            const ids = extend.bindElements.filter(x => x.resourceType == ResourceTypeEnum.Project).map(x => x.resourceId);
            setBindList(ids);
        }
    },[extend])

    useEffect(() => {
        fetchData().then();
    },[reloadTime])

    useUpdateEffect(() => {
        setReloadTime(Date.now());
    },[pagination.current, pagination.pageSize])

    useUpdateEffect(() => {
        setPagination({
            ...pagination,
            current : 1,
        });
        setReloadTime(Date.now());
    },[JSON.stringify(formParams)])

    useUpdateEffect(() => {
        setPagination({
            ...pagination,
            current : 1
        });
        setTimeout(() => {
            setReloadTime(Date.now());
        },0)
    },[owner])

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const {current, pageSize} = pagination;
        const combineParams:any = {}
        combineParams.search = formParams.search;
        combineParams.departmentIds = formParams.departmentIds && formParams.departmentIds.length > 0 ? formParams.departmentIds: null;
        const createTime = formParams.createTime;
        if(createTime && Array.isArray(createTime)){
            combineParams.createStartTime = getDayStartTimestamp(convertDateToTimestamp(createTime[0],DateFormat));
            combineParams.createEndTime = getDayEndTimestamp(convertDateToTimestamp(createTime[1],DateFormat));
        }
        combineParams.ownerId = owner == 1?userInfo?.id:null;
        await requestList({
            queryParams:combineParams,
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
            {updateVisible && <ProjectUpdatePanel projectInfo={selectedProject} allDepartInfo={allDepartInfo} onClose={() => setUpdateVisible(false)} onSuccess={() => setReloadTime(Date.now())}/>}
            {applyVisible && PRO_ProjectApplyModal && <PRO_ProjectApplyModal projectInfo={selectedProject} onClose={() => setApplyVisible(false)}/>}
        </>
    );
}

export default ProjectListPanel;