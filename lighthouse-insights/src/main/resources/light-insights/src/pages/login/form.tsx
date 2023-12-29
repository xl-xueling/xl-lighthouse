import {
  Form,
  Input,
  Checkbox,
  Button,
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


export default function LoginForm() {
  const t = useLocale(locale);
  const formRef = useRef<FormInstance>();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [agreeLicence,setAgreeLicence] = useState(true);

  async function login(params) {
    const loginParam = {
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
        Message.error(message || t['login.form.login.errMsg']);
      }
    }).finally(() => {setLoading(false)})
  }

  function onSubmitClick() {
    try{
      if(!agreeLicence){
        Message.error(t['login.form.agreeLicence.errMsg']);
        return;
      }
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
          rules={[{ required: true, message: t['login.form.userName.errMsg'] }]}
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
          rules={[{ required: true, message: t['login.form.password.errMsg'] }]}
        >
          <Input.Password
            prefix={<IconLock />}
            autoComplete='off'
            placeholder={t['login.form.password.placeholder']}
            onPressEnter={onSubmitClick}
          />
        </Form.Item>
        <Space size={16} direction="vertical">
          <div className={styles['login-form-password-actions']}>
            <Checkbox checked={agreeLicence} onChange={setAgreeLicence}>
              {t['login.form.agreeLicence']}
            </Checkbox>
          </div>
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
