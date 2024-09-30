import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link, Badge} from '@arco-design/web-react';
const { Text } = Typography;
import UserGroup from "@/pages/user/common/groups";
import {OrderStateEnum, OrderTypeEnum, PermissionEnum} from "@/types/insights-common";
import {getOrderDescription, getOrderStateDescription, getOrderTypeDescription} from "@/pages/common/desc/base";
import {formatString, formatTimeStampBackUp} from "@/utils/util";

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
                return getOrderDescription(t,record);
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
            headerCellStyle: {width:'150px' },
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
