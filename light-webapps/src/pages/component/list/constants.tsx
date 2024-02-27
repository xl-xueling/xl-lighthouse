import React from 'react';
import {Button, Typography, Space, Link, Popconfirm, TreeSelect} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import { CiLock } from "react-icons/ci";
import {translateResponse} from "@/pages/department/common";

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: t['componentList.label.id'],
            dataIndex: 'id',
        },
        {
            title: t['componentList.label.title'],
            dataIndex: 'title',
            render: (value,record) =>
            {
                return (
                        <span style={{display:"inline-flex",alignItems:"center"}}>{value}{record.privateType == 0 ?<CiLock style={{marginLeft:'5px'}}/>:null}</span>
                    )
            }
        },
        {
            title: t['componentList.label.display'],
            dataIndex: 'configuration',
            headerCellStyle: {width: '500px'},
            render: (value, record) => {
                return <TreeSelect
                    placeholder={"Please Select"}
                    multiple={true}
                    treeCheckable={true}
                    allowClear={true}
                    treeData={translateResponse(value)}
                />;
            }
        },
        {
            title: t['componentList.label.admins'],
            dataIndex: 'user',
            render: (value) => {
                return (<UserGroup users={[value]}/>);
            },
        },
        {
            title: t['componentList.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width: '250px'},
            render: (value, record) => {
                let updateButton;
                let deleteButton;
                if(record.permissions.includes('ManageAble')){
                    updateButton = <Button key={getRandomString()}
                                           onClick={() => callback(record, 'update')}
                                           type="text"
                                           size="mini">
                        {t['componentList.columns.button.update']}
                    </Button>;
                    deleteButton =
                        <Popconfirm key={getRandomString()}
                                    focusLock
                                    position={"tr"}
                                    title='Confirm'
                                    content={t['componentList.form.delete.confirm']}
                                    onOk={() => callback(record, 'delete')}
                        >
                            <Button
                                type="text"
                                size="mini">
                                {t['componentList.columns.button.delete']}
                            </Button>
                        </Popconfirm>
                }
                return  <Space size={0} direction="horizontal">{[updateButton,deleteButton]}</Space>;
            }
        }
    ];
}