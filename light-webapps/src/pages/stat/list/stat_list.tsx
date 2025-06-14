import {
    Table,
    Message, PaginationProps, Notification,
} from '@arco-design/web-react';
import React, {useContext, useEffect, useMemo, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {useSelector} from "react-redux";
import locale from './locale';
import {Stat, TreeNode} from "@/types/insights-web";
import {requestChangeState, requestDeleteById, requestList} from "@/api/stat";
import {getBindColumns, getColumns, getColumnsOfManage} from "@/pages/stat/list/constants";
import StatUpdateModal from "@/pages/stat/update";
import {ResourceTypeEnum, StatStateEnum} from "@/types/insights-common";
import {requestBinded} from "@/api/metricset";
import {MetricSetBindListContext, StatListContext} from "@/pages/common/context";
import {GroupManageContext} from "@/pages/common/context";
import {useUpdateEffect} from "ahooks";
import useNavigateTo from "@/pages/common/redirect/useNavigateTo";

interface Props {
    formParams?:any,
    from?:string,
    parentLoading?:boolean,
    extend?:any,
    defaultPagination?:PaginationProps,
}

const StatisticalListPanel:React.FC<Props> = ({
                                              formParams= {},
                                              from= null,
                                              parentLoading = false,
                                              extend = null,
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
    const navigateTo = useNavigateTo();
    const [loading,setLoading] = useState<boolean>(false);
    const [listData,setListData] = useState<Array<Stat>>([]);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [applyVisible,setApplyVisible] = React.useState(false);
    const [dataVersion,setDataVersion] = useState<number>();
    const [updateModalVisible,setUpdateModalVisible] = React.useState(false);
    const [selectedStat,setSelectedStat] = useState<Stat>(null);
    const refFetchId = useRef<number>(null);
    const [bindList,setBindList] = useState<number[]>([]);
    const [refreshTime,setRefreshTime] = useState<number>(null);
    const { handleMetricBindListReloadCallback } = useContext(MetricSetBindListContext) || {};
    const handlerStatDeleteCallback = useContext(GroupManageContext);
    const [pagination, setPagination] = useState<PaginationProps>(defaultPagination);
    const {PRO_StatApplyModal = null} = useContext(StatListContext) || {};

    const tableCallback = async (record, type) => {
        if(type == 'showUpdateModal'){
            setSelectedStat(record);
            setUpdateModalVisible(true);
        }else if(type == 'showDetailModal'){
            setSelectedStat(record);
            setDetailVisible(!detailVisible);
        }else if(type == 'updateCallBack'){
            setSelectedStat(record);
            setListData(listData.map(x => record.id == x.id ? record:x))
        }else if(type == 'restart'){
            await handlerChangeState(record,StatStateEnum.RUNNING);
        }else if(type == 'stop'){
            await handlerChangeState(record,StatStateEnum.STOPPED);
        }else if(type == 'delete'){
            setSelectedStat(record);
            await handlerDelete(record.id);
        }else if(type == 'bind'){
            await handlerBind(record.id);
        }else if(type == 'apply'){
            setSelectedStat(record);
            setApplyVisible(true);
        }else if(type == 'preview'){
            navigateTo('/stat/preview/'+record.id);
        }
    };

    async function handlerBind(id:number){
        const bindParams = {
            bindElements:[{resourceId:id,resourceType:ResourceTypeEnum.Stat}],
            metricIds:extend.id,
        }
        await requestBinded(bindParams).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['statList.columns.bind.success']});
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

    const handlerChangeState = async (record:Stat,state:StatStateEnum) => {
        const changeParam = {
            id:record.id,
            state:state,
        }
        await requestChangeState(changeParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                let tooltips;
                if(state == StatStateEnum.RUNNING){
                    tooltips = t['statList.columns.restart.success'];
                }else if(state == StatStateEnum.STOPPED){
                    tooltips = t['statList.columns.stop.success'];
                }else if(state == StatStateEnum.FROZEN){
                    tooltips = t['statList.columns.frozen.success'];
                }
                record.state = state;
                setSelectedStat(record);
                setListData(listData.map(x => record.id == x.id ? record:x))
                Notification.info({style: { width: 420 }, title: 'Notification', content: tooltips});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error)=>{
            console.log(error);
        })
    }

    const handlerDelete = async (id:number) => {
        await requestDeleteById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['statList.columns.delete.success']});
                const updatedList = listData.filter(x => x.id != id);
                setListData(updatedList);
                handlerStatDeleteCallback(id);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error)
        })
    }

    const handleGetColumns = () => {
        if(!from){
            return getColumns(t,tableCallback);
        }else if(from == 'group-manage'){
            return getColumnsOfManage(t, tableCallback);
        }else if(from == 'bind'){
            return getBindColumns(t,bindList,tableCallback);
        }
    }

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const columns = useMemo(() => handleGetColumns(), [t,from,listData,bindList]);

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const fetchData = async () => {
        const {current, pageSize} = pagination;
        refFetchId.current = Date.now();
        const fetchId = refFetchId.current;
        await requestList({
            queryParams:formParams,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                if (refFetchId.current === fetchId) {
                    setListData(data.list);
                    setPagination({
                        ...pagination,
                        current,
                        pageSize,
                        total: data.total});
                    setLoading(false);
                }
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                setLoading(false);
            }
        }).catch((error) => {
            console.log(error);
            setLoading(false);
        })
    }

    useEffect(() => {
        if(extend != null){
            const ids = extend.bindElements.filter(x => x.resourceType == ResourceTypeEnum.Stat).map(x => x.resourceId);
            setBindList(ids);
        }
    },[extend])

    useEffect(() => {
        setLoading(true);
        fetchData().then();
    },[refreshTime])

    useUpdateEffect(() => {
       setRefreshTime(Date.now());
    },[pagination.current, pagination.pageSize])

    useUpdateEffect(() => {
        setLoading(true);
        setPagination({
            ...pagination,
            current : 1,
        });
        setRefreshTime(Date.now());
    },[JSON.stringify(formParams)])

    return (
        <>
        <Table
            border={false}
               rowKey={'id'}
               size={"small"} columns={columns}
               data={listData}
               onChange={onChangeTable}
               pagination={pagination}
               loading={parentLoading ? false : loading}/>
            {updateModalVisible && <StatUpdateModal statInfo={selectedStat} onClose={() => setUpdateModalVisible(false)} listCallback={tableCallback}/>}
            {applyVisible && PRO_StatApplyModal && <PRO_StatApplyModal statInfo={selectedStat} onClose={() => setApplyVisible(false)}/>}
        </>
        );
};

export default StatisticalListPanel;