import {
    Typography,
    Grid,
    Message, Form, Input, Select, Spin, Modal, Space, Button, Notification
} from '@arco-design/web-react';
import {IconMinusCircleFill, IconPenFill} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
const { Title } = Typography;
import locale from './locale';
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/common/edittable/EditTable";
import {formatString, getRandomString, getTextBlenLength} from "@/utils/util";
import {Group} from "@/types/insights-web";
import {requestCreate, requestUpdate} from "@/api/group";
import {_ColumnNamePattern, _TokenPattern} from "@/utils/constants";
const { Row, Col } = Grid;

export default function GroupUpdatePanel({groupInfo,onClose,callback}) {

    const t = useLocale(locale);
    const editTableRef= useRef(null);
    const tempalteEditTableRef= useRef(null);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [loading,setLoading] = useState<boolean>(true);
    const [formInstance] = Form.useForm();
    const [initData,setInitData] = useState(null);
    const formRef = useRef(null);
    const [expandedKeys, setExpandedKeys] = useState([]);

    const columnsProps: EditTableColumnProps[]  = [
        {
            title: 'Name',
            dataIndex: 'name',
            componentType:EditTableComponentEnum.INPUT,
            headerCellStyle: { width:'20%'},
        },
        {
            title: 'Type',
            dataIndex: 'type',
            initValue:"string",
            componentType:EditTableComponentEnum.SELECT,
            headerCellStyle: { width:'130px'},
            render:(value, record) => {
                const isLock = record.lockColumns && record.lockColumns.includes('type');
                return (
                    <Select size={"mini"}
                            disabled={isLock}
                            popupVisible={expandedKeys.includes(record.key)}
                            value={value}
                            onChange={(value) => {record['type'] = value}}
                            onFocus={(e) => {
                                setExpandedKeys((keys) => [...keys, record.key]);
                            }}
                            onKeyDown={(event) => {
                                if(event.key == 'Enter'){
                                    setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                                }
                            }}
                            onBlur={() => {
                                setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                            }}
                            defaultValue={"string"}>
                        <Select.Option key={"string"}  value={"string"} onClick={() => {
                            setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                        }}>
                            String
                        </Select.Option>
                        <Select.Option key={"number"}  value={"number"} onClick={() => {
                            setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                        }}>
                            Number
                        </Select.Option>
                    </Select>)
            }
        },
        {
            title: 'Comment',
            dataIndex: 'comment',
            componentType:EditTableComponentEnum.INPUT,
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            componentType:EditTableComponentEnum.BUTTON,
            headerCellStyle: { width:'12%'},
            render: (_, record) => {
                const isLock = record.lockColumns && record.lockColumns.includes('operation');
                if(isLock){
                    return <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%'}}>
                        <IconMinusCircleFill style={{ cursor:"pointer",color:"gray"}}/>
                    </Space>
                }else{
                    return <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%'}}>
                        <IconMinusCircleFill style={{ cursor:"pointer"}} onClick={() => editTableRef.current.removeRow(record.key)}/>
                    </Space>
                }
            }
            ,
        },
    ];

    const onOk = async() => {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const columns = editTableRef.current.getData();
        if(!columns || columns.length == 0){
            Notification.warning({style: { width: 420 }, title: 'Warning', content: t['groupUpdate.form.validate.column.notEmpty.errorMsg']});
            return;
        }
        const columnNameArray:string[] = [];
        for(let i=0;i<columns.length;i++){
            const name = columns[i].name;
            const comment = columns[i].comment;
            if(!name){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: t['groupUpdate.form.validate.columnName.notEmpty']});
                return;
            }
            if(name.length < 3 || name.length > 15){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupUpdate.form.validate.columnName.length.failed'],name)});
                return;
            }
            if(!_ColumnNamePattern.test(name)){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupUpdate.form.validate.columnName.failed'],name)});
                return;
            }
            if(comment && getTextBlenLength(comment) > 50){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupUpdate.form.validate.columnComment.length.failed'],name)});
                return;
            }
            if(comment && getTextBlenLength(comment) < 3){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupUpdate.form.validate.columnComment.length.failed'],name)});
                return;
            }
            if(columnNameArray.includes(name)){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupUpdate.form.validate.columnName.duplicate.failed'],name)});
                return;
            }else{
                columnNameArray.push(name);
            }
            delete columns[i].key;
        }
        const group:Group = {
            id:groupInfo.id,
            projectId:groupInfo.projectId,
            token:values.token,
            desc:values.desc,
            columns:columns,
        }
        setConfirmLoading(true);
        console.log("groupInfo:" + JSON.stringify(group));
        requestUpdate(group).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['groupUpdate.form.submit.success']});
                callback('update-group',group);
                onClose();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setConfirmLoading(false);
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        })
    }

    useEffect(() => {
        const columnArr: Array<EditTableColumn> = [];
        for (let i = 0; i < groupInfo?.columns.length; i++) {
            const columnInfo = groupInfo?.columns[i];
            columnArr.push({...columnInfo, "key": getRandomString(),lockColumns:['name','type','operation']})
        }
        setInitData(columnArr);
        const formData = {
            token:groupInfo?.token,
            desc:groupInfo?.desc,
        }
        formInstance.setFieldsValue(formData);
    },[groupInfo])

    return (
        <Modal
            title={t['groupUpdate.modal.title']}
            onOk={onOk}
            visible={true}
            style={{ width:'750px' }}
            confirmLoading={confirmLoading}
            onCancel={onClose}>
            <Form
                form={formInstance}
                ref={formRef}
                autoComplete={"off"}
                layout={"vertical"}>
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Token'}
                </Typography.Title>
                <Form.Item field="token"
                           rules={[
                               { required: true, message: t['groupUpdate.form.validate.token.notEmpty.errorMsg'], validateTrigger : ['onSubmit'] },
                               { required: true, match: new RegExp(_TokenPattern,"g"),message: t['groupUpdate.form.validate.token.failed'] , validateTrigger : ['onSubmit']},
                           ]}>
                    <Input
                        allowClear
                        placeholder={'Please Input Token'} disabled={true}/>
                </Form.Item>
                <Form.Item field="columns">
                    <Grid.Row>
                        <Grid.Col span={16}>
                            <Typography.Title
                                style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}>
                                {t['groupUpdate.form.label.columns']}
                            </Typography.Title>
                        </Grid.Col>
                        <Grid.Col span={8} style={{ textAlign: 'right' }}>
                            <Button type={"secondary"} size={"mini"} onClick={() => editTableRef.current.addRow()}>{t['groupUpdate.form.button.columnAdd']}</Button>
                        </Grid.Col>
                    </Grid.Row>
                    <EditTable ref={editTableRef} columnsProps={columnsProps} columnsData={initData}/>
                </Form.Item>
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {t['groupUpdate.form.label.description']}
                </Typography.Title>
                <Form.Item field="desc" rules={[
                    { required: true, message: t['groupUpdate.form.validate.desc.notEmpty.errorMsg'], validateTrigger : ['onSubmit'] },
                ]}>
                    <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}
