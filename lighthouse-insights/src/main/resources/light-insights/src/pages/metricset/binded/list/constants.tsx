import React from 'react';
import {Button, Typography, Space, Link, Popconfirm} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import { CiLock } from "react-icons/ci";
import { LuLock } from "react-icons/lu";
import { PiLockBold } from "react-icons/pi";
import {RiAppsLine} from "react-icons/ri";
import {PermissionEnum, ResourceTypeEnum} from "@/types/insights-common";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import {MetricSet} from "@/types/insights-web";



export function getColumns(t: any, metricSetInfo:MetricSet, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: t['bindedList.list.column.label.id'],
            dataIndex: 'id',
        },
        {
            title:t['bindedList.list.column.label.title'],
            dataIndex: 'title',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        record.extend?.title
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        record.extend?.title
                    )
                }
            }
        },
        {
            title: t['bindedList.list.column.label.elementType'],
            dataIndex: 'relationType',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        t['bindedList.list.elementType.project']
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        t['bindedList.list.elementType.stat']
                    )
                }

            }
        },
        {
            title: t['bindedList.list.column.label.department'],
            dataIndex: 'department',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        <DepartmentLabel departmentId={record.extend?.departmentId} />
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        <DepartmentLabel departmentId={record.extend?.project?.departmentId} />
                    )
                }
            }
        },
        {
            title: t['bindedList.list.column.label.admins'],
            dataIndex: 'admins',
            render: (value,record) =>
            {
                return (<UserGroup users={record.extend?.admins}/>)
            }
        },
        {
            title: t['bindedList.list.column.label.bindTime'],
            dataIndex: 'createTime',
            render: (value,record) =>
            {
                return (formatTimeStamp(value,DateTimeFormat))
            }
        },
        {
            title: t['bindedList.list.column.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'250px' },
            render: (_, record) => {
                const itemPermission = record.extend?.permissions;
                console.log("itemPermissions length:" + itemPermission.length);
                let removeButton;
                let applyButton;
                if(metricSetInfo.permissions.includes(PermissionEnum.ManageAble)){
                    removeButton =
                        <Popconfirm key={getRandomString()}
                                    focusLock
                                    position={"tr"}
                                    title='Confirm'
                                    content={t['bindedList.list.column.label.operations.remove.confirm']}
                                    onOk={() => callback(record, 'remove')}
                        >
                        <Button key={getRandomString()}
                                           type="text"
                                           size="mini">
                        {t['bindedList.list.column.label.operations.remove']}
                    </Button>
                        </Popconfirm>
                }
                if(itemPermission.length == 0){
                    applyButton =  <Button key={getRandomString()}
                                           type="text"
                                           onClick={() => callback(record,'apply')}
                                           size="mini">
                        {t['bindedList.list.column.label.operations.apply']}
                    </Button>
                }
                return  <Space size={0} direction="horizontal">{[removeButton,applyButton]}</Space>;
            }
        }
    ];
}