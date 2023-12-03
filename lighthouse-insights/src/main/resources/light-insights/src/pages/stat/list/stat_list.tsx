import {
    Table,
    Message, PaginationProps
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

export default function StatisticalListPanel({formParams,from = null}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);

    const [listData,setListData] = useState<Array<StatPagination>>([]);

    const tableCallback = async (record, type) => {
        console.log("record is:" + record + ",type:" + type)
        if(type == 'favorite'){
            await handlerFavoriteProject(record.id).then();
        }else if(type == 'unFavorite'){
            await handlerUnFavoriteProject(record.id).then();
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



    const handlerFavoriteProject = async (id: number) => {
        try{
            const result = await requestFavoriteProject(id);
            if(result.code == '0'){
                Message.success(t['statList.table.operations.favorite.success']);
                setFavoriteIds([...favoriteIds,id]);
            }else{
                Message.error(result.message ||t['system.error']);
            }
        }catch (error){
            console.log(error);
            Message.error(t['system.error']);
        }
    };

    const handlerUnFavoriteProject = async (id: number) => {
        try{
            const result = await requestUnFavoriteProject(id);
            if(result.code == '0'){
                Message.success(t['statList.table.operations.unfavorite.success']);
                const newArr = favoriteIds.filter((item) => item !== id);
                setFavoriteIds(newArr);
            }else{
                Message.error(result.message || t['system.error']);
            }
        }catch (error){
            console.log(error);
            Message.error(t['system.error']);
        }
    };



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
                    const result:ResultData<Record<number,PrivilegeEnum[]>> = await requestPrivilegeCheck({type:"stat",items:statIds});
                    resolve(result.data);
                }
                proc().then();
            })

            const fetchProjectInfo:Promise<Record<number,Project>> = new Promise<Record<number,Project>> ((resolve => {
                const projectIds = statsInfo?.map(z => z.projectId);
                const proc = async () => {
                    const result:ResultData<Record<number,Project>> = await requestQueryProjectByIds(projectIds);
                    resolve(result.data);
                }
                proc().then();
            }))

            const fetchGroupInfo:Promise<Record<number,Group>> = new Promise<Record<number,Group>> ((resolve => {
                const groupIds = statsInfo?.map(z => z.groupId);
                const proc = async () => {
                    const result:ResultData<Record<number,Group>> = await requestQueryGroupByIds(groupIds);
                    resolve(result.data);
                }
                proc().then();
            }))

            Promise.all([fetchPrivilegeInfo,fetchProjectInfo,fetchGroupInfo])
                .then(([r1,r2,r3]) => {
                    const combinePaginationData = statsInfo.reduce((result:StatPagination[],item:Stat) => {
                        const project:Project = r2[item.projectId];
                        const department = allDepartInfo.find(x => x.id == project.departmentId);
                        const combinedItem = { ...item, ...{"key":item.id,"permissions":r1[item.id],"project":project,"group":r3[item.groupId],"department":department}};
                        result.push(combinedItem);
                        return result;
                    },[]);
                    setListData(combinePaginationData);
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
        <Table border={false}
               size={"small"} columns={columns}
               data={listData}
               onChange={onChangeTable}
               pagination={pagination}
               loading={loading}/>
    );
}