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
import {IconEdit, IconStar, IconStarFill} from "@arco-design/web-react/icon";
import {PiLinkSimple} from "react-icons/pi";
import {getStatExpiredEnumDescription, StatExpiredEnum, StatStateEnum} from "@/types/insights-common";
import {getRandomString} from "@/utils/util";
import {getStatStateDescriptionWithBadge} from "@/pages/common/desc/base";
import {Link} from "@arco-design/web-react/lib";
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
                return getStatStateDescriptionWithBadge(t,value)
            },
        },
        {
            title: 'Operation',
            dataIndex: 'operate',
            headerCellStyle: {width:'200px' },
            render: (_, record) => {
                let viewButton;
                let updateButton;
                let stopButton;
                let startButton;
                let deleteButton;
                let activeButton;
                if(record.permissions.includes('ManageAble')){
                    viewButton = <Link key={getRandomString()} target={"_blank"} href={'/stat/display/' + record.id}>
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
                return <Space size={16} direction="horizontal">{[viewButton,updateButton,stopButton,startButton,deleteButton]}</Space>
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
                return getStatStateDescriptionWithBadge(t,value)
            },
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            headerCellStyle: {width:'200px' },
            render: (_, record) => {
                let viewButton;
                let applyButton;
                if(record.permissions.includes('ManageAble') || record.permissions.includes('AccessAble')){
                    viewButton = <Link key={getRandomString()} target={"_blank"} href={'/stat/display/' + record.id}>
                        <Button
                            type="text"
                            size="mini">
                            {t['statList.table.operations.view']}
                        </Button>
                    </Link>
                }else{
                    applyButton = <Popconfirm key={getRandomString()}
                                               position={"tr"}
                                               focusLock
                                               onOk={() => callback(record, 'apply')}
                                               title='Confirm'
                                               content={t['statList.table.operations.apply.confirm']}
                    >
                        <Button
                            type="text"
                            size="mini">
                            {t['statList.table.operations.apply']}
                        </Button>
                    </Popconfirm>;
                }
                return <Space size={16} direction="horizontal">{[viewButton,applyButton]}</Space>
            }
        },
    ];
}



export function getBindColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
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
            title: 'Project',
            dataIndex: 'project.title',
            render:(value,record) => {
                return value;
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
                return getStatStateDescriptionWithBadge(t,value)
            },
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            headerCellStyle: {width:'200px' },
            render: (_, record) => {
                const bindButton = <Popconfirm key={getRandomString()}
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
                return <Space size={16} direction="horizontal">{[bindButton]}</Space>
            }
        },
    ];
}