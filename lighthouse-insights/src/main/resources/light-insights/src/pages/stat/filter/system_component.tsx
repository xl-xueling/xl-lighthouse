import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {getRandomString} from "@/utils/util";
import {ComponentTypeEnum, RenderFilterConfig} from "@/types/insights-common";
import {Input, Table, TableColumnProps, TreeSelect} from "@arco-design/web-react";
import {translateToTreeNodes} from "@/pages/department/common";
import {IconPlus} from "@arco-design/web-react/icon";

export default function SystemComponents({onSelect}) {

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
            title:"默认下拉选择框",
            componentType:ComponentTypeEnum.FILTER_SELECT,
        },
        {
            title:"默认输入框",
            componentType:ComponentTypeEnum.FILTER_INPUT,
        },
    ]


    return (
        <Table rowKey={() => getRandomString()} size={"small"} columns={columns} data={systemComponents} pagination={false}/>
    );
}