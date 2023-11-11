import {
    Form,
    Input,
    Checkbox,
    Link,
    Button,
    Space, Message, Select, Avatar,
} from '@arco-design/web-react';
import { FormInstance } from '@arco-design/web-react/es/Form';
import {IconDesktop, IconDice, IconEmail, IconIdcard, IconLock, IconPhone, IconUser} from '@arco-design/web-react/icon';
import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import useStorage from '@/utils/useStorage';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import { registerRequest } from '@/api/register'

export default function RegisterForm() {
  const formRef = useRef<FormInstance>();
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(false);


  const [departmentOptions, setDepartmentOptions] = useState([]);

    const toptions = ['Beijing', 'Shanghai', 'Guangzhou', 'Shenzhen', 'Wuhan'];


    useEffect(() => {
        fetch('https://randomuser.me/api/?results=3')
            .then((response) => response.json())
            .then((body) => {
                const newOptions = body.results.map((user) => ({
                    label: user.name.first,
                    value: user.email,
                }));
                console.log("newOptions:" + JSON.stringify(newOptions))
                setDepartmentOptions(newOptions);
            });
    }, []);

  const t = useLocale(locale);

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

  async function register(params) {
    setErrorMessage('');
    setLoading(true);
    try{
      console.log("send params:" + JSON.stringify(params));
      const data =
          await registerRequest(params).then((res:any) => {
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
      try{
          formRef.current.validate().then((values) => {
              register(values);
          });
      }catch (error){
          console.log("error:"+error)
      }
  }
    const [form] = Form.useForm();
    const FormItem = Form.Item;
    return (
        <div className={styles['register-form-wrapper']}>
        <div className={styles['register-form-title']}>{t['register.form.title']}</div>
        <div className={styles['register-form-error-msg']}>{errorMessage}</div>
        <Form
            form={form}
            ref={formRef}
            wrapperCol={{ span: 24 }}
            autoComplete='off'
            onSubmit={(v) => {
                onSubmitClick();
                Message.success('success');
            }}
        >
            <FormItem field='userName' rules={[
                { required: true, message: t['register.form.userName.errMsg'] , validateTrigger : ['onBlur']},
                { required: true, match: new RegExp(/^[a-zA-Z0-9_]{5,15}$/,"g"),message: '用户名校验失败' , validateTrigger : ['onBlur']},
                ]}>
                <Input prefix={<IconUser />} placeholder='Enter Your UserName' />
            </FormItem>
            <FormItem field='password' rules={[
                { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onBlur'] },
                { required: true, match: new RegExp(/^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{4,19}$/,"g"),message: '密码校验失败' , validateTrigger : ['onBlur']},
            ]}>
                <Input prefix={<IconLock />} placeholder='Enter Your Password' />
            </FormItem>
            <FormItem
                field='confirm_password'
                dependencies={['password']}
                rules={[
                    {
                    validator: (v, callback) => {
                        try{
                            if (!v) {
                                return callback(t['register.form.confirm.password.errMsg'])
                            } else if (form.getFieldValue('password') !== v) {
                                return callback(t['register.form.confirm.password.equals.errMsg']);
                            }
                        }catch (error){

                        }
                    }
                }]}
            >
                <Input prefix={<IconLock />} placeholder='Confirm Your Password' />
            </FormItem>
            <FormItem
                field='email' rules={[
                    { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onBlur'] },
                    { required: true, match: new RegExp(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,"g"),message: '邮箱校验失败' , validateTrigger : ['onBlur']},
                ]}>
                <Input prefix={<IconEmail />} placeholder='Enter Your Email'
            />
            </FormItem>
            <FormItem
                field='phone'>
                <Input prefix={<IconPhone />} placeholder='Enter Your Phone Number' />
            </FormItem>
            <FormItem
                field='department'>
                <Select prefix={<IconIdcard/>}
                        options={departmentOptions}
                    placeholder='Please Select Department'
                    showSearch
                >
                </Select>
            </FormItem>
            <FormItem>
                <Button style={{marginBottom:16}} type='primary' htmlType='submit' long>
                    Register
                </Button>
                <Button href={"/login"}
                        type="text"
                          long
                          className={styles['login-form-register-btn']}>
                          {t['register.form.login']}
                        </Button>
            </FormItem>
        </Form>
        </div>
    );
}
