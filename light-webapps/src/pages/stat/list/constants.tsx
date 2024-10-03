import {
    Badge,
    Button,
    Popconfirm,
    Popover,
    Space,
    TableColumnProps,
    Tabs,
    Tooltip,
    Typography
} from "@arco-design/web-react";
import React from "react";
import {StatStateEnum} from "@/types/insights-common";
import {getRandomString} from "@/utils/util";
import {getStatExpiredEnumDescription, getStatStateDescriptionWithBadge} from "@/pages/common/desc/base";
import {Link} from "@arco-design/web-react/lib";
const TabPane = Tabs.TabPane;
const { Text } = Typography;

export function getColumnsOfManage(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: t['statList.label.id'],
            dataIndex: 'id',
        },
        {
            title: t['statList.label.title'],
            dataIndex: 'title',
            render:(value,record) => {
                return (<Text>{value}</Text>)
            }
        },
        // {
        //     title: t['statList.label.group'],
        //     dataIndex: 'group',
        //     render:(value,record) => {
        //         return (record.token);
        //     }
        // },
        {
            title: t['statList.label.timeparam'],
            dataIndex: 'timeparam',
        },
        {
            title: t['statList.label.expired'],
            dataIndex: 'expired',
            render:(value,record) => {
                return getStatExpiredEnumDescription(value);
            }
        },
        {
            title: t['statList.label.state'],
            dataIndex: 'state',
            render: (value) => {
                return getStatStateDescriptionWithBadge(t,value)
            },
        },
        {
            title: t['statList.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'300px' },
            render: (_, record) => {
                let viewButton;
                let updateButton;
                let stopButton;
                let startButton;
                let deleteButton;
                let activeButton;
                if(record.permissions.includes('ManageAble')){
                    viewButton = <Link key={getRandomString()} target={"_blank"} href={'/stat/preview/' + record.id}>
                        <Button
                            type="text"
                            size="mini">
                            {t['statList.table.operations.view']}
                        </Button>
                    </Link>
                    updateButton = <Button key={getRandomString()}
                            onClick={() => callback(record, 'showUpdateModal')}
                            type="text"
                            size="mini">
                        {t['statList.table.operations.update']}
                    </Button>;
                    if(record.state == StatStateEnum.RUNNING){
                        stopButton =  <Popconfirm key={getRandomString()}
                                                  position={"tr"}
                                                  focusLock
                                                  onOk={() => callback(record, 'stop')}
                                                  title='Confirm'
                                                  content={t['statList.table.operations.stop.confirm']}
                        >
                            <Button
                                type="text"
                                size="mini">
                                {t['statList.table.operations.stop']}
                            </Button>
                        </Popconfirm>;
                    }else if(record.state == StatStateEnum.STOPPED){
                        startButton =  <Popconfirm key={getRandomString()}
                                                  position={"tr"}
                                                  focusLock
                                                  onOk={() => callback(record, 'restart')}
                                                  title='Confirm'
                                                  content={t['statList.table.operations.restart.confirm']}
                        >
                            <Button
                                type="text"
                                size="mini">
                                {t['statList.table.operations.restart']}
                            </Button>
                        </Popconfirm>;
                        deleteButton = <Popconfirm key={getRandomString()}
                                                   position={"tr"}
                                                   focusLock
                                                   onOk={() => callback(record, 'delete')}
                                                   title='Confirm'
                                                   content={t['statList.table.operations.delete.confirm']}
                        >
                            <Button
                                type="text"
                                size="mini">
                                {t['statList.table.operations.delete']}
                            </Button>
                        </Popconfirm>;
                    }
                }
                return <Space size={2} direction="horizontal">{[viewButton,updateButton,stopButton,startButton,deleteButton]}</Space>
            }
        },
    ];
}


export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: t['statList.label.id'],
            dataIndex: 'id',
        },
        {
            title: t['statList.label.title'],
            dataIndex: 'title',
            render:(value,record) => {
                return (<Text>{value}</Text>)
            }
        },
        {
            title: t['statList.label.relationship'],
            dataIndex: 'relationship',
            render:(value,record) => {
                return (record.projectTitle + ' > ' + record.token);
            }
        },
        {
            title: t['statList.label.timeparam'],
            dataIndex: 'timeparam',
        },
        {
            title: t['statList.label.expired'],
            dataIndex: 'expired',
            render:(value,record) => {
                return getStatExpiredEnumDescription(value);
            }
        },
        {
            title: t['statList.label.state'],
            dataIndex: 'state',
            render: (value) => {
                return getStatStateDescriptionWithBadge(t,value)
            },
        },
        {
            title: t['statList.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'180px' },
            render: (_, record) => {
                let viewButton;
                let applyButton;
                if(record.permissions.includes('ManageAble') || record.permissions.includes('AccessAble')){
                    viewButton = <Link key={getRandomString()} href={'/stat/preview/'+record.id} onClick={(e) => {e.preventDefault();callback(record, 'preview')}} style={{ textDecoration: 'none' }}>
                        <Button
                                type="text"
                                size="mini">
                            {t['statList.table.operations.view']}
                        </Button>
                    </Link>

                }else{
                    applyButton = <Button key={getRandomString()}
                        type="text"
                        size="mini" onClick={() => callback(record, 'apply')}>
                        {t['statList.table.operations.apply']}
                    </Button>;
                }
                return <Space size={16} direction="horizontal">{[viewButton,applyButton]}</Space>
            }
        },
    ];
}



export function getBindColumns(t: any,bindList:Array<number>,callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: t['statList.label.id'],
            dataIndex: 'id',
        },
        {
            title: t['statList.label.title'],
            dataIndex: 'title',
            render:(value,record) => {
                return (<Text>{value}</Text>)
            }
        },
        {
            title: t['statList.label.relationship'],
            dataIndex: 'relationship',
            render:(value,record) => {
                return (record.projectTitle + ' > ' + record.token);
            }
        },
        {
            title: t['statList.label.timeparam'],
            dataIndex: 'timeparam',
        },
        {
            title: t['statList.label.state'],
            dataIndex: 'state',
            render: (value) => {
                return getStatStateDescriptionWithBadge(t,value)
            },
        },
        {
            title: t['statList.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'130px' },
            render: (value, record) => {
                let bindButton = null;
                if(bindList.includes(record.id)){
                    bindButton =
                        <Button key={getRandomString()} disabled={true}
                            type="secondary"
                            size="mini">
                            {t['statList.table.operations.binded']}
                        </Button>;
                }else{
                    bindButton = <Popconfirm key={getRandomString()}
                                                   position={"tr"}
                                                   focusLock
                                                   onOk={() => callback(record, 'bind')}
                                                   title='Confirm'
                                                   content={t['statList.table.operations.bind.confirm']}
                    >
                        <Button
                            type="text"
                            size="mini">
                            {t['statList.table.operations.bind']}
                        </Button>
                    </Popconfirm>;
                }
                return <Space size={16} direction="horizontal">{[bindButton]}</Space>
            }
        },
    ];
}