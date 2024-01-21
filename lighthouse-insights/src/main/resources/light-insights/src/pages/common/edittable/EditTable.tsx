import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {Form, FormInstance, Input, Table, TableColumnProps} from '@arco-design/web-react';
import styles from './style/index.module.less';
import {Column} from "@/types/insights-web";
import {getRandomString} from "@/utils/util";

const FormItem = Form.Item;
const EditableContext = React.createContext<{ getForm?: () => FormInstance }>({});


export enum EditTableComponentEnum {
    INPUT,
    SELECT,
    BUTTON,
    TreeSelect=3,
    Label=4,
}

export interface EditTableColumn extends Column{
    key:number|string;
    editable?:boolean;
}

export interface EditTableColumnProps extends TableColumnProps {
    componentType:EditTableComponentEnum;
    options?:number[]|string[];
    initValue?:string|number;
}


const EditTable = React.forwardRef( (props:{columnsProps,columnsData},ref) => {
    const columnsProps = props.columnsProps;
    const columnsData = props.columnsData;
    const tableRef = useRef(null);
    const [count, setCount] = useState(1);
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
        const initValues = columnsProps.filter(x => x.initValue).reduce((result,entity) => {
            result[entity.dataIndex] = entity.initValue;
            return result;
        },{})
        const newRow = {key: getRandomString() ,...initValues}
        setCount(count + 1);
        setData(data.concat(newRow));
    }

    React.useImperativeHandle(ref,() => ({
        removeRow,
        addRow,
        getData
    }));

    useEffect(() => {
        if(columnsData){
            setData(columnsData.map(z => {
                return {...z, key: getRandomString()};
            }));
        }
    },[columnsData])


    return (
        <div className={styles["edit_panel"]}>
            <Table
                className={"edit_table_panel"}
                ref={tableRef}
                style={{minHeight: '200px'}}
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
                columns={columnsProps.map((column) => {
                    return {
                        ...column,
                        onCell: () => ({
                            onHandleSave: handleSave,
                        }),
                    }
                }
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
                ref.current &&
                !ref.current.contains(e.target)
            ) {
                cellValueChangeHandler(rowData[column.dataIndex]);
            }
        },
        [editing, rowData, column]
    );
    useEffect(() => {
        const isLock = rowData.lockColumns && rowData.lockColumns.includes(column.dataIndex);
        if(column.componentType == EditTableComponentEnum.INPUT && !isLock){
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

    if (column.componentType == EditTableComponentEnum.INPUT) {
        const isLock = rowData.lockColumns && rowData.lockColumns.includes(column.dataIndex);
        return (
            <div ref={ref}>
                <FormItem
                    style={{ marginBottom: 0 }}
                    labelCol={{ span: 0 }}
                    wrapperCol={{ span: 24 }}
                    initialValue={rowData[column.dataIndex]}
                    field={column.dataIndex}
                >
                    <Input size={"mini"} ref={refInput} onChange={cellValueChangeHandler} disabled={isLock}/>
                </FormItem>
            </div>
        );
    }
    return (
        <div
            className={`editable-cell ${className}`}
            onClick={() => {
                (rowData.editable != undefined && rowData.editable == false) ? setEditing(false):setEditing(!editing)
            }}
        >
            {children}
        </div>
    );
}

export default EditTable;