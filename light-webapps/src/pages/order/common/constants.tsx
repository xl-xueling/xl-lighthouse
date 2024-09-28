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

export function getUserApproveDescription(t: any,orderInfo:Order) {
    return [
        {
            label: t['detailModal.user.approve.columns.id'],
            value: orderInfo?.user?.id,
        },
        {
            label: t['detailModal.user.approve.columns.username'],
            value: orderInfo?.user?.username,
        },
        {
            label: t['detailModal.user.approve.columns.email'],
            value: orderInfo?.user?.email,
        },
        {
            label: t['detailModal.user.approve.columns.department'],
            value: <DepartmentLabel departmentId={orderInfo?.user?.departmentId}/>,
        },
        {
            label: t['detailModal.user.approve.columns.createTime'],
            value:  formatTimeStampBackUp(orderInfo?.user?.createTime),
        },
    ];
}


export function getStatAccessDescription(t: any,orderInfo:Order) {
    return [
        {
            label: t['detailModal.statAccess.columns.id'],
            value: orderInfo?.extend?.id,
        },
        {
            label: t['detailModal.statAccess.columns.title'],
            value: orderInfo?.extend?.title,
        },
        {
            label: t['detailModal.statAccess.columns.relationship'],
            value: orderInfo?.extend?.projectTitle + ' > ' + orderInfo?.extend?.token,
        },
        {
            label: t['detailModal.statAccess.columns.department'],
            value: <DepartmentLabel departmentId={orderInfo?.extend?.departmentId}/>,
        },
        {
            label: t['detailModal.statAccess.columns.desc'],
            value: orderInfo?.extend?.desc ,
            span: 3,
        },
    ];
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

export function getViewAccessDescription(t: any,orderInfo:Order) {
    return [
        {
            label: t['detailModal.viewAccess.columns.id'],
            value:  orderInfo?.extend?.id,
        },
        {
            label:  t['detailModal.viewAccess.columns.title'],
            value:  orderInfo?.extend?.title,
        },
        {
            label: t['detailModal.viewAccess.columns.admins'],
            value: <DepartmentLabel departmentId={orderInfo?.extend?.departmentId}/>,
        },
        {
            label: t['detailModal.viewAccess.columns.desc'],
            value: orderInfo?.extend?.desc,
            span: 2,
        },
    ];
}

export function getProjectAccessDescription(t: any,orderInfo:Order) {
    return [
        {
            label: t['detailModal.projectAccess.columns.id'],
            value:  orderInfo?.extend?.id,
        },
        {
            label: t['detailModal.projectAccess.columns.title'],
            value:  orderInfo?.extend?.title,
        },
        {
            label: t['detailModal.projectAccess.columns.department'],
            value: <DepartmentLabel departmentId={orderInfo?.extend?.departmentId}/>,
        },
        {
            label: t['detailModal.projectAccess.columns.admins'],
            value: <UserGroup users={orderInfo?.extend?.admins}/>
        },
        {
            label: t['detailModal.projectAccess.columns.desc'],
            value: orderInfo?.extend?.desc,
            span: 2,
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

export function getLimitingSettingsDescription(t: any,orderInfo:Order) {
    return [
        {
            label: t['detailModal.limitingSettings.columns.id'],
            value:  orderInfo?.extend?.id,
        },
        {
            label: t['detailModal.limitingSettings.columns.token'],
            value:  orderInfo?.extend?.token,
        },
        {
            label: t['detailModal.limitingSettings.columns.project'],
            value:  orderInfo?.extend?.projectTitle,
        },
        {
            label: t['detailModal.limitingSettings.columns.strategy'],
            value: orderInfo?.extendConfig?.strategy
        },
        {
            label: t['detailModal.limitingSettings.columns.currentValue'],
            value: orderInfo?.extendConfig?.currentValue,
        },
        {
            label: t['detailModal.limitingSettings.columns.updateValue'],
            value: orderInfo?.extendConfig?.updateValue,
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