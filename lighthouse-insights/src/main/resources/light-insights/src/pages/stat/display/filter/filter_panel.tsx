import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department, Project} from "@/types/insights-web";
import { Line } from '@ant-design/plots';
import { Chart, Line as Line2, Point, Tooltip,getTheme } from "bizcharts";
import { LineAdvance} from 'bizcharts';
import {IconEdit, IconList, IconMinus, IconPlus, IconPublic, IconPushpin} from "@arco-design/web-react/icon";
import {
    Button,
    Divider,
    Form,
    Input,
    Modal,
    Radio,
    Space,
    Table,
    TableColumnProps, TreeSelect,
    Typography
} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import ProjectTree from "@/pages/project/common/project-tree";
import GroupManagePanel from "@/pages/group/manage";
import GroupAddPanel from "@/pages/group/add/group_add";
import {FilterComponent, RenderTypeEnum} from "@/types/insights-common";
import {requestList} from "@/api/component";
import {translateToTreeNodes} from "@/pages/department/common";
import {getRandomString} from "@/utils/util";
const RadioGroup = Radio.Group;


export default function FilterPanel({onClose = null}) {

    const sourceColumns: TableColumnProps[] = [
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'Display',
            dataIndex: 'renderType',
            render: (value, record) => {
                if(value == RenderTypeEnum.FILTER_INPUT){
                    return (
                        <Input size={"small"}/>
                    )
                }else{
                    return (
                        <TreeSelect size={"small"} treeData={translateToTreeNodes(record.config)} />
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
                    <IconPlus onClick={() => selectComponent(record)}/>
                )
            }
        },
    ];


    const targetColumns : TableColumnProps[] = [
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'Dimens',
            dataIndex: 'dimens',
        },
        {
            title: 'Display',
            dataIndex: 'renderType',
            render: (value, record) => {
                if(value == RenderTypeEnum.FILTER_INPUT){
                    return (
                        <Input size={"small"}/>
                    )
                }else{
                    return (
                        <TreeSelect size={"small"} treeData={translateToTreeNodes(record.config)} />
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
                    <IconMinus onClick={() => removeComponent(record.key)}/>
                )
            }
        },
    ];

    const selectComponent = (component:FilterComponent) => {
        const copyComponent = {...component,"key":getRandomString()}
        setTargetData([...targetData,copyComponent])
    }

    const removeComponent = (key) => {
        setTargetData(targetData.filter(x => x.key != key))
    }

    const [sourceData,setSourceData] = useState<Array<FilterComponent>>([]);

    const [targetData,setTargetData] = useState<Array<FilterComponent>>([]);

    const fetchComponentsInfo:Promise<{list:Array<FilterComponent>,total:number}> = new Promise<{list:Array<FilterComponent>,total:number}>((resolve) => {
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
            renderType:RenderTypeEnum.FILTER_INPUT,
        },
        {
            key: getRandomString(),
            id:2,
            isBuiltIn:true,
            title:"内置选择框",
            renderType:RenderTypeEnum.FILTER_SELECT,
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

    return (
        <Modal
            title= '修改筛选项'
            style={{ width:'1200px',top:'20px' }}
            visible={true}
            onCancel={() => onClose()}>
            <div className={styles.layout}>
                <div className={styles['layout-left-side']}>
                    <Space size={24} direction="vertical" className={styles.left}>
                        <Form initialValues={{componentType: 0}}>
                            <Form.Item field={"componentType"}>
                                <RadioGroup onChange={changeComponentType}>
                                    <Radio value={0}>内置筛选组件</Radio>
                                    <Radio value={1}>自定义筛选组件</Radio>
                                </RadioGroup>
                            </Form.Item>
                        </Form>
                        <Input.Search  placeholder={'Search'} allowClear />
                        <Table size={"small"} columns={sourceColumns} data={sourceData} />
                    </Space>

                </div>
                <Divider type='vertical' style={{height:'400px'}} />
                <Space size={10} direction="vertical" style={{width:'700px'}}>
                    <Typography.Title style={{fontSize:'14px'}}>
                        {'已选择：'}
                    </Typography.Title>
                    <Table size={"small"} columns={targetColumns} data={targetData} />
                </Space>
            </div>
        </Modal>
    );
}