import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link, Badge} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStamp} from "@/utils/util";

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
            render: (value,record) =>
                <UserGroup users={[value]}/>
            ,
        },
        {
            title: t['approveList.columns.type'],
            dataIndex: 'orderType',
            render: (value) => {
                if(value == '1'){
                    return <Text>{t['approveList.columns.type.project.access']}</Text>;
                }else if(value == '2'){
                    return <Text>{t['approveList.columns.type.stat.access']}</Text>;
                }else if(value == '3'){
                    return <Text>{t['approveList.columns.type.metrics.access']}</Text>;
                }else if(value == '4'){
                    return <Text>{t['approveList.columns.type.adjust.limited.threshold']}</Text>;
                }else if(value == '5'){
                    return <Text>{t['approveList.columns.type.stat.pend.approve']}</Text>;
                }else if(value == '6'){
                    return <Text>{t['approveList.columns.type.user.pend.approve']}</Text>;
                }
            },
        },
        {
            title: t['approveList.columns.desc'],
            dataIndex: 'detail',
            render: (value,record) =>
            {
                return "--";
            }
        },
        {
            title: t['approveList.columns.admins'],
            dataIndex: 'admins',
            render: (value) => {
                return (<UserGroup users={value}/>);
            },
        },
        {
            title: t['approveList.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStamp(value)},
        },
        {
            title: t['approveList.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                if(value === 0){
                    return <Badge status="processing" text={t['approveList.columns.state.pending']}/>;
                }else if (value === 1) {
                    return <Badge status="success" text={t['approveList.columns.state.approved']}/>;
                }else if(value === 2){
                    return <Badge status="error" text={t['approveList.columns.state.rejected']}/>;
                }else if(value === 3){
                    return <Badge status="error" text={t['approveList.columns.state.retracted']}/>;
                }
            },
        },
        {
            title: t['approveList.columns.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'250px' },
            render: (_, record) => (
                <Space size={0} direction="horizontal">
                    <Button
                        onClick={() => callback(record, 'update')}
                        type="text"
                        size="mini">
                        {t['approveList.columns.operations.process']}
                    </Button>
                </Space>
            ),
        },
    ]
}
