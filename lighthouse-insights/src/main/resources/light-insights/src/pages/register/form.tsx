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
import { queryAll as queryAllDepartment } from "@/api/department";

export default function RegisterForm() {
  const formRef = useRef<FormInstance>();
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [departmentOptions, setDepartmentOptions] = useState([]);

    const t = useLocale(locale);
    useEffect(() => {
        queryAllDepartment(null).then((res:any) => {
            const {code, msg, data} = res;
            if (code === '0') {
                const departmentMap = new Map();
                data.forEach(x => {
                    departmentMap.set(x.id,x);
                })
                const newOptions = data.map(function(department) {
                    let name = '';
                    const fullpath = department.fullpath;
                    const parentArr = fullpath.split(",");
                    for (let i = 0; i < parentArr.length; i++) {
                        const department = departmentMap.get(parentArr[i]);
                        name += department.name;
                        if(i !== parentArr.length - 1){
                            name += "_";
                        }
                    }
                    return {
                        label:name,
                        value:department.id,
                        key:department.id
                    }
                }).filter(x => x.key !== "0")
                setDepartmentOptions(newOptions);
            } else {
                setErrorMessage(msg || t['register.form.getDepartmentsInfo.errMsg']);
            }
        },
            (error) => {
                setErrorMessage(t['system.error']);
            })
    }, [departmentOptions]);



  async function register(params) {
    setErrorMessage('');
    setLoading(true);
    try{
      const data =
          await registerRequest(params).then((res:any) => {
            const {code, msg, data} = res;
            if (code === '0') {
                window.location.href = '/login';
            } else {
              setErrorMessage(msg || t['register.form.register.errMsg']);
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
                { required: true, match: new RegExp(/^[a-zA-Z0-9_]{5,15}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onBlur']},
                ]}>
                <Input prefix={<IconUser />} placeholder='Enter Your UserName' />
            </FormItem>
            <FormItem field='password' rules={[
                { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onBlur'] },
                { required: true, match: new RegExp(/^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{4,19}$/,"g"),message: t['register.form.password.validate.errMsg'] , validateTrigger : ['onBlur']},
            ]}>
                <Input prefix={<IconLock />} placeholder='Enter Your Password' />
            </FormItem>
            <FormItem
                field='confirm_password'
                dependencies={['password']}
                rules={[
                        { required: true, message: t['register.form.confirm.password.errMsg'], validateTrigger : ['onBlur'] },
                    {
                    validator: (v, callback) => {
                        try{
                            if (!v) {
                                return callback(t['register.form.confirm.password.errMsg'])
                            } else if (form.getFieldValue('password') !== v) {
                                return callback(t['register.form.confirm.password.equals.errMsg']);
                            }
                        }catch (error){
                            console.log("error:" + error);
                        }
                    }
                }]}
            >
                <Input prefix={<IconLock />} placeholder='Confirm Your Password' />
            </FormItem>
            <FormItem
                field='email' rules={[
                    { required: true, message: t['register.form.email.errMsg'], validateTrigger : ['onBlur'] },
                    { required: true, match: new RegExp(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,"g"),message: t['register.form.email.validate.errMsg'] , validateTrigger : ['onBlur']},
                ]}>
                <Input prefix={<IconEmail />} placeholder='Enter Your Email'
            />
            </FormItem>
            <FormItem
                field='phone'
                rules={[
                    { required: true, message: t['register.form.phone.errMsg'] , validateTrigger : ['onBlur']},
                ]}>
                <Input prefix={<IconPhone />} placeholder='Enter Your Phone Number' />
            </FormItem>
            <FormItem
                field='department'
                rules={[
                    { required: true, message: t['register.form.department.errMsg'] , validateTrigger : ['onBlur']},
                ]}>
                <Select prefix={<IconIdcard/>}
                    placeholder='Please Select Department'
                        showSearch={{
                            retainInputValue: true,
                        }}
                        filterOption={function (inputValue,option) {
                            return option.props.children.toLowerCase().indexOf(inputValue.toLowerCase()) >= 0
                        }}
                    >
                    {departmentOptions.map((option, index) => (
                        <Select.Option key={option.value}  value={option.value}>
                            {option.label}
                        </Select.Option>
                    ))}
                </Select>
            </FormItem>
            <FormItem>
                <Button style={{marginBottom:16}} type='primary' htmlType='submit' long loading={loading}>
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
