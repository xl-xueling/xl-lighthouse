import {
    Table,
    Message, PaginationProps, Notification,
} from '@arco-design/web-react';
import React, {useEffect, useMemo, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {useSelector} from "react-redux";
import locale from './locale';
import {Department, Stat, TreeNode} from "@/types/insights-web";
import {requestChangeState, requestDeleteById, requestList} from "@/api/stat";
import {getColumns, getColumnsOfManage} from "@/pages/stat/list/constants";
import Detail from "@/pages/stat/list/detail";
import StatUpdateModal from "@/pages/stat/update";
import {getRandomString} from "@/utils/util";
import {GlobalErrorCodes} from "@/utils/constants";
import StatDetailModal from "@/pages/stat/list/detail";
import {StatStateEnum} from "@/types/insights-common";
import {requestResetPasswd} from "@/api/user";
import SearchForm from "@/pages/stat/list/form";

export default function StatisticalListPanel({formParams = {},from = null,parentLoading=false}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const [listData,setListData] = useState<Array<Stat>>([]);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [dataVersion,setDataVersion] = useState<number>();
    const [updateModalVisible,setUpdateModalVisible] = React.useState(false);
    const [currentItem,setCurrentItem] = useState<Stat>(null);
    const refFetchId = useRef<number>(null);
    const [refreshTime,setRefreshTime] = useState<number>(null);

    const tableCallback = async (record, type) => {
        if(type == 'showUpdateModal'){
            setCurrentItem(record);
            setUpdateModalVisible(true);
        }else if(type == 'showDetailModal'){
            setCurrentItem(record);
            setDetailVisible(!detailVisible);
        }else if(type == 'updateCallBack'){
            setCurrentItem(record);
            setListData(listData.map(x => record.id == x.id ? record:x))
        }else if(type == 'restart'){
            await handlerChangeState(record,StatStateEnum.RUNNING);
        }else if(type == 'stop'){
            await handlerChangeState(record,StatStateEnum.STOPPED);
        }else if(type == 'delete'){
            setCurrentItem(record);
            await handlerDelete(record.id);
        }
    };

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
                setCurrentItem(record);
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
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error)
        })
    }

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const columns = useMemo(() => (from && from == 'group-manage') ? getColumnsOfManage(t, tableCallback) : getColumns(t,tableCallback), [t,listData]);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });


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
        setLoading(true);
        fetchData().then();
    },[refreshTime,pagination.current, pagination.pageSize, JSON.stringify(formParams)])


    return (
        <>
        <Table border={false}
               rowKey={'id'}
               size={"small"} columns={columns}
               data={listData}
               onChange={onChangeTable}
               pagination={pagination}
               loading={parentLoading ? false : loading}/>
            {detailVisible && <StatDetailModal statInfo={currentItem} onClose={() => setDetailVisible(false)}/>}
            {updateModalVisible && <StatUpdateModal statInfo={currentItem} onClose={() => setUpdateModalVisible(false)} listCallback={tableCallback}/>}
        </>
        );
}