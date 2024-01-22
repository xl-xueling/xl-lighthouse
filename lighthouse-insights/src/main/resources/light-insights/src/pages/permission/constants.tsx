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
            headerCellStyle: { width:'20px' },
            render: (_, record) => {
                return <Button icon={<PiLinkSimple/>} size={"mini"} shape={"round"} onClick={() => {callback(record, 'binded')}}/>
            }
        },
        {
            title: 'Department',
            dataIndex: 'departmentName',
            headerCellStyle: { width:'20px' },
            render: (_, record) => {
                return <Button icon={<PiLinkSimple/>} size={"mini"} shape={"round"} onClick={() => {callback(record, 'binded')}}/>
            }
        },
        {
            title: 'CreateTime',
            dataIndex: 'createTime',
            headerCellStyle: { width:'20px' },
            render: (_, record) => {
                return <Button icon={<PiLinkSimple/>} size={"mini"} shape={"round"} onClick={() => {callback(record, 'binded')}}/>
            }
        }
    ]
}