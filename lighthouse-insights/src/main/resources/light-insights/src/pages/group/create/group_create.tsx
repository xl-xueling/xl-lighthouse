import {
  Form,
  Grid,
  Input, Message,
  Modal,
  Select, Space,
  Typography
} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import EditTable, {EditTableColumnProps, EditTableComponentEnum} from "@/pages/common/edittable/EditTable";
import {IconMinusCircleFill, IconPlus} from "@arco-design/web-react/icon";
import {getTextBlenLength, stringifyObj} from "@/utils/util";
import {requestCreate} from "@/api/group";
import {Group, Project} from "@/types/insights-web";

export default function GroupCreateModal({projectId,callback,onClose}) {

  const [confirmLoading, setConfirmLoading] = useState(false);

  const editTableRef= useRef(null);
  const t = useLocale(locale);
  const FormItem = Form.Item;
  const columnNameRegex = /^[a-zA-Z]\w{3,14}$/;
  const formRef = useRef(null);

  const onOk = async() => {
    setConfirmLoading(true);
    await formRef.current.validate();
    const values = formRef.current.getFieldsValue();
    const columns = editTableRef.current.getData();
    console.log("columns is:" + JSON.stringify(columns));
    if(!columns || columns.length == 0){
      Message.error("列信息不能为空！")
      return;
    }
    for(let i=0;i<columns.length;i++){
      const name = columns[i].name;
      const desc = columns[i].desc;
      if(!columnNameRegex.test(name)){
        Message.error("列名称校验失败！")
        return;
      }
      if(desc && getTextBlenLength(desc) > 50){
        Message.error("列名称描述校验失败！")
        return;
      }
    }
    const group:Group = {
      projectId:projectId,
      token:values.token,
      desc:values.desc,
      columns:columns,
    }
    requestCreate(group).then((result) => {
      if(result.code === '0'){
        Message.success(t['groupCreate.form.submit.success']);
        setTimeout(() => {
          setConfirmLoading(false);
          window.location.href = "/project/manage/"+projectId;
        },3000)
      }else{
        Message.error(result.message || t['system.error']);
      }
    }).catch((error) => {
      console.log(error);
      Message.error(t['system.error'])
    })
  }



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
      initValue:1,
      componentType:EditTableComponentEnum.SELECT,
      headerCellStyle: { width:'130px'},
      render:(text, record) => {
        return (
            <Select size={"mini"}
                         onChange={(value) => {record['type'] = value}}
                          defaultValue={1}>
              <Select.Option key={1}  value={1}>
                String
              </Select.Option>
              <Select.Option key={2}  value={2}>
                Number
              </Select.Option>
        </Select>)
      }
    },
    {
      title: 'Description',
      dataIndex: 'desc',
      componentType:EditTableComponentEnum.INPUT,
      editable: true,
    },
    {
        title: 'Operate',
        dataIndex: 'operate',
        componentType:EditTableComponentEnum.BUTTON,
        headerCellStyle: { width:'12%'},
        render: (_, record) => (
            <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%',paddingTop:'5px' }}>
                <IconMinusCircleFill style={{ cursor:"pointer"}} onClick={() => editTableRef.current.removeRow(record.key)}/>
            </Space>
        ),
    },
  ];
  return (
      <Modal
          title='Create Group'
          onOk={onOk}
          maskClosable={false}
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
                       { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onSubmit'] },
                       { required: true, match: new RegExp(/^[a-z0-9_]{5,20}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onSubmit']},
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
                <IconPlus style={{ cursor:"pointer" }} onClick={() => editTableRef.current.addRow()}/>
              </Grid.Col>
            </Grid.Row>

            <EditTable ref={editTableRef} columnsProps={columnsProps} columnsData={[]}/>
          </Form.Item>

          <Typography.Title
              style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
          >
            {'Description'}
          </Typography.Title>
          <Form.Item field="desc" rules={[
            { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onSubmit'] },
            { required: true, match: new RegExp(/^[^￥{}【】#@=^&|《》]{0,200}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onSubmit']},
          ]}>
            <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
          </Form.Item>
        </Form>
      </Modal>
  );
}
   

