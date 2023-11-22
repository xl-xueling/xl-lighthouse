import React, { useState, useRef, useEffect, useContext, useCallback } from 'react';
import {Button, Table, Input, Select, Form, FormInstance, Space, TableColumnProps} from '@arco-design/web-react';
import {IconMinusCircleFill} from "@arco-design/web-react/icon";
const FormItem = Form.Item;
const EditableContext = React.createContext<{ getForm?: () => FormInstance }>({});
import styles from './style/index.module.less';
import {Column} from "@/types/insights-web";
import {stringifyObj} from "@/utils/util";


export interface EditTableColumn extends Column{
    key:number;
}

export interface EditTableColumnProps extends TableColumnProps {
    isSelect:boolean;
}

const EditTable = React.forwardRef((props:{columns,initData}, ref) => {

    const columns = props.columns
    const initData = props.initData;
    const [count, setCount] = useState(5);
    const [data,setData] = useState(initData);

    function handleSave(row) {
        const newData = [...data];
        const index = newData.findIndex((item) => row.key === item.key);
        newData.splice(index, 1, { ...newData[index], ...row });
        setData(newData);
    }

    const removeRow = (key) => {
        setData(data.filter((item) => item.key !== key));
    }

    React.useImperativeHandle(ref,() => ({
        removeRow
    }));

    useEffect(() => {
        setData(initData);
    },[initData])

    function addRow() {
        setCount(count + 1);
        setData(
            data.concat({
                key: `${count + 1}`,
                name: 'Tom',
                type: 10000,
                desc: '33 Park Road, London',
            })
        );
    }

    return (
        <div className={styles.edit_panel}>
            <Button
                type='dashed'
                size={"mini"}
                onClick={addRow}
            >
                添加
            </Button>
            <Table
                size={"mini"}
                data={data}
                components={{
                    body: {
                        row: EditableRow,
                        cell: EditableCell,
                    },
                }}
                columns={columns.map((column) =>
                    column.editable
                        ? {
                            ...column,
                            onCell: () => ({
                                onHandleSave: handleSave,
                            }),
                        }
                        : column
                )}
            />
        </div>
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
                !ref.current.contains(e.target) &&
                !e.target.classList.contains('js-demo-select-option')
            ) {
                cellValueChangeHandler(rowData[column.dataIndex]);
            }
        },
        [editing, rowData, column]
    );
    useEffect(() => {
        editing && refInput.current && refInput.current.focus();
    }, [editing]);
    useEffect(() => {
        document.addEventListener('click', handleClick, true);
        return () => {
            document.removeEventListener('click', handleClick, true);
        };
    }, [handleClick]);

    const cellValueChangeHandler = (value) => {
        if (column.isSelect) {
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

    if (editing) {
        return (
            <div ref={ref}>
                {column.isSelect ? (

                    <Select
                        size={"mini"}
                        onChange={cellValueChangeHandler}
                        defaultValue={rowData[column.dataIndex]}
                        options={[2000, 5000, 10000, 20000]}
                    >
                    </Select>
                ) : (
                    <FormItem
                        style={{ marginBottom: 0 }}
                        labelCol={{ span: 0 }}
                        wrapperCol={{ span: 24 }}
                        initialValue={rowData[column.dataIndex]}
                        field={column.dataIndex}
                        rules={[{ required: true }]}
                    >
                        <Input size={"mini"} ref={refInput} onPressEnter={cellValueChangeHandler} />
                    </FormItem>
                )}
            </div>
        );
    }

    return (
        <div
            className={column.editable ? `editable-cell ${className}` : className}
            onClick={() => column.editable && setEditing(!editing)}
        >
            {children}
        </div>
    );
}

export default EditTable;