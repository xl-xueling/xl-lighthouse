import {
  Button,
  Card,
  Checkbox,
  Form,
  Grid,
  Input, Message,
  Modal,
  Radio,
  Select, Space,
  Tabs,
  Typography
} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import QuickOperation from "./statistic-list";
import GroupBasicInfo from "@/pages/project/manage/group_basic";
import EditTable, {EditTableColumnProps, EditTableComponentEnum} from "@/pages/components/edittable/EditTable";
import {IconMinusCircleFill, IconPenFill, IconPlus, IconPlusCircleFill} from "@arco-design/web-react/icon";
import {FormInstance} from "@arco-design/web-react/lib";
import {getTextBlenLength, stringifyObj} from "@/utils/util";
import {requestCreate} from "@/api/group";
import {Group} from "@/types/insights-web";

export default function GroupAddPanel({onClose}) {

  const [confirmLoading, setConfirmLoading] = useState(false);

  const editTableRef= useRef(null);

  const [form] = Form.useForm();

  const columnNameRegex = /^[a-zA-Z]\w{3,14}$/;

  const onOk = async() => {
    setConfirmLoading(true);
    try{
      const v = await form.validate();
      const params:Group = new Group();
      params.token = v.token;
      params.desc = v.desc;
      const columns = editTableRef.current.getData();
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
      params.columns = columns;
      const result = await requestCreate(params);
    }catch (e) {
      console.info(e);
    }finally {
      setConfirmLoading(false);
    }
  }

  const formItemLayout = {
    labelCol: {
      span: 4,
    },
    wrapperCol: {
      span: 20,
    },
  };

  const t = useLocale(locale);
  const FormItem = Form.Item;

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
      componentType:EditTableComponentEnum.SELECT,
      headerCellStyle: { width:'90px'},
      render:(k,v) => (
          <Select size={"mini"}
                  onChange={editTableRef.current.cellValueChangeHandler}
                  defaultValue={1}
          >
            <Select.Option key={1}  value={1}>
              String
            </Select.Option>
            <Select.Option key={2}  value={2}>
              Number
            </Select.Option>
          </Select>
      )
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
          visible={true}
          style={{ width:'750px' }}
          confirmLoading={confirmLoading}
          onCancel={onClose}
      >
        <Form
            form={form}
            className={styles['search-form']}
            layout={"vertical"}>

          <Typography.Title
              style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
          >
            {'Token'}
          </Typography.Title>
          <Form.Item field="token"
                     rules={[
                       { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onBlur'] },
                       { required: true, match: new RegExp(/^[a-z0-9_]{5,20}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onBlur']},
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

            <EditTable ref={editTableRef} columns={columnsProps} initData={[]} />
          </Form.Item>

          <Typography.Title
              style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
          >
            {'Description'}
          </Typography.Title>
          <Form.Item field="desc" rules={[
            { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onBlur'] },
            { required: true, match: new RegExp(/^[^￥{}【】#@=^&|《》]{0,200}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onBlur']},
          ]}>
            <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
          </Form.Item>
        </Form>
      </Modal>
  );
}
   

