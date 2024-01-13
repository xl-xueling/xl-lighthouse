import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {
    Button,
    Form,
    Tabs,
    Grid,
    Input, Message,
    Modal, Notification,
    Select, Space, TableColumnProps, TreeSelect,
    Typography, Table
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import EditTable, {EditTableColumnProps, EditTableComponentEnum} from "@/pages/common/edittable/EditTable";
import {IconMinusCircleFill, IconPlus} from "@arco-design/web-react/icon";
import {formatString, getRandomString, getTextBlenLength, stringifyObj} from "@/utils/util";
import {requestCreate} from "@/api/group";
import {Group, Project} from "@/types/insights-web";
import {GlobalErrorCodes} from "@/utils/constants";
import {Component, ComponentTypeEnum, RenderFilterConfig} from "@/types/insights-common";
import {translateToTreeNodes} from "@/pages/department/common";
import {requestList} from "@/api/component";
import SystemComponents from "@/pages/stat/filter/system_component";


export default function StatFilterConfigModal({onClose}) {

    const editTableRef= useRef(null);
    const sourceColumns: TableColumnProps[] = [
        {
            title: 'Title',
            dataIndex: 'title',
            headerCellStyle: { width: '300px' },
        },
        {
            title: 'Display',
            dataIndex: 'renderType',
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
            headerCellStyle: { width: '300px' },
            cellStyle:{textAlign:"center"},
            render: (value, record) => {
                return (
                    <IconPlus onClick={() => selectComponent(record)}/>
                )
            }
        },
    ];


    const targetColumns : EditTableColumnProps[] = [
        {
            title: 'Label',
            dataIndex: 'label',
            editable:true,
            headerCellStyle: { width: '250px' },
            componentType:EditTableComponentEnum.INPUT,
        },
        {
            title: 'Dimens',
            dataIndex: 'dimens',
            editable:true,
            headerCellStyle: { width: '250px' },
            componentType:EditTableComponentEnum.INPUT,
        },
        {
            title: 'Display',
            dataIndex: 'componentType',
            headerCellStyle: { width: '400px' },
            editable:true,
            componentType:EditTableComponentEnum.TreeSelect,
            render: (value, record) => {
                console.log("----value is:" + value + ",record:" + JSON.stringify(record));
                if(value == ComponentTypeEnum.FILTER_INPUT){
                    return (
                        <Input size={"small"}/>
                    )
                }if(value == ComponentTypeEnum.FILTER_SELECT){
                    return (
                        <TreeSelect size={"mini"} treeData={translateToTreeNodes(record.config)} />
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
            headerCellStyle: { width: '150px' },
            editable:true,
            componentType:EditTableComponentEnum.INPUT,
            cellStyle:{textAlign:"center"},
            render: (value, record) => {
                // return (
                //     // <IconMinus onClick={() => removeComponent(record.key)}/>
                // )
            }
        },
    ];

    const selectComponent = (component:RenderFilterConfig) => {
        component = {...component,label:'--',dimens:'--'}
        console.log("component is:" + JSON.stringify(component));
        setTargetData([...targetData,component])
    }

    // const removeComponent = (key) => {
    //     setTargetData(targetData.filter(x => x.key != key))
    // }

    const [sourceData,setSourceData] = useState<Array<Component>>([]);

    const [targetData,setTargetData] = useState<Array<RenderFilterConfig>>([]);

    const fetchComponentsInfo:Promise<{list:Array<Component>,total:number}> = new Promise<{list:Array<Component>,total:number}>((resolve) => {
        const proc = async () => {
            const result = await requestList({
                params: {
                    // page: current,
                    // pageSize,
                    // owner:owner?1:0,
                    // ...formParams,
                },
            });
            resolve(result.data);
        }
        proc().then();
    })

    const defaultComponents = [
        {
            key: getRandomString(),
            id:1,
            isBuiltIn:true,
            title:"内置输入框",
            renderType:ComponentTypeEnum.FILTER_INPUT,
        },
        {
            key: getRandomString(),
            id:2,
            isBuiltIn:true,
            title:"内置选择框",
            renderType:ComponentTypeEnum.FILTER_SELECT,
        },
    ]

    useEffect(() => {
        setSourceData(defaultComponents);
    },[])

    const changeComponentType = (componentType) => {
        if(componentType == 0){
            setSourceData(defaultComponents);
        }else{
            Promise.all([fetchComponentsInfo]).then((result) => {
                const data = result[0];
                const newlist = data.list.map(z =>  {
                    return {"key":getRandomString(),...z}
                })
                console.log("result is:" + JSON.stringify(newlist));
                setSourceData(newlist);
            });
        }
    }

    function handleSave(row) {
        // const newData = [...targetData];
        // const index = newData.findIndex((item) => row.key === item.key);
        // newData.splice(index, 1, { ...newData[index], ...row });
        // setTargetData(newData);
        console.log("-")
    }

    return (
        <Modal
            title= '修改筛选项'
            style={{ width:'1000px',top:'20px',maxWidth:'70%' }}
            visible={true}
            onCancel={() => onClose()}>
            <Space size={10} direction="vertical" style={{width:'100%'}}>
                <Tabs type={"card-gutter"} defaultActiveTab='1'>
                    <Tabs.TabPane key='1' title='内置组件' style={{padding:'5px'}}>
                        <SystemComponents onSelect={selectComponent}/>
                    </Tabs.TabPane>
                    <Tabs.TabPane key='2' title='自定义组件'>
                        <Input.Search  placeholder={'Search'} allowClear />
                        <Table size={"small"} columns={sourceColumns} data={sourceData} />
                    </Tabs.TabPane>
                </Tabs>
                <Typography.Title style={{fontSize:'14px',marginTop:'20px'}}>
                    {'当前配置：'}
                </Typography.Title>
                <EditTable ref={editTableRef} columnsProps={targetColumns} columnsData={targetData}/>
            </Space>
        </Modal>
    );
}

