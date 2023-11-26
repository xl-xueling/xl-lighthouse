import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Tabs, PaginationProps
} from '@arco-design/web-react';
import {
    IconDashboard, IconFile,
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen, IconPenFill,
    IconPlus,
    IconPlusCircleFill, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {Column, Department, Group, PrivilegeEnum, Project, Stat, StatPagination, User} from "@/types/insights-web";
import {requestQueryByIds as requestQueryGroupByIds} from "@/api/group";
import {requestQueryByIds as requestQueryProjectByIds} from "@/api/project";
import {requestList, requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/components/edittable/EditTable";
import {requestPrivilegeCheck} from "@/api/privilege";
import {ResultData} from "@/types/insights-common";

export default function StatisticalListPanel({formParams,columns}:{formParams:object,columns:TableColumnProps[]}) {

    const [loading,setLoading] = useState<boolean>(false);

    const [listData,setListData] = useState<Array<StatPagination>>([]);

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const [pagination, setPagination] = useState<PaginationProps>({
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const t = useLocale();

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
                const statIds = statsInfo!.map(z => z.id);
                const proc = async () => {
                    const result:ResultData<Record<number,PrivilegeEnum[]>> = await requestPrivilegeCheck({type:"stat",items:statIds});
                    resolve(result.data);
                }
                proc().then();
            })

            const fetchProjectInfo:Promise<Record<number,Project>> = new Promise<Record<number,Project>> ((resolve => {
                const projectIds = statsInfo.map(z => z.projectId);
                const proc = async () => {
                    const result:ResultData<Record<number,Project>> = await requestQueryProjectByIds(projectIds);
                    resolve(result.data);
                }
                proc().then();
            }))

            const fetchGroupInfo:Promise<Record<number,Group>> = new Promise<Record<number,Group>> ((resolve => {
                const groupIds = statsInfo.map(z => z.groupId);
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