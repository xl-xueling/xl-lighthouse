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
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {requestLogin} from "@/api/user";
import {ResultData} from "@/types/insights-common";
import {LoginParam} from "@/types/insights-web";
import {md5} from "js-md5";


export default function LoginForm() {
  const t = useLocale(locale);
  const formRef = useRef<FormInstance>();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const login = async () => {
    const params = formRef.current.getFieldsValue();
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
        window.location.href = '/dashboard/workplace';
      } else {
        Notification.warning({
          style: { width: 420 },
          title: 'Warning',
          content: message || t['system.error'],
        })
      }
    }).catch((error)=>{
      Notification.warning({style: { width: 420 }, title: 'Error', content: t['system.error']})
    }).finally(() => {setLoading(false)})
  }

  const handleSubmit = async () => {
    try{
      await formRef.current.validate();
      login().then();
    }catch (error){
      console.log(error);
    }
  };

  useEffect(() => {
    const handleKeyPress = async (event) => {
      if (event.key === 'Enter') {
        await handleSubmit();
      }
    };
    document.addEventListener('keypress', handleKeyPress);
    return () => {
      document.removeEventListener('keypress', handleKeyPress);
    };
  }, []);


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
          handleSubmit();
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
              <span dangerouslySetInnerHTML={{__html: t['login.form.agreeLicence']}}/>
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
