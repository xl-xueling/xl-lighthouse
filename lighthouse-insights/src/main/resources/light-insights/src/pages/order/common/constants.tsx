import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link, Badge} from '@arco-design/web-react';
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import {OrderStateEnum, RoleTypeEnum} from "@/types/insights-common";
import {Order} from "@/types/insights-web";
import {getOrderDetailStateDescription, getOrderStateDescription} from "@/pages/common/desc/base";
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
            render: (value) => {
                if(value == '1'){
                    return <Text>{t['detailModal.columns.type.project.access']}</Text>;
                }else if(value == '2'){
                    return <Text>{t['detailModal.columns.type.stat.access']}</Text>;
                }else if(value == '3'){
                    return <Text>{t['detailModal.columns.type.metrics.access']}</Text>;
                }else if(value == '4'){
                    return <Text>{t['detailModal.columns.type.adjust.limited.threshold']}</Text>;
                }else if(value == '5'){
                    return <Text>{t['detailModal.columns.type.stat.pend.approve']}</Text>;
                }else if(value == '6'){
                    return <Text>{t['detailModal.columns.type.user.pend.approve']}</Text>;
                }
            },
        },
        {
            title: t['detailModal.columns.createTime'],
            dataIndex: 'createTime',
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
                if(value === RoleTypeEnum.FULL_MANAGE_PERMISSION){
                    return t['detailModal.detail.columns.roleType.systemManager'];
                }else if(value == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION){
                    return t['detailModal.detail.columns.roleType.departmentManager'];
                }else if(value == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
                    return t['detailModal.detail.columns.roleType.projectManager'];
                }else if(value == RoleTypeEnum.OPT_MANAGE_PERMISSION){
                    return t['detailModal.detail.columns.roleType.operateManager'];
                }
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