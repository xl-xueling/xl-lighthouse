import {
  Button,
  Form,
  Grid,
  Input, Message,
  Modal, Notification,
  Select, Space,
  Typography
} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import EditTable, {EditTableColumnProps, EditTableComponentEnum} from "@/pages/common/edittable/EditTable";
import {IconMinusCircleFill, IconPlus} from "@arco-design/web-react/icon";
import {formatString, getTextBlenLength, stringifyObj} from "@/utils/util";
import {requestCreate} from "@/api/group";
import {Group, Project} from "@/types/insights-web";
import {GlobalErrorCodes} from "@/utils/constants";

export default function GroupCreateModal({projectId,callback,onClose}) {

  const [confirmLoading, setConfirmLoading] = useState(false);

  const [columnsData,setColumnsData] = useState([]);

  const editTableRef= useRef(null);
  const t = useLocale(locale);
  const FormItem = Form.Item;
  const columnNameRegex = /^[a-zA-Z]\w{2,14}$/;
  const formRef = useRef(null);

  const onOk = async() => {
    await formRef.current.validate();
    const values = formRef.current.getFieldsValue();
    const columns = editTableRef.current.getData();
    if(!columns || columns.length == 0){
      Notification.warning({style: { width: 420 }, title: 'Warning', content: t['groupCreate.form.validate.column.notEmpty.errorMsg']});
      return;
    }
    for(let i=0;i<columns.length;i++){
      const name = columns[i].name;
      const comment = columns[i].comment;
      if(!name){
        Notification.warning({style: { width: 420 }, title: 'Warning', content: t['groupCreate.form.validate.columnName.notEmpty']});
        return;
      }
      if(name.length < 3 || name.length > 15){
        Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupCreate.form.validate.columnName.length.failed'],name)});
        return;
      }
      if(!columnNameRegex.test(name)){
        Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupCreate.form.validate.columnName.failed'],name)});
        return;
      }
      if(comment && getTextBlenLength(comment) > 50){
        Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupCreate.form.validate.columnComment.length.failed'],name)});
        return;
      }
      if(comment && getTextBlenLength(comment) < 3){
        Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['groupCreate.form.validate.columnComment.length.failed'],name)});
        return;
      }
      delete columns[i].key;
    }
    const group:Group = {
      projectId:projectId,
      token:values.token,
      desc:values.desc,
      columns:columns,
    }
    setConfirmLoading(true);
    requestCreate(group).then((response) => {
      console.log("create group result is:" + JSON.stringify(response));
      const {code, data ,message} = response;
      if(code == '0'){
        Notification.info({style: { width: 420 }, title: 'Notification', content: t['groupCreate.form.submit.success']});
        group.id = data;
        callback('create-group',group);
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

  const [expandedKeys, setExpandedKeys] = useState([]);

  const columnsProps: EditTableColumnProps[]  = [
    {
      title: 'Name',
      dataIndex: 'name',
      editable: true,
      componentType:EditTableComponentEnum.INPUT,
      headerCellStyle: { width:'20%'},
    },
    {
      title: 'Type',
      dataIndex: 'type',
      editable: true,
      initValue:"number",
      componentType:EditTableComponentEnum.SELECT,
      headerCellStyle: { width:'130px'},
      render:(text, record) => {
        return (
            <Select size={"mini"}
                    popupVisible={expandedKeys.includes(record.key)}
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
                          defaultValue={"number"}>
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
      editable: true,
    },
    {
        title: 'Operation',
        dataIndex: 'operation',
        componentType:EditTableComponentEnum.BUTTON,
        headerCellStyle: { width:'12%'},
        render: (_, record) => (
            <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%'}}>
                <IconMinusCircleFill style={{ cursor:"pointer"}} onClick={() => editTableRef.current.removeRow(record.key)}/>
            </Space>
        ),
    },
  ];
  return (
      <Modal
          title={t['groupCreate.modal.title']}
          onOk={onOk}
          visible={true}
          style={{ width:'750px' }}
          confirmLoading={confirmLoading}
          onCancel={onClose}
      >
        <Form
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
                       { required: true, message: t['groupCreate.form.validate.token.notEmpty.errorMsg'], validateTrigger : ['onSubmit'] },
                       { required: true, match: new RegExp(/^[a-z0-9_]{5,20}$/,"g"),message: t['groupCreate.form.validate.token.failed'] , validateTrigger : ['onSubmit']},
                     ]}>
            <Input
                allowClear
                placeholder={'Please Input Token'} />
          </Form.Item>
          <Form.Item field="columns">
            <Grid.Row>
              <Grid.Col span={16}>
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}>
                  {'Columns'}
                </Typography.Title>
              </Grid.Col>
              <Grid.Col span={8} style={{ textAlign: 'right' }}>
                <Button type={"secondary"} size={"mini"} onClick={() => editTableRef.current.addRow()}>添加</Button>
              </Grid.Col>
            </Grid.Row>

            <EditTable ref={editTableRef} columnsProps={columnsProps} columnsData={columnsData}/>
          </Form.Item>

          <Typography.Title
              style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
          >
            {'Description'}
          </Typography.Title>
          <Form.Item field="desc" rules={[
            { required: true, message: t['groupCreate.form.validate.desc.notEmpty.errorMsg'], validateTrigger : ['onSubmit'] },
          ]}>
            <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
          </Form.Item>
        </Form>
      </Modal>
  );
}
   

