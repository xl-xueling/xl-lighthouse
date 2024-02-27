import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {getRandomString} from "@/utils/util";
import {ComponentTypeEnum, RenderFilterConfig} from "@/types/insights-common";
import {Input, Space, Table, TableColumnProps, TreeSelect} from "@arco-design/web-react";
import {translateToTreeNodes} from "@/pages/department/common";
import {IconPlus} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/stat/filter/locale";

export default function SystemComponents({onSelect}) {

    const t = useLocale(locale);

    const columns: TableColumnProps[] = [
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'Display',
            dataIndex: 'componentType',
            headerCellStyle: { width: '450px' },
            render: (value, record) => {
                if(value == ComponentTypeEnum.FILTER_INPUT){
                    return (
                        <Input size={"small"}/>
                    )
                }else{
                    return (
                        <TreeSelect size={"mini"} treeData={translateToTreeNodes(record.config)} />
                    )
                }
            },
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            cellStyle:{textAlign:"center"},
            render: (value, record) => {
                return (
                    <IconPlus style={{cursor:'pointer'}} onClick={() => onSelect(record)}/>
                )
            }
        },
    ];

    const systemComponents:RenderFilterConfig[] = [
        {
            title:t['basic.componentsType.system.filterSelect'],
            componentType:ComponentTypeEnum.FILTER_SELECT,
            componentId:0,
        },
        {
            title:t['basic.componentsType.system.filterInput'],
            componentType:ComponentTypeEnum.FILTER_INPUT,
            componentId:0,
        },
    ]


    return (
        <Space size={16} direction="vertical" style={{ width: '100%',height:'270px' }}>
            <Table rowKey={() => getRandomString()} size={"mini"} columns={columns} data={systemComponents} pagination={false}/>
        </Space>
    );
}