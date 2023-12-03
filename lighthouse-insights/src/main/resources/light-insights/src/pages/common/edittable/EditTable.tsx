import React, { useState, useRef, useEffect, useContext, useCallback } from 'react';
import {Button, Table, Input, Select, Form, FormInstance, Space, TableColumnProps, Card} from '@arco-design/web-react';
import {IconMinusCircleFill} from "@arco-design/web-react/icon";
const FormItem = Form.Item;
const EditableContext = React.createContext<{ getForm?: () => FormInstance }>({});
import styles from './style/index.module.less';
import {Column} from "@/types/insights-web";
import {stringifyMap, stringifyObj} from "@/utils/util";


export enum EditTableComponentEnum {
    INPUT,
    SELECT,
    ACE_EDITOR,
    BUTTON
}

export interface EditTableColumn extends Column{
    key:number;
}

export interface EditTableColumnProps extends TableColumnProps {
    componentType:EditTableComponentEnum;
    options?:number[]|string[];
}


const EditTable = React.forwardRef( (props:{columnProps,columnsData,editmode},ref) => {

    const columnProps = props.columnProps;
    const columnsData = props.columnsData;
    const editmode = props.editmode || false;
    const tableRef = useRef(null);
    const [count, setCount] = useState(5);
    const [data,setData] = useState(columnsData);

    function handleSave(row) {
        const newData = [...data];
        const index = newData.findIndex((item) => row.key === item.key);
        newData.splice(index, 1, { ...newData[index], ...row });
        setData(newData);
    }

    const removeRow = (key) => {
        setData(data.filter((item) => item.key !== key));
    }

    const getData = () => {
        return data;
    }

    const addRow = () => {
        setCount(count + 1);
        setData(
            data.concat({
                key: `${count + 1}`,
            })
        );
    }

    React.useImperativeHandle(ref,() => ({
        removeRow,
        addRow,
        getData
    }));

    useEffect(() => {
        setData(columnsData);
    },[columnsData])


    return (
            <Table
                ref={tableRef}
                style={{minHeight: editmode ? '200px' : 'auto' }}
                size={"mini"}
                data={data}
                pagination={false}
                border={true}
                components={{
                    body: {
                        row: EditableRow,
                        cell: EditableCell,
                    },
                }}
                columns={columnProps.map((column) => {
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
    );
})

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

export default EditTable;