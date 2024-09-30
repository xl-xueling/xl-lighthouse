import {Project} from "@/types/insights-web";
import {PermissionEnum, ResourceTypeEnum} from "@/types/insights-common";
import ProjectLabel from "@/pages/project/common/ProjectLabel";
import StatLabel from "@/pages/stat/common/StatLabel";
import ViewLabel from "@/pages/view/common/ViewLabel";
import {Button, Popconfirm, Space, Typography} from "@arco-design/web-react";
import React from "react";
import DepartmentLabel from "@/pages/department/common/depart";
import UserGroup from "@/pages/user/common/groups";
import {getRandomString} from "@/utils/util";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";


export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: t['callerAuthList.column.label.id'],
            dataIndex: 'id',
        },
        {
            title:t['callerAuthList.column.label.title'],
            dataIndex: 'title',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project && record?.extend){
                    return (
                        <ProjectLabel projectInfo={record?.extend} />
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat && record?.extend){
                    return (
                        <StatLabel statInfo={record?.extend}/>
                    )
                }else if(record.resourceType == ResourceTypeEnum.View && record?.extend){
                    return (
                        <ViewLabel viewInfo={record?.extend}/>
                    )
                }else{
                    return (
                        <Typography.Text type="secondary">
                            {t['basic.warning.relateElementDeleted']}
                        </Typography.Text>
                    )
                }
            }
        },
        {
            title: t['callerAuthList.column.label.resourceType'],
            dataIndex: 'resourceType',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        t['basic.resourceType.project']
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        t['basic.resourceType.statistic']
                    )
                }else if(record.resourceType == ResourceTypeEnum.View){
                    return (
                        t['basic.resourceType.view']
                    )
                }else{
                    return '--';
                }

            }
        },
        {
            title: t['callerAuthList.column.label.Department'],
            dataIndex: 'departmentId',
            render: (value,record) =>
            {
                return record.extend?.departmentId?<DepartmentLabel departmentId={record.extend?.departmentId} />:'--'
            }
        },
        {
            title: t['callerAuthList.column.label.expired'],
            dataIndex: 'expireTime',
            render: (value,record) =>
            {
                return (formatTimeStamp(value,DateTimeFormat))
            }
        },
        {
            title: t['callerAuthList.column.label.admins'],
            dataIndex: 'admins',
            render: (value,record) =>
            {
                return (<UserGroup users={record.extend?.admins}/>)
            }
        },
        {
            title: t['callerAuthList.column.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'250px' },
            render: (_, record) => {
                const itemPermission = record.extend?.permissions;
                let extensionButton = <Button key={getRandomString()} onClick={() => callback(record, 'extension')}
                        type="text"
                        size="mini">
                    {'续签'}
                </Button>
                let removeButton =
                    <Popconfirm key={getRandomString()}
                                focusLock
                                position={"tr"}
                                title='Confirm'
                                content={t['callerAuthList.column.label.operations.remove.confirm']}
                                onOk={() => callback(record, 'remove')}
                    >
                        <Button key={getRandomString()}
                                type="text"
                                size="mini">
                            {'删除'}
                        </Button>
                    </Popconfirm>
                return  <Space size={0} direction="horizontal">{[extensionButton,removeButton]}</Space>;
            }
        }
    ];

}