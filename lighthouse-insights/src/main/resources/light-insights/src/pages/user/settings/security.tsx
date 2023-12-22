import React, {useContext, useRef, useState} from 'react';
import { useSelector } from 'react-redux';
import {Button, Form, Input, Message, Space, TreeSelect} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {User} from "@/types/insights-web";
import {FormInstance} from "@arco-design/web-react/es/Form";
import {GlobalContext} from "@/context";
import {requestChangePassword, requestUpdateById} from "@/api/user";
import {ResultData} from "@/types/insights-common";

export default function Security({userInfo}) {
  const t = useLocale(locale);
  const formRef = useRef<FormInstance>();
  const [form] = Form.useForm();
  const [formLoading, setFormLoading] = useState(false);
  const { lang } = useContext(GlobalContext);

  function onSubmitClick() {
      console.log("-------1");
    setFormLoading(true);
    formRef.current.validate().then((values) => {
      const proc = async () =>{
        const response:ResultData = await requestChangePassword(values);
        console.log("response:" + JSON.stringify(response));
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

    const initialValues = {
        "id":userInfo.id,
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
              console.log("------222")
              onSubmitClick();
          }}
      >

          <Form.Item
              // style={{ display:"none" }}
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
              field="confirmPassword"
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