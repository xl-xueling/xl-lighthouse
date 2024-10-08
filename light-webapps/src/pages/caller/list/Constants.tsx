
import React from "react";
import {Button, Popconfirm, Space,Link} from "@arco-design/web-react";
import Ellipsis from "@arco-design/web-react/es/Typography/ellipsis";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import {getRandomString} from "@/utils/util";
import { useLocation, useHistory } from 'react-router-dom';
import DepartmentLabel from "@/pages/department/common/depart";

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: t['callerList.columns.id'],
            dataIndex: 'id',
        },
        {
            title: t['callerList.columns.name'],
            dataIndex: 'name',
        },
        // {
        //     title: 'Description',
        //     dataIndex: 'desc',
        //     headerCellStyle: { width:'500px' },
        //     render:(value,record) => {
        //         return <Ellipsis rows={2} expandable={false}>{value}</Ellipsis>
        //     }
        // },
        {
            title: t['callerList.columns.department'],
            dataIndex: 'departmentId',
            render: (value,record) => <DepartmentLabel departmentId={value}/> ,
        },
        {
            title: t['callerList.columns.createdTime'],
            dataIndex: 'createTime',
            render: (value, record) => {
                return formatTimeStamp(value,DateTimeFormat) ;
            }
        },
        {
            title: t['callerList.columns.admins'],
            dataIndex: 'admins',
            render: (value) => {
                return (<UserGroup users={value}/>);
            },
        },
        {
            title: t['callerList.columns.operations'],
            dataIndex: 'operations',
            render: (value, record) => {
                let manageButton = <Link key={getRandomString()} href={`/caller/manage/${record.id}`} onClick={(e) => {e.preventDefault();callback(record, 'manage')}} style={{ textDecoration: 'none' }}>
                    <Button type="text" size="mini">
                        {t['callerList.columns.operations.manage']}
                    </Button>
                </Link>
                let deleteButton = <Popconfirm key={getRandomString()}
                                               focusLock
                                               position={"tr"}
                                               title='Confirm'
                                               content={t['callerList.columns.operations.delete.confirm']}
                                               onOk={() => callback(record, 'delete')}>
                        <Button key={getRandomString()}
                                               type="text"
                                               size="mini">
                        {t['callerList.columns.operations.delete']}
                    </Button>
                </Popconfirm>
                return  <Space key={getRandomString()} size={0} direction="horizontal">{[manageButton,deleteButton]}</Space>;
            }
        },
    ];
}