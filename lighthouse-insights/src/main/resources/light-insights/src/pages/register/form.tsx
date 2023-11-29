import {
    Form,
    Input,
    Button, Message,
} from '@arco-design/web-react';
import { FormInstance } from '@arco-design/web-react/es/Form';
import {IconEmail, IconLock, IconUser} from '@arco-design/web-react/icon';
import React, {useRef, useState } from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import DepartmentTreeSelect from "@/pages/department/common/select";
import {requestRegister} from "@/api/register";
import {ResultData} from "@/types/insights-common";

export default function RegisterForm() {

    const t = useLocale(locale);
    const formRef = useRef<FormInstance>();
    const [loading, setLoading] = useState(false);
    const [form] = Form.useForm();
    const FormItem = Form.Item;

      async function register(params) {
        setLoading(true);
        try{
          await requestRegister(params).then((response:ResultData) => {
                const {code, message, data} = response;
                if (code === '0') {
                    window.location.href = '/login';
                } else {
                  Message.error(message || t['register.form.register.errMsg']);
                }
              }
          ).finally(() => {
                setLoading(false);
          });
        }catch (error){
          console.log(error);
        }
      }

  function onSubmitClick() {
      try{
          formRef.current.validate().then((values) => {
              register(values).then();
          });
      }catch (error){
          console.log(error)
      }
  }

    return (
        <div className={styles['register-form-wrapper']}>
        <div className={styles['register-form-title']}>{t['register.form.title']}</div>
        <Form
            form={form}
            ref={formRef}
            wrapperCol={{ span: 24 }}
            autoComplete='off'
            onSubmit={(v) => {
                onSubmitClick();
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
                            console.log(error);
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
                field='department'
                rules={[
                    { required: true, message: t['register.form.department.errMsg'] , validateTrigger : ['onBlur']},
                ]}>
                <DepartmentTreeSelect />
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
