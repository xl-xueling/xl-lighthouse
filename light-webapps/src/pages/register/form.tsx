import {
    Form,
    Input,
    Button, Message, TreeSelect, Notification,
} from '@arco-design/web-react';
import { FormInstance } from '@arco-design/web-react/es/Form';
import {IconEmail, IconIdcard, IconLock, IconUser} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {ResultData} from "@/types/insights-common";
import {getDataWithLocalCache} from "@/utils/localCache";
import {fetchAllDepartmentData, translate} from "@/pages/department/common";
import {ArcoTreeNode, User} from "@/types/insights-web";
import {requestRegister} from "@/api/user";
import md5 from 'md5';

export default function RegisterForm() {

    const t = useLocale(locale);
    const formRef = useRef<FormInstance>();
    const [loading, setLoading] = useState(false);
    const [form] = Form.useForm();
    const FormItem = Form.Item;
    const [treeData,setTreeData] = useState([]);

    useEffect(() => {
        const proc = async () => {
            const allDepartInfo = await getDataWithLocalCache('cache_all_department',300,fetchAllDepartmentData);
            if(allDepartInfo){
                const data: ArcoTreeNode[] = translate(allDepartInfo);
                setTreeData(data);
            }
        }
        proc().then();
    },[])

      async function register(params) {
          setLoading(true);
          const registerParams:User = {
            username:params.username,
            password:md5(params.password),
            email:params.email,
            departmentId:Number(params.departmentId),
          }
          await requestRegister(registerParams).then((response:ResultData) => {
              const {code, message, data} = response;
              if (code === '0') {
                  Notification.info({
                      style: { width: 420 },
                      title: 'Notification',
                      content: t['register.form.success'],
                  })
                  setTimeout(() => {
                      window.location.href = '/login';
                      setLoading(false);
                  },3000)
              } else {
                  Notification.warning({
                      style: { width: 420 },
                      title: 'Notification',
                      content:message || t['system.error'],
                  })
                  setLoading(false);
              }
          }).catch((error) => {
              Notification.error({
                  style: { width: 420 },
                  title: 'Notification',
                  content:t['system.error'],
              })
              setLoading(false);
          })
      }

  function onSubmitClick() {
      try{
          formRef.current.validate().then((values) => {
              register(values).then();
          });
      }catch (error){
          console.log(error);
          Message.error(t['system.error']);
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
            <FormItem field='username' rules={[
                { required: true, message: t['register.form.userName.errMsg'] , validateTrigger : ['onBlur']},
                { required: true, match: new RegExp(/^[a-zA-Z0-9_]{5,15}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onBlur']},
                ]}>
                <Input prefix={<IconUser />} placeholder='Enter Your UserName' />
            </FormItem>
            <FormItem field='password' rules={[
                { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onBlur'] },
                { required: true, match: new RegExp(/^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,24}$/,"g"),message: t['register.form.password.validate.errMsg'] , validateTrigger : ['onBlur']},
            ]}>
                <Input prefix={<IconLock />} placeholder='Enter Your Password' />
            </FormItem>
            <FormItem
                field='confirmPassword'
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
                field='departmentId'
                rules={[
                    { required: true, message: t['register.form.department.errMsg'] , validateTrigger : ['onBlur']},
                ]}>
                <TreeSelect prefix={<IconIdcard/>} showSearch={true}
                            filterTreeNode={(inputText,node) => {
                                return node.props.title.toLowerCase().indexOf(inputText.toLowerCase()) > -1;
                            }}
                            placeholder='Select Department'
                            allowClear={true}  treeData={treeData} />
            </FormItem>
            <FormItem>
                <Button style={{marginBottom:16}} type='primary' htmlType='submit' long loading={loading}>
                    {t['register.form.register']}
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
