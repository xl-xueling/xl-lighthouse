import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {Form, Input, Table} from "@arco-design/web-react";
import EditTable, {EditTableComponentEnum} from "@/pages/common/edittable/EditTable";
import {FormInstance} from "@arco-design/web-react/lib";
import {getRandomString} from "@/utils/util";
const EditableContext = React.createContext<{ getForm?: () => FormInstance }>({});
const FormItem = Form.Item;

const EditTableV2 = React.forwardRef( (props:{columns,data},ref) => {
    const columns = props.columns;
    const data = props.data;

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
        return data;
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
        setTargetData(data);
    },[data])

    return (
        <Table size={"mini"} className={'editable-cell'}
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
                console.log("----Handler click..")
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
        console.log("-----cellValueChange,value is:" + value)
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
        console.log("show cell input...")
        return (
            <div ref={ref}>
                <FormItem
                    style={{ marginBottom: 0 }}
                    labelCol={{ span: 0 }}
                    wrapperCol={{ span: 24 }}
                    initialValue={rowData[column.dataIndex]}
                    field={column.dataIndex}>
                    <Input size={"mini"} ref={refInput} />
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