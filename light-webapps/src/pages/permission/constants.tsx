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
import {getRoleTypeDescription} from "@/pages/common/desc/base";

export function getUserPermissionColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: 'ID',
            dataIndex: 'id',
            headerCellStyle: { width:'20px' },
        },
        {
            title: 'RoleType',
            dataIndex: 'roleType',
            headerCellStyle: { width:'20px' },
            render: (value, record) => {
                return getRoleTypeDescription(t,value);
            }
        },
        {
            title: 'User',
            dataIndex: 'extend',
            headerCellStyle: { width:'20px' },
            render: (value, record) => {
                console.log("record:" + record);
                return <UserGroup users={[value]}/>
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
                const button = <Popconfirm key={getRandomString()}
                                           focusLock
                                           position={"tr"}
                                           title='Confirm'
                                           content={t['permissionManage.list.operation.remove.confirm']}
                                           onOk={() => callback(record, 'release')}
                >
                    <Button key={getRandomString()}
                            type="text"
                            size="mini">
                        {t['permissionManage.list.operation.remove']}
                    </Button>
                </Popconfirm>
                return  <Space size={0} direction="horizontal">{[button]}</Space>;
            }
        }
    ]
}

export function getDepartPermissionColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: 'ID',
            dataIndex: 'id',
            headerCellStyle: { width:'20px' },
        },
        {
            title: 'RoleType',
            dataIndex: 'roleType',
            headerCellStyle: { width:'20px' },
            render: (value, record) => {
                return getRoleTypeDescription(t,value);
            }
        },
        {
            title: 'Department',
            dataIndex: 'ownerId',
            headerCellStyle: { width:'20px' },
            render: (value, record) => <DepartmentLabel departmentId={value} />
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
                const button = <Popconfirm key={getRandomString()}
                                           focusLock
                                           position={"tr"}
                                           title='Confirm'
                                           content={t['permissionManage.list.operation.remove.confirm']}
                                           onOk={() => callback(record, 'release')}
                >
                    <Button key={getRandomString()}
                                       type="text"
                                       size="mini">
                    {t['permissionManage.list.operation.remove']}
                </Button>
                </Popconfirm>
                return  <Space size={0} direction="horizontal">{[button]}</Space>;
            }
        }
    ]
}