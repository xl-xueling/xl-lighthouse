
import React from "react";
import {Button, Link, Popconfirm, Space} from "@arco-design/web-react";
import Ellipsis from "@arco-design/web-react/es/Typography/ellipsis";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import {getRandomString} from "@/utils/util";


export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Name',
            dataIndex: 'name',
        },
        {
            title: 'Description',
            dataIndex: 'desc',
            headerCellStyle: { width:'500px' },
            render:(value,record) => {
                return <Ellipsis rows={2} expandable={false}>{value}</Ellipsis>
            }
        },
        {
            title: 'CreateTime',
            dataIndex: 'createTime',
            render: (value, record) => {
                return formatTimeStamp(value,DateTimeFormat) ;
            }
        },
        {
            title: 'Admins',
            dataIndex: 'admins',
            render: (value) => {
                return (<UserGroup users={value}/>);
            },
        },
        {
            title: 'Operations',
            dataIndex: 'operations',
            render: (value, record) => {
                // let updateButton = <Button key={getRandomString()}
                //                             type="text"
                //                             onClick={() => callback(record,'update')}
                //                             size="mini">
                //     {'修改'}
                // </Button>;
                let configButton = <Link key={getRandomString()} target={"_blank"} href={'/caller/manage/'+record.id}>
                        <Button key={getRandomString()}
                                               type="text"
                                               size="mini">
                        {'管理'}
                    </Button>
                </Link>
                let deleteButton = <Popconfirm key={getRandomString()}
                                               focusLock
                                               position={"tr"}
                                               title='Confirm'
                                               content={t['callerList.operation.delete.confirm']}
                                               onOk={() => callback(record, 'delete')}>
                        <Button key={getRandomString()}
                                               type="text"
                                               size="mini">
                        {'删除'}
                    </Button>
                </Popconfirm>
                return  <Space key={getRandomString()} size={0} direction="horizontal">{[configButton,deleteButton]}</Space>;
            }
        },
    ];
}