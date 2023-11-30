import React, {useContext, useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import { GlobalContext } from '@/context';
import {
  Input,
  Select,
  Cascader,
  Button,
  Form,
  Space,
  Message,
  Skeleton, TreeSelect,
} from '@arco-design/web-react';
import {useSelector} from "react-redux";
import {FormInstance} from "@arco-design/web-react/es/Form";
import {requestUpdateById} from "@/api/user";
import {translate} from "@/pages/department/common";
import {ResultData} from "@/types/insights-common";

export default function BasicInfoForm({userInfo,allDepartInfo}) {

  const t = useLocale(locale);
  const [form] = Form.useForm();
  const formRef = useRef<FormInstance>();
  const { lang } = useContext(GlobalContext);
  const [formLoading, setFormLoading] = useState(false);

  const initialValues = {
     "id":userInfo.id,
    "userName":userInfo.userName,
    "department":String(userInfo.departmentId),
    "phone":userInfo.phone,
    "email":userInfo.email,
    "createdTime":userInfo.createdTime,
    "state":userInfo.state,
  }

  const onSubmitClick = () => {
      setFormLoading(true);
      formRef.current.validate().then((values) => {
          const proc = async () =>{
              const result:ResultData = await requestUpdateById(values);
              if (result.code === '0') {
                  Message.success(t['userSetting.form.basicinfo.success']);
              } else {
                  Message.error(result.message || t['system.error']);
              }
          }
          proc().then();
      }).catch((error) => {
         console.log(error)
          Message.error(t['system.error']);
      }).finally(() =>
      {
          setFormLoading(false);
      })
  }

  const handleReset = () => {
    form.resetFields();
  };

  return (
      <Form
          style={{ width: '60%', marginTop: '30px' }}
          form={form}
          ref={formRef}
          initialValues = {initialValues}
          labelCol={{ span: lang === 'en-US' ? 5 : 4 }}
          wrapperCol={{ span: lang === 'en-US' ? 17 : 18 }}
          onSubmit={(v) => {
              onSubmitClick();
          }}
      >
          <Form.Item
              style={{ display:"none" }}
              field="id"
              rules={[
                  {
                      required: true,
                      message: t['userSetting.info.userName.placeholder'],
                  },
              ]}
          >
              <Input disabled={true} placeholder={t['userSetting.info.nickName.placeholder']}  />
          </Form.Item>

        <Form.Item
            label={t['userSetting.info.userName']}
            field="userName"
            rules={[
                { required: true, message: t['userSetting.form.userName.errMsg'] , validateTrigger : ['onBlur']},
                { required: true, match: new RegExp(/^[a-zA-Z0-9_]{5,15}$/,"g"),message: t['userSetting.form.userName.validate.errMsg'] , validateTrigger : ['onBlur']},
            ]}>
            <Input placeholder={t['userSetting.info.nickName.placeholder']}  />
        </Form.Item>
        <Form.Item
            label={t['userSetting.info.email']}
            field="email"
            rules={[
                { required: true, message: t['userSetting.form.email.errMsg'], validateTrigger : ['onBlur'] },
                { required: true, match: new RegExp(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,"g"),message: t['userSetting.form.email.validate.errMsg'] , validateTrigger : ['onBlur']},
            ]}>
            <Input placeholder={t['userSetting.info.email.placeholder']} />
        </Form.Item>

        <Form.Item
            label={t['userSetting.info.phone']}
            field="phone"
            rules={[
                { required: false, match: new RegExp(/^[\d()\\-]{5,20}$/,"g"),message: t['userSetting.form.phone.validate.errMsg'] , validateTrigger : ['onBlur']},
            ]}
        >
            <Input placeholder={t['userSetting.info.phone.placeholder']} />
        </Form.Item>

          <Form.Item label={t['userSetting.info.department']} field="department"
                     rules={[
                         {
                             required: true,
                         },
                     ]}>
              <TreeSelect
                  placeholder={"Please Select"}
                  treeData={translate(allDepartInfo)}
                  allowClear={true}
                  showSearch={true}
              />
          </Form.Item>

          <Form.Item label=" ">
              <Space>
                  <Button type="primary" long htmlType='submit' loading={formLoading}>
                      Submit
                  </Button>
              </Space>
          </Form.Item>
      </Form>
  );
}