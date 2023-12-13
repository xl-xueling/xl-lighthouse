import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department, Project} from "@/types/insights-web";
import { Line } from '@ant-design/plots';
import { Chart, Line as Line2, Point, Tooltip,getTheme } from "bizcharts";
import { LineAdvance} from 'bizcharts';
import {IconEdit, IconList, IconPlus, IconPublic, IconPushpin} from "@arco-design/web-react/icon";
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
const RadioGroup = Radio.Group;


export default function FilterPanel({onClose = null}) {

    const columns: TableColumnProps[] = [
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
                        <TreeSelect size={"small"} />
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
                    <IconPlus/>
                )
            }
        },
    ];

    const [sourceData,setSourceData] = useState<Array<FilterComponent>>([]);


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
            key: '1',
            title:"内置输入框",
            renderType:RenderTypeEnum.FILTER_INPUT,
        },
        {
            key: '2',
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
            console.log("componentType is:" + JSON.stringify(componentType))
            Promise.all([fetchComponentsInfo]).then((result) => {
                console.log("result is:" + JSON.stringify(result));
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
                        <Table size={"small"} columns={columns} data={sourceData} />
                    </Space>

                </div>
                <Divider type='vertical' style={{height:'400px'}} />
                <Space size={10} direction="vertical">
                    <Typography.Title style={{fontSize:'14px'}}>
                        {'已选择：'}
                    </Typography.Title>
                    <Table size={"small"} columns={columns} data={sourceData} />
                </Space>
            </div>
        </Modal>
    );
}