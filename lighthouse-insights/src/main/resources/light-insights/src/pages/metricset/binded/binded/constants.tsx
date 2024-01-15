import React from 'react';
import {Button, Typography, Space, Link, Popconfirm} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import { CiLock } from "react-icons/ci";
import { LuLock } from "react-icons/lu";
import { PiLockBold } from "react-icons/pi";
import {RiAppsLine} from "react-icons/ri";

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'Description',
            dataIndex: 'desc',
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            render: (_, record) => {
                return <Button key={getRandomString()}
                    // onClick={() => callback(record, 'update')}
                               type="text"
                               size="mini">
                    {'选择'}
                </Button>;
            }
        },
    ];
}