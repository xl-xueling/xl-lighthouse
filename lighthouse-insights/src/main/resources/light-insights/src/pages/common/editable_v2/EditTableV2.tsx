import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {Form, Input, Table} from "@arco-design/web-react";
import EditTable, {EditTableComponentEnum} from "@/pages/common/edittable/EditTable";
import {FormInstance} from "@arco-design/web-react/lib";
import {getRandomString} from "@/utils/util";
const EditableContext = React.createContext<{ getForm?: () => FormInstance }>({});
const FormItem = Form.Item;
import styles from './style/index.module.less';

const EditTableV2 = React.forwardRef( (props:{columns,initData}, ref) => {
    const columns = props.columns;
    const initData = props.initData;

    const [targetColumns,setTargetColumns] = useState([]);
    const [targetData,setTargetData] = useState([]);

    function handleSave(row) {
        const newData = [...targetData];
        const index = newData.findIndex((item) => row.key === item.key);
        newData.splice(index, 1, { ...newData[index], ...row });
        setTargetData(newData);
    }

    const removeRow = (key) => {
        setTargetData(targetData.filter((item) => item.key !== key));
    }

    const getData = () => {
        return targetData?.map((z) => {
            const obj = {...z};
            Object.entries(z).forEach(([key, value]) => {
                if (value === null || value === undefined || value == '--') {
                    obj[key] = '';
                }
            });
            return obj;
        });
    }

    const addRow = (row) => {
        setTargetData(targetData.concat(row));
    }

    React.useImperativeHandle(ref,() => ({
        removeRow,
        addRow,
        getData
    }));

    useEffect(() => {
        setTargetColumns(columns);
    },[])

    useEffect(() => {
        setTargetData(initData);
    },[initData])

    return (
        <div className={styles["edit_panel"]}>
            <Table size={"mini"}
                   className={"edit_table_panel_v2"}
                   style={{minHeight:'200px'}}
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
                autoComplete={'off'}
                wrapperProps={rest}
                className={`${className} editable-row`}>
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

    const cellValueChangeHandler = (event) => {
        const value = event.target.value;
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
                    Object.entries(values).forEach(([key, value]) => {
                        if (value === null || value === undefined || value == '') {
                            values[key] = '--';
                        }
                    });
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
                    initialValue={rowData[column.dataIndex] == '--'?'':rowData[column.dataIndex]}
                    field={column.dataIndex}>
                    <Input size={"mini"} ref={refInput} onBlur={cellValueChangeHandler}/>
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

export default EditTableV2;