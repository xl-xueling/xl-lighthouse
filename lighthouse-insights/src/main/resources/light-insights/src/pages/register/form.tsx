import {
    Form,
    Input,
    Checkbox,
    Link,
    Button,
    Space, Message,
} from '@arco-design/web-react';
import { FormInstance } from '@arco-design/web-react/es/Form';
import { IconLock, IconUser } from '@arco-design/web-react/icon';
import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import useStorage from '@/utils/useStorage';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import { loginRequest } from '@/api/login'

export default function RegisterForm() {
  const formRef = useRef<FormInstance>();
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const t = useLocale(locale);

  const [agreeLicence,setAgreeLicence] = useState(true);

  function afterLoginSuccess(params,data) {
    // if (rememberPassword) {
    //   setLoginParams(JSON.stringify(params));
    // } else {
    //   removeLoginParams();
    // }
    localStorage.setItem('userStatus', 'login');
    localStorage.setItem('token',data.token);
    window.location.href = '/';
  }

  async function login(params) {
    setErrorMessage('');
    setLoading(true);
    try{
      const data =
          await loginRequest(params).then((res:any) => {
            console.log("res is:" + JSON.stringify(res));
            const {code, msg, data} = res;
            console.log("token:" + data.token);
            if (code === '0') {
              afterLoginSuccess(params,data);
            } else {
              setErrorMessage(msg || t['register.form.login.errMsg']);
            }
          }
      ).finally(() => {
            setLoading(false);
      });
    }catch (error){
      console.log("error:" + error);
    }
  }

  function onSubmitClick() {
    formRef.current.validate().then((values) => {
      login(values);
    });
  }
    const [form] = Form.useForm();
    const FormItem = Form.Item;
    return (
        <div className={styles['login-form-wrapper']}>
            <div className={styles['login-form-title']}>{t['register.form.title']}</div>
            <div className={styles['login-form-error-msg']}>{errorMessage}</div>
        <Form
            form={form}
            style={{ width: 320 }}
            wrapperCol={{ span: 24 }}
            autoComplete='off'
            onValuesChange={(v, vs) => {
                console.log(v, vs);
            }}
            onSubmit={(v) => {
                console.log(v);
                Message.success('success');
            }}
        >
            <FormItem field='userName' rules={[{ required: true, message: 'username is required' }]}>
                <Input placeholder='please enter your username' />
            </FormItem>
            <FormItem field='password' rules={[{ required: true, message: 'password is required' }]}>
                <Input placeholder='please enter your password' />
            </FormItem>
            <FormItem
                field='confirm_password'
                dependencies={['password']}
                rules={[{
                    validator: (v, cb) => {
                        if (!v) {
                            return cb('confirm_password is required')
                        } else if (form.getFieldValue('password') !== v) {
                            return cb('confirm_password must be equal with password')
                        }
                        cb(null)
                    }
                }]}
            >
                <Input placeholder='please confirm your password' />
            </FormItem>
            <FormItem>
                <Button type='primary' htmlType='submit' long>
                    Register
                </Button>
            </FormItem>
        </Form>
        </div>
    );
}
