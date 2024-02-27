import React, {useContext, useRef, useState} from 'react';
import { useSelector } from 'react-redux';
import {Button, Form, Input, Message, Notification, Space, TreeSelect} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {User} from "@/types/insights-web";
import {FormInstance} from "@arco-design/web-react/es/Form";
import {GlobalContext} from "@/context";
import {requestChangePassword, requestUpdateById} from "@/api/user";
import {ResultData} from "@/types/insights-common";
import md5 from 'md5';
import {removeLoginStatus} from "@/utils/checkLogin";
export default function Security({userInfo}) {
  const t = useLocale(locale);
  const formRef = useRef<FormInstance>();
  const [form] = Form.useForm();
  const [formLoading, setFormLoading] = useState(false);
  const { lang } = useContext(GlobalContext);

  function onSubmitClick() {
    setFormLoading(true);
    formRef.current.validate().then((values) => {
      const proc = async () =>{
        const changePasswdParams = {
            id:values.id,
            username:userInfo.username,
            originPassword:md5(values.originPassword),
            password:md5(values.password),
        }
        const response:ResultData = await requestChangePassword(changePasswdParams);
        const {code, data ,message} = response;
        if(code == '0'){
          Notification.info({style: { width: 420 }, title: 'Notification', content: t['security.form.submit.success']});
            setTimeout(() => {
                removeLoginStatus();
                window.location.href = '/login';
                setFormLoading(false);
            },3000)
        }else{
          Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            setFormLoading(false);
        }
      }
      proc().then();
    }).catch((error) => {
      console.log(error)
    })
  }

    const initialValues = {
        "id":userInfo?.id,
    }

  return (
      <Form
          style={{ width: '60%', marginTop: '30px' }}
          form={form}
          ref={formRef}
          autoComplete='off'
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
            label={t['security.form.label.original.password']}
            field="originPassword"
            rules={[
              {
                required: true,
                message: t['security.form.original.password.errMsg'],
                validateTrigger : ['onSubmit']
              },
            ]}
        >
          {(
              <Input.Password />
          )}
        </Form.Item>

        <Form.Item
            label={t['security.form.label.password']}
            field="password"
            rules={[
                { required: true, message: t['security.form.password.errMsg'], validateTrigger : ['onBlur'] },
                { required: true, match: new RegExp(/^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,24}$/,"g"),message: t['security.form.password.validate.errMsg'] , validateTrigger : ['onSubmit']},
            ]}
        >
          {(
              <Input.Password />
          )}
        </Form.Item>

          <Form.Item
              label={t['security.form.label.confirm.password']}
              field="confirmPassword"
              dependencies={['password']}
              rules={[
                  { required: true, message: t['security.form.confirm.password.errMsg'], validateTrigger : ['onSubmit'] },
                  {
                      validator: (v, callback) => {
                          try{
                              if (!v) {
                                  return callback(t['security.form.confirm.password.errMsg'])
                              } else if (form.getFieldValue('password') !== v) {
                                  return callback(t['security.form.confirm.password.equals.errMsg']);
                              }
                          }catch (error){
                              console.log(error);
                          }
                      }, validateTrigger : ['onSubmit']
                  }]}
          >
              {(
                  <Input.Password />
              )}
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