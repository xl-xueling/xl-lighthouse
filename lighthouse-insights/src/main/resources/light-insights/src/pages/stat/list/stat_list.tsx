import {
    Table,
    Message, PaginationProps, Notification,
} from '@arco-design/web-react';
import React, {useEffect, useMemo, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {useSelector} from "react-redux";
import locale from './locale';
import { Department, Stat} from "@/types/insights-web";
import {requestChangeState, requestList} from "@/api/stat";
import {getColumns, getColumnsOfManage} from "@/pages/stat/list/constants";
import Detail from "@/pages/stat/list/detail";
import StatUpdateModal from "@/pages/stat/update";
import {getRandomString} from "@/utils/util";
import {GlobalErrorCodes} from "@/utils/constants";
import StatDetailModal from "@/pages/stat/list/detail";
import {StatStateEnum} from "@/types/insights-common";
import {requestResetPasswd} from "@/api/user";

export default function StatisticalListPanel({formParams = {},from = null}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const [listData,setListData] = useState<Array<Stat>>([]);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [dataVersion,setDataVersion] = useState<number>();
    const [updateModalVisible,setUpdateModalVisible] = React.useState(false);
    const [currentItem,setCurrentItem] = useState<Stat>(null);
    const refFetchId = useRef<number>(null);

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
            await handlerChangeState(record.id,StatStateEnum.RUNNING);
        }else if(type == 'stop'){
            await handlerChangeState(record.id,StatStateEnum.STOPPED);
        }else if(type == 'delete'){
            console.log("---delete item...")
        }
    };

    const handlerChangeState = async (id:number,state:StatStateEnum) => {
        console.log("state:" + state);
        const changeParam = {
            id:id,
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
                Notification.info({style: { width: 420 }, title: 'Notification', content: tooltips});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error)=>{
            console.log(error);
        })
    }

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const columns = useMemo(() => (from && from == 'group-manage') ? getColumnsOfManage(t, tableCallback) : getColumns(t,tableCallback), [t]);
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
    },[pagination.current, pagination.pageSize, JSON.stringify(formParams)])


    return (
        <>
        <Table border={false}
               rowKey={'id'}
               size={"small"} columns={columns}
               data={listData}
               onChange={onChangeTable}
               pagination={pagination}
               loading={loading}/>
            {detailVisible && <StatDetailModal statInfo={currentItem} onClose={() => setDetailVisible(false)}/>}
            {updateModalVisible && <StatUpdateModal statInfo={currentItem} onClose={() => setUpdateModalVisible(false)} listCallback={tableCallback}/>}
        </>
        );
}