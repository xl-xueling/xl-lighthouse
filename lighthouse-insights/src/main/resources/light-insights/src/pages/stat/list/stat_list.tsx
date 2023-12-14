import {
    Table,
    Message, PaginationProps, Card, Typography
} from '@arco-design/web-react';
import React, {useEffect, useMemo, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {useSelector} from "react-redux";
import locale from './locale';
import { Department, Group, PrivilegeEnum, Project, Stat, StatPagination, User} from "@/types/insights-web";
import {requestQueryByIds as requestQueryGroupByIds} from "@/api/group";
import {requestQueryByIds as requestQueryProjectByIds} from "@/api/project";
import {requestList} from "@/api/stat";
import {requestPrivilegeCheck} from "@/api/privilege";
import {ResultData} from "@/types/insights-common";
import {getColumns, getColumnsOfManage} from "@/pages/stat/list/constants";
import {requestFavoriteProject, requestUnFavoriteProject} from "@/api/favorites";
import Detail from "@/pages/stat/list/detail";
import {requestQueryByIds} from "@/api/user";

export default function StatisticalListPanel({formParams,from = null}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);

    const [listData,setListData] = useState<Array<StatPagination>>([]);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [selectedItem,setSelectedItem] = useState<StatPagination>(null);

    const tableCallback = async (record, type) => {
        if(type == 'detail'){
            setSelectedItem(record);
            setDetailVisible(!detailVisible);
        }
    };

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const [favoriteIds,setFavoriteIds] = useState<Array<number>>([]);
    const columns = useMemo(() => (from && from == 'group-manage') ? getColumnsOfManage(t, tableCallback) : getColumns(t, favoriteIds,tableCallback), [t,favoriteIds]);
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
            
            const fetchPrivilegeInfo:Promise<Record<number,PrivilegeEnum[]>> = new Promise<Record<number,PrivilegeEnum[]>>((resolve, reject) => {
                const statIds = statsInfo?.map(z => z.id);
                const proc = async () => {
                    const result:ResultData<Record<number,PrivilegeEnum[]>> = await requestPrivilegeCheck({type:"stat",ids:statIds});
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
        <Card>
        <Table border={false}
               size={"small"} columns={columns}
               data={listData}
               onChange={onChangeTable}
               pagination={pagination}
               loading={loading}/>
            {detailVisible && <Detail statInfo={selectedItem} onClose={() => setDetailVisible(false)}/>}
        </Card>
    );
}