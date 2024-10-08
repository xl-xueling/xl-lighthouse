import React from 'react';
import {Typography} from '@arco-design/web-react';
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import {OrderTypeEnum} from "@/types/insights-common";
import {Order} from "@/types/insights-web";
import {
    getLimitingStrategyDescription,
    getOrderApproveRoleTypeDescription,
    getOrderDetailStateDescription,
    getOrderStateDescription,
    getOrderTypeDescription
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
    let statInfo;
    if(orderInfo?.orderType == OrderTypeEnum.CALLER_STAT_ACCESS || orderInfo?.orderType == OrderTypeEnum.CALLER_STAT_ACCESS_EXTENSION){
        statInfo = orderInfo?.extend?.stat;
    }else{
        statInfo = orderInfo?.extend;
    }
    return [
        {
            label: t['detailModal.statAccess.columns.id'],
            value: statInfo?.id,
        },
        {
            label: t['detailModal.statAccess.columns.title'],
            value: statInfo?.title,
        },
        {
            label: t['detailModal.statAccess.columns.relationship'],
            value: statInfo?.projectTitle + ' > ' + statInfo?.token,
        },
        {
            label: t['detailModal.statAccess.columns.department'],
            value: <DepartmentLabel departmentId={statInfo?.departmentId}/>,
        },
        {
            label: t['detailModal.statAccess.columns.desc'],
            value: statInfo?.desc ,
            span: 3,
        },
    ];
}


export function getViewAccessDescription(t: any,orderInfo:Order) {
    let viewInfo;
    if(orderInfo?.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS  || orderInfo?.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS_EXTENSION){
        viewInfo = orderInfo?.extend?.view;
    }else{
        viewInfo = orderInfo?.extend;
    }
    return [
        {
            label: t['detailModal.viewAccess.columns.id'],
            value:  viewInfo?.id,
        },
        {
            label:  t['detailModal.viewAccess.columns.title'],
            value:  viewInfo?.title,
        },
        {
            label: t['detailModal.viewAccess.columns.admins'],
            value: <DepartmentLabel departmentId={viewInfo?.departmentId}/>,
        },
        {
            label: t['detailModal.viewAccess.columns.desc'],
            value: viewInfo?.desc,
            span: 2,
        },
    ];
}

export function getProjectAccessDescription(t: any,orderInfo:Order) {
    let projectInfo;
    if(orderInfo?.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS || orderInfo?.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS_EXTENSION){
        projectInfo = orderInfo?.extend?.project;
    }else{
        projectInfo = orderInfo?.extend;
    }
    return [
        {
            label: t['detailModal.projectAccess.columns.id'],
            value:  projectInfo?.id,
        },
        {
            label: t['detailModal.projectAccess.columns.title'],
            value:  projectInfo?.title,
        },
        {
            label: t['detailModal.projectAccess.columns.department'],
            value: <DepartmentLabel departmentId={projectInfo?.departmentId}/>,
        },
        {
            label: t['detailModal.projectAccess.columns.admins'],
            value: <UserGroup users={projectInfo?.admins}/>
        },
        {
            label: t['detailModal.projectAccess.columns.desc'],
            value: projectInfo?.desc,
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