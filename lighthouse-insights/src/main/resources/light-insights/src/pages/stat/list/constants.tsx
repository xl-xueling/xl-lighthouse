import {Button, Popconfirm, Space, TableColumnProps, Tabs, Tooltip, Typography} from "@arco-design/web-react";
import React from "react";
import {IconStar, IconStarFill} from "@arco-design/web-react/icon";
const TabPane = Tabs.TabPane;
const { Text } = Typography;
export function getColumnsOfManage(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    return [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
            render:(_,record) => {
                return <Tooltip content={record.template} style={{ width:'500px' }}>
                    <Text>{_}</Text>
                </Tooltip>
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
        },
        {
            title: 'State',
            dataIndex: 'state',
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
                        content={t['statList.table.operations.stop.confirm']}
                    >
                        <Button
                            type="secondary"
                            size="mini">
                            {t['statList.table.operations.stop']}
                        </Button>
                    </Popconfirm>
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


export function getColumns(t: any,favoriteIds:Array<number>, callback: (record: Record<string, any>, type: string) => Promise<void>) {

    console.log("favorites Ids is:" + JSON.stringify(favoriteIds));

    return [
        {
            title: '',
            dataIndex: 'favorite',
            headerCellStyle: { width:'5px' },
            render: (_, record) => {
                if(favoriteIds?.includes(record.id)){
                    return <Space size={16} direction="horizontal">
                        <Popconfirm
                            focusLock
                            position={"bl"}
                            title='Confirm'
                            content= {t['statList.table.operations.unfavorite.confirm']}
                            onOk={() => callback(record, 'unFavorite')}
                        >
                            <IconStarFill style={{ cursor:"pointer"}}/>
                        </Popconfirm>
                    </Space>
                }else{
                    return <Space size={16} direction="horizontal">
                        <Popconfirm
                            focusLock
                            position={"bl"}
                            title='Confirm'
                            content= {t['statList.table.operations.favorite.confirm']}
                            onOk={() => callback(record, 'favorite')}
                        >
                            <IconStar style={{ cursor:"pointer" }}/>
                        </Popconfirm>
                    </Space>
                }
            }
        },
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
            render:(_,record) => {
                return <Tooltip content={record.template} style={{ width:'500px' }}>
                    <Text>{_}</Text>
                </Tooltip>
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
        },
        {
            title: 'State',
            dataIndex: 'state',
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
                        content={t['statList.table.operations.stop.confirm']}
                    >
                        <Button
                            type="secondary"
                            size="mini">
                            {t['statList.table.operations.stop']}
                        </Button>
                    </Popconfirm>
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