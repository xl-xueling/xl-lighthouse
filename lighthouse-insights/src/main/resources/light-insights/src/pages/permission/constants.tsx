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
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";

export function getDepartPermissionColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: 'ID',
            dataIndex: 'id',
            headerCellStyle: { width:'20px' },
        },
        {
            title: 'Department',
            dataIndex: 'ownerId',
            headerCellStyle: { width:'20px' },
            render: (value, record) => {
                return <DepartmentLabel departmentId={value} /> ;
            }
        },
        {
            title: 'CreateTime',
            dataIndex: 'createTime',
            headerCellStyle: { width:'20px' },
            render: (value, record) => {
                return formatTimeStamp(value,DateTimeFormat) ;
            }
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            headerCellStyle: { width:'20px' },
            render: (value, record) => {
                const button = <Button key={getRandomString()}
                                       type="text"
                                       onClick={() => window.open('/project/preview/' + record.id)}
                                       size="mini">
                    {'移除'}
                </Button>;
                return  <Space size={0} direction="horizontal">{[button]}</Space>;
            }
        }
    ]
}