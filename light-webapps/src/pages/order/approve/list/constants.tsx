import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link, Badge} from '@arco-design/web-react';
const { Text } = Typography;
import UserGroup from "@/pages/user/common/groups";
import {OrderStateEnum, OrderTypeEnum, PermissionEnum} from "@/types/insights-common";
import {getOrderStateDescription, getOrderTypeDescription} from "@/pages/common/desc/base";
import {formatString, formatTimeStampBackUp} from "@/utils/util";

const getApproveDescription = (t: any, orderInfo) => {
    if(orderInfo.orderType == OrderTypeEnum.PROJECT_ACCESS){
        return formatString(t['approveList.description.projectAccess'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.STAT_ACCESS){
        return formatString(t['approveList.description.statAccess'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.METRIC_ACCESS){
        return formatString(t['approveList.description.metricAccess'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.VIEW_ACCESS){
        return formatString(t['approveList.description.viewAccess'],orderInfo?.extend?.title)
    }else if(orderInfo.orderType == OrderTypeEnum.LIMITING_SETTINGS){
        return formatString(t['approveList.description.updateLimitingThreshold'],orderInfo?.extend?.token)
    } else if(orderInfo.orderType == OrderTypeEnum.USER_PEND_APPROVE){
        return formatString(t['approveList.description.userPendApprove'],orderInfo?.extend?.username)
    } else if(orderInfo.orderType == OrderTypeEnum.STAT_PEND_APPROVE){
        return formatString(t['approveList.description.statPendApprove'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS){
        return formatString(t['approveList.description.callerProjectAccess'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.project?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_STAT_ACCESS){
        return formatString(t['approveList.description.callerStatAccess'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.stat?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS){
        return formatString(t['approveList.description.callerViewAccess'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.view?.title)
    }
}

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: t['approveList.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['approveList.columns.user'],
            dataIndex: 'user',
            render: (value,record) => {
                return <UserGroup users={[value]}/>;
            },
        },
        {
            title: t['approveList.columns.type'],
            dataIndex: 'orderType',
            render: (value,record) => {
                return getOrderTypeDescription(t,record.orderType)
            },
        },
        {
            title: t['approveList.columns.desc'],
            dataIndex: 'detail',
            render: (value,record) =>
            {
                return getApproveDescription(t,record);
            }
        },
        {
            title: t['approveList.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStampBackUp(value)},
        },
        {
            title: t['approveList.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                return getOrderStateDescription(t,value)
            },
        },
        {
            title: t['approveList.columns.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'250px' },
            render: (_, record) => {
                if(record.state == OrderStateEnum.Processing && record.permissions.includes(PermissionEnum.ManageAble)){
                    return  <Space size={0} direction="horizontal">
                        <Button
                            onClick={() => callback(record, 'process')}
                            type="text"
                            size="mini">
                            {t['approveList.columns.operations.process']}
                        </Button>
                    </Space>
                }else{
                    return  <Space size={0} direction="horizontal">
                        <Button
                            onClick={() => callback(record, 'detail')}
                            type="text"
                            size="mini">
                            {t['approveList.columns.operations.detail']}
                        </Button>
                    </Space>
                }

            },
        },
    ]
}
