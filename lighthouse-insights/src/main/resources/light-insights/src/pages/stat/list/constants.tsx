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
import {IconStar, IconStarFill} from "@arco-design/web-react/icon";
import {PiLinkSimple} from "react-icons/pi";
import {getStatExpiredEnumDescription, StatExpiredEnum, StatStateEnum} from "@/types/insights-common";
import {getRandomString} from "@/utils/util";
import {getStatStateDescription} from "@/pages/common/desc/base";
const TabPane = Tabs.TabPane;
const { Text } = Typography;



export function getColumnsOfManage(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: '',
            dataIndex: 'binded',
            headerCellStyle: { width:'20px' },
            render: (_, record) => {
                return <Button icon={<PiLinkSimple/>} size={"mini"} shape={"round"} onClick={() => {callback(record, 'binded')}}/>
            }
        },
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
            render:(value,record) => {
                return (<div onClick={() => callback(record, 'showDetailModal')} style={{ cursor: "pointer" }} ><Text>{value}</Text></div>)
            }
        },
        {
            title: 'Group',
            dataIndex: 'group.token',
        },
        {
            title: 'TimeParam',
            dataIndex: 'timeparam',
        },
        {
            title: 'Expired',
            dataIndex: 'expired',
            render:(value,record) => {
                return getStatExpiredEnumDescription(value);
            }
        },
        {
            title: 'State',
            dataIndex: 'state',
            render: (value) => {
                return getStatStateDescription(t,value)
            },
        },
        {
            title: 'Operate',
            dataIndex: 'operate',
            headerCellStyle: {width:'200px' },
            render: (_, record) => {
                let updateButton;
                let stopButton;
                let startButton;
                let deleteButton;
                let activeButton;
                if(record.permissions.includes('ManageAble')){
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
                return <Space size={16} direction="horizontal">{[updateButton,stopButton,startButton,deleteButton]}</Space>
            }
        },
    ];
}


export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: '',
            dataIndex: 'binded',
            headerCellStyle: { width:'20px' },
            render: (_, record) => {
                return <Button icon={<PiLinkSimple/>} size={"mini"} shape={"round"} onClick={() => {callback(record, 'binded')}}/>
            }
        },
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
            render:(value,record) => {
                return (<div onClick={() => callback(record, 'detail')} style={{ cursor: "pointer" }} ><Text>{value}</Text></div>)
            }
        },
        {
            title: 'Project',
            dataIndex: 'project.title',
        },
        {
            title: 'Department',
            dataIndex: 'department.name',
        },
        {
            title: 'Admins',
            dataIndex: 'adminIds',
            render:(value,record) => {
                return (
                    record.admins.map((user) => (
                        <Popover
                            key={user.id}
                            trigger='click'
                            content={
                                <span>
                                <div><b>Email：</b>{user.email}</div>
                                <div><b>Phone：</b>{user.phone}</div>
                              </span>
                            }>
                            <Text style={{ cursor:"pointer" }}>
                                {user.userName};
                            </Text>
                        </Popover>
                        ))
                )
            }
        },
        {
            title: 'State',
            dataIndex: 'state',
            render: (value) => {
                return getStatStateDescription(t,value)
            },
        },
        {
            title: 'Operate',
            dataIndex: 'operate',
            headerCellStyle: {width:'200px' },
            render: (_, record) => (
                <Space size={16} direction="horizontal">
                    <Button
                        type="secondary"
                        size="mini">
                        {t['statList.table.operations.view']}
                    </Button>
                    <Button
                        type="secondary"
                        size="mini">
                        {t['statList.table.operations.update']}
                    </Button>
                    <Popconfirm
                        position={"tr"}
                        focusLock
                        title='Confirm'
                        content={t['statList.table.operations.frozen.confirm']}
                    >
                        <Button
                            type="secondary"
                            size="mini">
                            {t['statList.table.operations.frozen']}
                        </Button>
                    </Popconfirm>
                    <Popconfirm
                        position={"tr"}
                        focusLock
                        title='Confirm'
                        content={t['statList.table.operations.delete.confirm']}
                    >
                        <Button
                            type="secondary"
                            size="mini">
                            {t['statList.table.operations.delete']}
                        </Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];
}