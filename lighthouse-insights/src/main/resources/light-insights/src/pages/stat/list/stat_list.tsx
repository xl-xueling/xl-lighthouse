import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Tabs
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
import GroupStatistics from "@/pages/project/manage/statistic-list";
import {Column, Department, Group, PrivilegeEnum, Project, Stat, StatPagination, User} from "@/types/insights-web";
import {requestQueryByIds as requestQueryGroupByIds} from "@/api/group";
import {requestQueryByIds as requestQueryProjectByIds} from "@/api/project";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/components/edittable/EditTable";
import StatEditPanel from "@/pages/project/manage/stat_edit";
import GroupEditPanel from "@/pages/project/manage/group_edit";
import {requestPrivilegeCheck} from "@/api/privilege";
import {ResultData} from "@/types/insights-common";

export default function StatisticalListPanel({statsInfo}:{statsInfo:Array<Stat>}) {

    const [loading,setLoading] = useState<boolean>(true);

    const [listData,setListData] = useState<Array<StatPagination>>([]);

    useEffect(() => {
        if(!statsInfo || !statsInfo.length){
            return;
        }
        setLoading(true);
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

        const data = Promise.all([fetchPrivilegeInfo,fetchProjectInfo,fetchGroupInfo])
            .then(([r1,r2,r3]) => {
                const combinePaginationData = statsInfo.reduce((result:StatPagination[],item:Stat) => {
                        const combinedItem = { ...item, ...{"key":item.id,"permissions":r1[item.id],"project":r2[item.projectId],"group":r3[item.groupId]}};
                        result.push(combinedItem);
                        return result;
                    },[]);
                setListData(combinePaginationData);
            }).catch((error) => {
                console.log(error)
            }).finally(() => {
                setLoading(false);
            })
    },[statsInfo])


    const columns: TableColumnProps[] = [
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'Project',
            dataIndex: 'project.name',
        },
        {
            title: 'Group',
            dataIndex: 'group.token',
        },
        {
            title: 'Department',
            dataIndex: 'department2',
        },
        {
            title: 'TimeParam',
            dataIndex: 'timeparam',
        },
        {
            title: 'Expired',
            dataIndex: 'expired',
        },
        {
            title: 'Operate',
            dataIndex: 'operate',
        },
    ];

    return (
        <Table border={false} size={"small"} columns={columns} data={listData} loading={loading}/>
    );
}