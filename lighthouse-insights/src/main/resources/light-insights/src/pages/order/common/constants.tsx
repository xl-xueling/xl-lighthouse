import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link, Badge} from '@arco-design/web-react';
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStamp} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import {RoleTypeEnum} from "@/types/insights-common";
import {Order} from "@/types/insights-web";
const { Text } = Typography;

export function getOrderColumns(t: any) {
    return [
        {
            title: t['order.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.columns.user'],
            dataIndex: 'user',
            render: (value,record) =>
                <UserGroup users={[value]}/>
            ,
        },
        {
            title: t['order.columns.type'],
            dataIndex: 'orderType',
            render: (value) => {
                if(value == '1'){
                    return <Text>{t['order.columns.type.project.access']}</Text>;
                }else if(value == '2'){
                    return <Text>{t['order.columns.type.stat.access']}</Text>;
                }else if(value == '3'){
                    return <Text>{t['order.columns.type.metrics.access']}</Text>;
                }else if(value == '4'){
                    return <Text>{t['order.columns.type.adjust.limited.threshold']}</Text>;
                }else if(value == '5'){
                    return <Text>{t['order.columns.type.stat.pend.approve']}</Text>;
                }else if(value == '6'){
                    return <Text>{t['order.columns.type.user.pend.approve']}</Text>;
                }
            },
        },
        {
            title: t['order.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStamp(value)},
        },
        {
            title: t['order.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                if(value === 0){
                    return <Badge status="processing" text={t['order.columns.state.process']}/>;
                }else if (value === 1) {
                    return <Badge status="success" text={t['order.columns.state.approved']}/>;
                }else if(value === 2){
                    return <Badge status="error" text={t['order.columns.state.rejected']}/>;
                }else if(value === 3){
                    return <Badge status="error" text={t['order.columns.state.retracted']}/>;
                }
            },
        }
    ];
}

export function getUserApproveColumns(t: any) {
    return [
        {
            title: t['order.user.approve.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.username'],
            dataIndex: 'username',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.email'],
            dataIndex: 'email',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.department'],
            dataIndex: 'department',
            render: (value,record) =>
                <DepartmentLabel department={value}/>
            ,
        },
        {
            title: t['order.user.approve.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStamp(value)},
        },
        {
            title: t['order.user.approve.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                if(value === 0){
                    return <Badge status="processing" text={t['order.user.approve.columns.state.pending']}/>;
                }else if (value === 1) {
                    return <Badge status="success" text={t['order.user.approve.columns.state.normal']}/>;
                }else if(value === 2){
                    return <Badge status="error" text={t['order.user.approve.columns.state.frozen']}/>;
                }else if(value === 3){
                    return <Badge status="error" text={t['order.user.approve.columns.state.deleted']}/>;
                }
            },
        }
    ];
}

export function getOrderDetailColumns(t: any,orderInfo:Order) {
    return [
        {
            title: t['order.detail.columns.id'],
            dataIndex: 'id',
            cellStyle:{
                display:"none",
            },
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.detail.columns.roleType'],
            dataIndex: 'roleType',
            render: (value,record) => {
                if(value === RoleTypeEnum.FULL_MANAGE_PERMISSION){
                    return t['order.detail.columns.roleType.systemManager'];
                }else if(value == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION){
                    return t['order.detail.columns.roleType.departmentManager'];
                }else if(value == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
                    return t['order.detail.columns.roleType.projectManager'];
                }else if(value == RoleTypeEnum.OPT_MANAGE_PERMISSION){
                    return t['order.detail.columns.roleType.operateManager'];
                }
            },
        },
        {
            title: t['order.detail.columns.approver'],
            dataIndex: 'user',
            render: (value,record) =>
                <UserGroup users={orderInfo.adminsMap[record.roleId]}/>
            ,
        },
        {
            title: t['order.detail.columns.approve.state'],
            dataIndex: 'state',
            render: (value) => {
                if(value === 0){
                    return <Badge status="processing" text={t['order.detail.columns.approve.state.pending']}/>;
                }else if (value === 1) {
                    return <Badge status="success" text={t['order.detail.columns.approve.state.approved']}/>;
                }else if(value === 2){
                    return <Badge status="error" text={t['order.detail.columns.approve.state.rejected']}/>;
                }else if(value === 3){
                    return <Badge status="error" text={t['order.detail.columns.approve.state.retracted']}/>;
                }
            },
        },
        {
            title: t['order.detail.columns.approve.approveTime'],
            dataIndex: 'approveTime',
            render: (value,record) => {
                if(value){
                    return <Text>{formatTimeStamp(value)}</Text>
                }
            }
            ,
        },
        {
            title: t['order.detail.columns.approve.apply'],
            dataIndex: 'apply',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
    ];
}