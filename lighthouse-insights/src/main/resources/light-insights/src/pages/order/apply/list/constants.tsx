import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link, Badge} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import {OrderStateEnum, PermissionEnum} from "@/types/insights-common";
import {getOrderStateDescription, getOrderTypeDescription} from "@/pages/common/desc/base";

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: t['applyList.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['applyList.columns.type'],
            dataIndex: 'orderType',
            render: (value,record) => {
                return getOrderTypeDescription(t,record.orderType)
            },
        },
        {
            title: t['applyList.columns.desc'],
            dataIndex: 'detail',
            render: (value,record) =>
            {
                return "--";
            }
        },
        {
            title: t['applyList.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStampBackUp(value)},
        },
        {
            title: t['applyList.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                return getOrderStateDescription(t,value)
            },
        },
        {
            title: t['applyList.columns.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'250px' },
            render: (_, record) => {
                let viewButton;
                let retractButton;
                if(record.state == OrderStateEnum.Processing && record.permissions.includes(PermissionEnum.ManageAble)){
                    viewButton =
                        <Button key={getRandomString()}
                            onClick={() => callback(record, 'detail')}
                            type="text"
                            size="mini">
                            {t['applyList.columns.operations.detail']}
                        </Button>
                    retractButton =
                        <Popconfirm key={getRandomString()}
                                    focusLock
                                    position={"tr"}
                                    title='Confirm'
                                    content={t['applyList.columns.operations.retracted.confirm']}
                                    onOk={() => callback(record, 'retract')}
                        >
                            <Button
                                type="text"
                                size="mini">
                                {t['applyList.columns.operations.retracted']}
                            </Button>
                        </Popconfirm>
                }else{
                    viewButton =
                        <Button key={getRandomString()}
                            onClick={() => callback(record, 'detail')}
                            type="text"
                            size="mini">
                            {t['applyList.columns.operations.detail']}
                        </Button>
                }
                return  <Space size={0} direction="horizontal">{[viewButton,retractButton]}</Space>;
            },
        },
    ]
}
