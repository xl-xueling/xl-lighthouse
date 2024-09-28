import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link, Badge} from '@arco-design/web-react';
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import {OrderStateEnum, OrderTypeEnum, RoleTypeEnum} from "@/types/insights-common";
import {Order} from "@/types/insights-web";
import {
    getLimitingStrategyDescription, getOrderApproveRoleTypeDescription,
    getOrderDetailStateDescription,
    getOrderStateDescription, getOrderTypeDescription
} from "@/pages/common/desc/base";
const { Text } = Typography;

export function getOrderColumns(t: any) {
    return [
        {
            title: t['detailModal.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.columns.user'],
            dataIndex: 'user',
            render: (value,record) =>
                <UserGroup users={[value]}/>
            ,
        },
        {
            title: t['detailModal.columns.type'],
            dataIndex: 'orderType',
            render: (value,record) => {
                if(record.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS){
                    return getOrderTypeDescription(t,value) + '('+record?.extend?.caller?.name+')';
                }else if(record.orderType == OrderTypeEnum.CALLER_STAT_ACCESS){
                    return getOrderTypeDescription(t,value) + '('+record?.extend?.caller?.name+')';
                }else if(record.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS){
                    return getOrderTypeDescription(t,value) + '('+record?.extend?.caller?.name+')';
                }else{
                    return getOrderTypeDescription(t,value);
                }
            },
        },
        {
            title: t['detailModal.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStampBackUp(value)},
        },
        {
            title: t['detailModal.columns.updateTime'],
            dataIndex: 'updateTime',
            render: (value) => {return formatTimeStampBackUp(value)},
        },
        {
            title: t['detailModal.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                return getOrderStateDescription(t,value);
            },
        }
    ];
}

export function getUserApproveColumns(t: any) {
    return [
        {
            title: t['detailModal.user.approve.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.user.approve.columns.username'],
            dataIndex: 'username',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.user.approve.columns.email'],
            dataIndex: 'email',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.user.approve.columns.department'],
            dataIndex: 'departmentId',
            render: (value,record) => {
                return <DepartmentLabel departmentId={value}/>
            }
            ,
        },
        {
            title: t['detailModal.user.approve.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStampBackUp(value)},
        }
    ];
}


export function getStatAccessColumns(t: any) {
    return [
        {
            title: t['detailModal.statAccess.columns.id'],
            dataIndex: 'id',
            headerCellStyle: { width: '90px' },
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.statAccess.columns.title'],
            dataIndex: 'title',
            headerCellStyle: { minWidth: '150px' },
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.statAccess.columns.relationship'],
            dataIndex: 'relationship',
            headerCellStyle: { minWidth: '150px' },
            render:(value,record) => {
                return (record.projectTitle + ' > ' + record.token);
            }
        },
        // {
        //     title: t['detailModal.statAccess.columns.department'],
        //     dataIndex: 'departmentId',
        //     render: (value,record) => {
        //         return <DepartmentLabel departmentId={value}/>
        //     }
        //     ,
        // },
        {
            title: t['detailModal.statAccess.columns.desc'],
            dataIndex: 'desc',
            render: (value,record) => {
                return <Text>{value}</Text>
            }
            ,
        },
    ]
}

export function getViewAccessColumns(t: any) {
    return [
        {
            title: t['detailModal.viewAccess.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.viewAccess.columns.title'],
            dataIndex: 'title',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.viewAccess.columns.admins'],
            dataIndex: 'admins',
            render: (value,record) => {
                return <UserGroup users={value}/>
            }
        },
        {
            title: t['detailModal.viewAccess.columns.desc'],
            dataIndex: 'desc',
            render: (value,record) => {
                return <Text>{value}</Text>
            }
            ,
        },
    ];
}

export function getProjectAccessColumns(t: any) {
    return [
        {
            title: t['detailModal.projectAccess.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.projectAccess.columns.title'],
            dataIndex: 'title',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.projectAccess.columns.department'],
            dataIndex: 'departmentId',
            render: (value,record) => {
                return <DepartmentLabel departmentId={value}/>
            }
            ,
        },
        {
            title: t['detailModal.projectAccess.columns.admins'],
            dataIndex: 'admins',
            render: (value,record) => {
                return <UserGroup users={value}/>
            }
        },
        {
            title: t['detailModal.projectAccess.columns.desc'],
            dataIndex: 'desc',
            render: (value,record) => {
                return <Text>{value}</Text>
            }
            ,
        },
    ];
}

export function getLimitingSettingsColumns(t: any) {
    return [
        {
            title: t['detailModal.limitingSettings.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.limitingSettings.columns.token'],
            dataIndex: 'token',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.limitingSettings.columns.project'],
            dataIndex: 'projectTitle',
            render: (value,record) => {
                return <Text>{value}</Text>
            }
            ,
        },
        {
            title: t['detailModal.limitingSettings.columns.strategy'],
            dataIndex: 'strategy',
            render: (value,record) => {
                return <Text>{getLimitingStrategyDescription(t,value)}</Text>
            }
        },

        {
            title: t['detailModal.limitingSettings.columns.currentValue'],
            dataIndex: 'currentValue',
            render: (value,record) => {
                return <Text>{value}</Text>
            }
            ,
        },
        {
            title: t['detailModal.limitingSettings.columns.updateValue'],
            dataIndex: 'updateValue',
            render: (value,record) => {
                return <Text>{value}</Text>
            }
            ,
        },
    ];
}

export function getOrderDetailColumns(t: any,orderInfo:Order) {
    return [
        {
            title: t['detailModal.detail.columns.id'],
            dataIndex: 'id',
            cellStyle:{
                display:"none",
            },
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['detailModal.detail.columns.roleType'],
            dataIndex: 'roleType',
            render: (value,record) => {
                return getOrderApproveRoleTypeDescription(t,value);
            },
        },
        {
            title: t['detailModal.detail.columns.approver'],
            dataIndex: 'user',
            render: (value,record) =>
                <UserGroup users={orderInfo.adminsMap[record.roleId]}/>
            ,
        },
        {
            title: t['detailModal.detail.columns.approve.state'],
            dataIndex: 'state',
            render: (value) => {
                return getOrderDetailStateDescription(t,value);
            },
        },
        {
            title: t['detailModal.detail.columns.approve.approveTime'],
            dataIndex: 'processTime',
            render: (value,record) => {
                if(value){
                    return <Text>{formatTimeStampBackUp(value)}</Text>
                }
            },
        },
        {
            title: t['detailModal.detail.columns.approve.reply'],
            dataIndex: 'reply',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
    ];
}