import React from 'react';
import {Button, Typography, Space, Link, Popconfirm, TreeSelect} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStamp, getRandomString} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import { CiLock } from "react-icons/ci";
import { LuLock } from "react-icons/lu";
import { PiLockBold } from "react-icons/pi";
import {RiAppsLine} from "react-icons/ri";
import {translateResponse} from "@/pages/department/common";

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
            render: (value,record) =>
            {
                return (
                        <span style={{display:"inline-flex",alignItems:"center"}}>{value}{record.privateType == 0 ?<CiLock style={{marginLeft:'5px'}}/>:null}</span>
                    )
            }
        },
        {
            title: 'Configuration',
            dataIndex: 'configuration',
            render: (value, record) => {
                return <TreeSelect
                    placeholder={"Please Select"}
                    multiple={true}
                    allowClear={true}
                    treeData={translateResponse(value)}
                />;
            }
        },
        {
            title: 'Admin',
            dataIndex: 'user',
            render: (value) => {
                return (<UserGroup users={[value]}/>);
            },
        },
        {
            title: 'Operations',
            dataIndex: 'operations',
            headerCellStyle: {width: '250px'},
            render: (value, record) => {
                let updateButton;
                if(record.permissions.includes('ManageAble')){
                    updateButton = <Button key={getRandomString()}
                                           onClick={() => callback(record, 'update')}
                                           type="text"
                                           size="mini">
                        {'修改'}
                    </Button>;
                }
                return  <Space size={0} direction="horizontal">{[updateButton]}</Space>;
            }
        }
    ];
}