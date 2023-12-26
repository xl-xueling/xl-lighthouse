import {
    Table,
    Message, PaginationProps,
} from '@arco-design/web-react';
import React, {useEffect, useMemo, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {useSelector} from "react-redux";
import locale from './locale';
import { Department, PermissionsEnum, Stat, StatPagination} from "@/types/insights-web";
import {requestList} from "@/api/stat";
import {requestPrivilegeCheck} from "@/api/privilege";
import {ResultData} from "@/types/insights-common";
import {getColumns, getColumnsOfManage} from "@/pages/stat/list/constants";
import Detail from "@/pages/stat/list/detail";
import StatUpdateModal from "@/pages/stat/update";

export default function StatisticalListPanel({formParams,from = null}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);

    const [listData,setListData] = useState<Array<StatPagination>>([]);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [updateModalVisible,setUpdateModalVisible] = React.useState(false);
    const [currentItem,setCurrentItem] = useState<Stat>(null);

    const tableCallback = async (record, type) => {
        if(type == 'update'){
            setCurrentItem(record);
            setUpdateModalVisible(true);
        }else if(type == 'detail'){
            setCurrentItem(record);
            setDetailVisible(!detailVisible);
        }
    };

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

    useEffect(() => {
        setLoading(true);
        const {current, pageSize} = pagination;
        const fetchData = async () => {
            const fetchStatsInfo:Promise<{list:Array<Stat>,total:number}> = new Promise<{list:Array<Stat>,total:number}>((resolve, reject) => {
                const proc = async () => {
                    const result = await requestList({
                        params: {
                            page: current,
                            pageSize,
                            ...formParams,
                        },
                    });
                    resolve(result.data);
                }
                proc().then();
            })

            const {list,total}:{list:Array<Stat>,total:number} = (await Promise.all([fetchStatsInfo]))[0];
            const statsInfo = list;
            
            const fetchPrivilegeInfo:Promise<Record<number,PermissionsEnum[]>> = new Promise<Record<number,PermissionsEnum[]>>((resolve, reject) => {
                const statIds = statsInfo?.map(z => z.id);
                const proc = async () => {
                    const result:ResultData<Record<number,PermissionsEnum[]>> = await requestPrivilegeCheck({type:"stat",ids:statIds});
                    resolve(result.data);
                }
                proc().then();
            })
            
            Promise.all([fetchPrivilegeInfo])
                .then(([r1]) => {
                    const listData = statsInfo?.reduce((result:StatPagination[],item:Stat) => {
                        const statPagination = { ...item, ...{"key":item.id,"permissions":r1[item.id]}};
                        result.push(statPagination);
                        return result;
                    },[]);
                    setListData(listData);
                    setPagination({
                        ...pagination,
                        current,
                        pageSize,
                        total: total});
                }).catch((error) => {
                    console.log(error);
                    Message.error(t['system.error']);
                }).finally(() => {
                    setLoading(false);
                })
         }
        fetchData().then();
    },[pagination.current, pagination.pageSize, JSON.stringify(formParams)])


    return (
        <>
        <Table border={false}
               size={"small"} columns={columns}
               data={listData}
               onChange={onChangeTable}
               pagination={pagination}
               loading={loading}/>
            {detailVisible && <Detail statInfo={currentItem} onClose={() => setDetailVisible(false)}/>}
            {updateModalVisible && <StatUpdateModal statInfo={currentItem} onClose={() => setUpdateModalVisible(false)} />}
        </>
        );
}