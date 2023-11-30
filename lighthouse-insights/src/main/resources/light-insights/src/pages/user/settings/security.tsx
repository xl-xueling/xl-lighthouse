import React, {useContext, useRef, useState} from 'react';
import { useSelector } from 'react-redux';
import {Button, Form, Input, Message, Space, TreeSelect} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {User} from "@/types/insights-web";
import {FormInstance} from "@arco-design/web-react/es/Form";
import {GlobalContext} from "@/context";
import {requestUpdateById} from "@/api/user";
import {ResultData} from "@/types/insights-common";

export default function Security() {
  const t = useLocale(locale);
  const formRef = useRef<FormInstance>();
  const [form] = Form.useForm();
  const [formLoading, setFormLoading] = useState(false);
  const { lang } = useContext(GlobalContext);

  function onSubmitClick() {
    setFormLoading(true);
    formRef.current.validate().then((values) => {
      const proc = async () =>{
        const response:ResultData = await requestUpdateById(values);
        if (response.code === '0') {
          Message.success(t['security.form.submit.success']);
        } else {
          Message.error(response.message || t['system.error']);
        }
      }
      proc().then();
    }).catch((error) => {
      console.log(error)
      Message.error(t['system.error']);
    }).finally(() => {setFormLoading(false);})
  }


  return (
      <Form
          style={{ width: '60%', marginTop: '30px' }}
          form={form}
          ref={formRef}
          labelCol={{ span: lang === 'en-US' ? 5 : 4 }}
          wrapperCol={{ span: lang === 'en-US' ? 17 : 18 }}
          onSubmit={(v) => {
            onSubmitClick();
          }}
      >

        <Form.Item
            label={t['security.form.label.original.password']}
            field="original_password"
            rules={[
              {
                required: true,
                message: t['security.form.original.password.errMsg'],
              },
            ]}
        >
          {(
              <Input />
          )}
        </Form.Item>

        <Form.Item
            label={t['security.form.label.password']}
            field="password"
            rules={[
              {
                required: true,
                message: t['security.form.password.errMsg'],
              },
            ]}
        >
          {(
              <Input />
          )}
        </Form.Item>

          <Form.Item
              label={t['security.form.label.confirm.password']}
              field="confirm_password"
              dependencies={['password']}
              rules={[
                  { required: true, message: t['security.form.confirm.password.errMsg'], validateTrigger : ['onBlur'] },
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
                      }
                  }]}
          >
              {(
                  <Input />
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