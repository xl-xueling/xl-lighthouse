import React, {useContext, useRef, useState} from 'react';
import { useSelector } from 'react-redux';
import cs from 'classnames';
import {Button, Form, Input, Message, Space, TreeSelect} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {DepartmentArcoTreeNode, User} from "@/types/insights-web";
import {FormInstance} from "@arco-design/web-react/es/Form";
import {GlobalContext} from "@/context";
import {requestUpdate} from "@/api/user";

function Security() {
  const t = useLocale(locale);

  const userInfo = useSelector((state: {userInfo:User}) => state.userInfo);
  const formRef = useRef<FormInstance>();
  const [form] = Form.useForm();
  const [formLoading, setFormLoading] = useState(false);
  const { lang } = useContext(GlobalContext);


  function onSubmitClick() {
    setFormLoading(true);
    formRef.current.validate().then((values) => {
      const proc = async () =>{
        const result = await requestUpdate(values);
        if (result.code === '0') {
          Message.success("修改信息成功");
        } else {
          Message.error(result.message || "System Error!");
        }
      }
      proc().then();
    }).catch(() => {
      console.log("system error!")
      Message.error("System Error!");
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
            style={{ display:"none" }}
            field="id"
            rules={[
              {
                required: true,
                message: t['userSetting.info.userName.placeholder'],
              },
            ]}
        >
          {(
              <Input disabled={true} placeholder={t['userSetting.info.nickName.placeholder']}  />
          )}
        </Form.Item>

        <Form.Item
            label={'原密码'}
            field="password"
            rules={[
              {
                required: true,
                message: t['userSetting.info.userName.placeholder'],
              },
            ]}
        >
          {(
              <Input placeholder={t['userSetting.info.nickName.placeholder']}  />
          )}
        </Form.Item>

        <Form.Item
            label={'新密码'}
            field="userName"
            rules={[
              {
                required: true,
                message: t['userSetting.info.userName.placeholder'],
              },
            ]}
        >
          {(
              <Input placeholder={t['userSetting.info.nickName.placeholder']}  />
          )}
        </Form.Item>

          <Form.Item
              label={'再次输入密码'}
              field="userName"
              rules={[
                  {
                      required: true,
                      message: t['userSetting.info.userName.placeholder'],
                  },
              ]}
          >
              {(
                  <Input placeholder={t['userSetting.info.nickName.placeholder']}  />
              )}
          </Form.Item>

        <Form.Item label=" ">
          <Space>
            <Button type="primary" long htmlType='submit' loading={formLoading}>
              Submit
            </Button>
            <Button
                href={"/register"}
                type="text"
                long
                className={styles['login-form-register-btn']}>
              Cancel
            </Button>
          </Space>
        </Form.Item>
      </Form>
  );
}

export default Security;
