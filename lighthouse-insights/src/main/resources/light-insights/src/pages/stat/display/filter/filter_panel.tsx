import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department, Project} from "@/types/insights-web";
import { Line } from '@ant-design/plots';
import { Chart, Line as Line2, Point, Tooltip,getTheme } from "bizcharts";
import { LineAdvance} from 'bizcharts';
const EditableContext = React.createContext<{ getForm?: () => FormInstance }>({});
import {IconEdit, IconList, IconMinus, IconPlus, IconPublic, IconPushpin} from "@arco-design/web-react/icon";
import {
    Button,
    Divider,
    Form, FormInstance,
    Input,
    Modal,
    Radio,
    Space,
    Table,
    TableColumnProps, TreeSelect,
    Typography
} from "@arco-design/web-react";
const FormItem = Form.Item;
import styles from "./style/index.module.less";
import {Component, ComponentTypeEnum} from "@/types/insights-common";
import {requestList} from "@/api/component";
import {translateToTreeNodes} from "@/pages/department/common";
import {getRandomString} from "@/utils/util";
import EditTable, {EditTableColumnProps, EditTableComponentEnum} from "@/pages/common/edittable/EditTable";
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
                if(value == ComponentTypeEnum.FILTER_INPUT){
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


    const targetColumns : EditTableColumnProps[] = [
        {
            title: 'Title',
            dataIndex: 'title',
            editable:true,
            componentType:EditTableComponentEnum.INPUT,
        },
        {
            title: 'Dimens',
            dataIndex: 'dimens',
            editable:true,
            componentType:EditTableComponentEnum.INPUT,
        },
        {
            title: 'Display',
            dataIndex: 'renderType',
            editable:true,
            componentType:EditTableComponentEnum.INPUT,
            render: (value, record) => {
                if(value == ComponentTypeEnum.FILTER_INPUT){
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

    const selectComponent = (component:Component) => {
        const copyComponent = {...component,"key":getRandomString()}
        setTargetData([...targetData,copyComponent])
    }

    // const removeComponent = (key) => {
    //     setTargetData(targetData.filter(x => x.key != key))
    // }

    const [sourceData,setSourceData] = useState<Array<Component>>([]);

    const [targetData,setTargetData] = useState<Array<Component>>([]);

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
                    <Table size={"small"}
                           data={targetData}
                           components={{
                               body: {
                                   row: EditableRow,
                                   cell: EditableCell,
                               },
                           }}
                           columns={targetColumns.map((column) => {
                                   return column.editable
                                       ? {
                                           ...column,
                                           onCell: () => ({
                                               onHandleSave: handleSave,
                                           }),
                                       }
                                       : column
                               }

                           )}
                    />
                </Space>
            </div>
        </Modal>
    );
}

function EditableRow(props) {
    const { children, record, className, ...rest } = props;
    const refForm = useRef(null);

    const getForm = () => refForm.current;

    return (
        <EditableContext.Provider
            value={{
                getForm,
            }}
        >
            <Form
                style={{ display: 'table-row' }}
                ref={refForm}
                wrapper='tr'
                wrapperProps={rest}
                className={`${className} editable-row`}
            >
                {props.children}
            </Form>
        </EditableContext.Provider>
    );
}

function EditableCell(props) {
    const { children, className, rowData, column, onHandleSave } = props;
    const ref = useRef(null);
    const refInput = useRef(null);
    const { getForm } = useContext(EditableContext);
    const [editing, setEditing] = useState(false);
    const handleClick = useCallback(
        (e) => {
            if (
                editing &&
                column.editable &&
                ref.current &&
                !ref.current.contains(e.target)
            ) {
                cellValueChangeHandler(rowData[column.dataIndex]);
            }
        },
        [editing, rowData, column]
    );
    useEffect(() => {
        if(column.componentType == EditTableComponentEnum.INPUT){
            editing && refInput.current.focus();
        }
    }, [editing]);

    useEffect(() => {
        document.addEventListener('click', handleClick, true);
        return () => {
            document.removeEventListener('click', handleClick, true);
        };
    }, [handleClick]);

    const cellValueChangeHandler = (value) => {
        if (column.componentType == EditTableComponentEnum.SELECT) {
            const values = {
                [column.dataIndex]: value,
            };
            onHandleSave && onHandleSave({ ...rowData, ...values });
            setTimeout(() => setEditing(!editing), 300);
        } else {
            const form = getForm();
            form.validate([column.dataIndex], (errors, values) => {
                if (!errors || !errors[column.dataIndex]) {
                    setEditing(!editing);
                    onHandleSave && onHandleSave({ ...rowData, ...values });
                }
            });
        }
    };

    if (column.componentType == EditTableComponentEnum.INPUT && editing) {
        return (
            <div ref={ref}>
                <FormItem
                    style={{ marginBottom: 0 }}
                    labelCol={{ span: 0 }}
                    wrapperCol={{ span: 24 }}
                    initialValue={rowData[column.dataIndex]}
                    field={column.dataIndex}
                >
                    <Input size={"mini"} ref={refInput} onPressEnter={cellValueChangeHandler} />
                </FormItem>
            </div>
        );
    }
    return (
        <div
            className={column.editable ? `editable-cell ${className}` : className}
            onClick={() => {
                column.editable && setEditing(!editing)
            }}
        >
            {children}
        </div>
    );
}
