import {
  Form,
  Input,
  Checkbox,
  Button,
  Notification,
  Space, Message,
} from '@arco-design/web-react';

import { FormInstance } from '@arco-design/web-react/es/Form';
import { IconLock, IconUser } from '@arco-design/web-react/icon';
import React, {useRef, useState } from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {requestLogin} from "@/api/user";
import {ResultData} from "@/types/insights-common";
import md5 from 'md5';
import {LoginParam} from "@/types/insights-web";


export default function LoginForm() {
  const t = useLocale(locale);
  const formRef = useRef<FormInstance>();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  async function login(params) {
    const loginParam:LoginParam = {
      username:params.username,
      password:md5(params.password),
    }
    setLoading(true);
    await requestLogin(loginParam).then((response:ResultData) => {
      const {code, message, data} = response;
      if (code === '0') {
        localStorage.setItem('userStatus', 'login');
        localStorage.setItem('accessKey',data.accessKey);
        localStorage.setItem('refreshKey',data.refreshKey);
        window.location.href = '/';
      } else {
        Notification.warning({
          style: { width: 420 },
          title: 'Warning',
          content: message || t['login.form.login.errMsg'],
        })
      }
    }).finally(() => {setLoading(false)})
  }

  function onSubmitClick() {
    try{
      formRef.current.validate().then((values) => {
        login(values).then();
      });
    }catch (error){
      console.log(error)
    }
  }
  return (
    <div className={styles['login-form-wrapper']}>
      <div className={styles['login-form-title']}>{t['login.form.title']}</div>
      <Form
        className={styles['login-form']}
        layout="vertical"
        form={form}
        ref={formRef}
        autoComplete='off'
        onSubmit={(v) => {
          onSubmitClick();
        }}
      >
        <Form.Item
          field="username"
          rules={[
            { required: true, message: t['login.form.userName.errMsg'] , validateTrigger : ['onBlur']},
            { required: true, match: new RegExp(/^[a-zA-Z0-9_]{5,15}$/,"g"),message: t['login.form.userName.validate.errMsg'] , validateTrigger : ['onBlur']},
          ]}
        >
          <Input
            prefix={<IconUser />}
            autoComplete='off'
            placeholder={t['login.form.userName.placeholder']}
            onPressEnter={onSubmitClick}
          />
        </Form.Item>
        <Form.Item
          field="password"
          rules={[
            { required: true, message: t['login.form.password.errMsg'], validateTrigger : ['onBlur'] },
            { required: true, match: new RegExp(/^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,24}$/,"g"),message: t['login.form.password.validate.errMsg'] , validateTrigger : ['onBlur']},
          ]}
        >
          <Input.Password
            prefix={<IconLock />}
            autoComplete='off'
            placeholder={t['login.form.password.placeholder']}
            onPressEnter={onSubmitClick}
          />
        </Form.Item>
        <Form.Item field="agreeLicence"
                   rules={[
                     {
                       validator: (value, callback) => {
                         if (!value || value != 'true' ) {
                           callback(t['login.form.agreeLicence.errMsg']);
                         }
                       },
                     },
                   ]}>
          <Checkbox.Group>
            <Checkbox value={true}>
              {t['login.form.agreeLicence']}
            </Checkbox>
          </Checkbox.Group>
        </Form.Item>
        <Space size={16} direction="vertical">
          <Button type="primary" long htmlType='submit' loading={loading}>
            {t['login.form.login']}
          </Button>
          <Button
              href={"/register"}
              type="text"
              long
              className={styles['login-form-register-btn']}>
            {t['login.form.register']}
          </Button>
        </Space>
      </Form>
    </div>
  );
}
