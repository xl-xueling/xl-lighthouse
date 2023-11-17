import React, {useContext, useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import { GlobalContext } from '@/context';
import {
  Input,
  Select,
  Cascader,
  Button,
  Form,
  Space,
  Message,
  Skeleton, TreeSelect,
} from '@arco-design/web-react';
import {useSelector} from "react-redux";
import {Department, DepartmentArcoTreeNode, User} from "@/types/insights-web";
import {stringifyObj} from "@/utils/util";
import {getDataWithLocalCache} from "@/utils/localCache";
import {
  fetchAllData as fetchAllDepartmentData,
  translateToFlatStruct,
} from "@/pages/department/common";
import styles from "@/pages/login/style/index.module.less";
import {FormInstance} from "@arco-design/web-react/es/Form";
import {requestUpdateById} from "@/api/user";
import {registerRequest} from "@/api/register";

function InfoForm() {

  const t = useLocale(locale);
  const [form] = Form.useForm();
  const formRef = useRef<FormInstance>();
  const { lang } = useContext(GlobalContext);

  const userInfo = useSelector((state: {userInfo:User}) => state.userInfo);
  const loading = useSelector((state: {userLoading:boolean}) => state.userLoading);

  const [formLoading, setFormLoading] = useState(false);

  const [departmentData, setDepartmentData] = useState<Array<DepartmentArcoTreeNode>>(null);

  useEffect(() => {
    const proc = async ():Promise<Array<Department>> => {
      return await getDataWithLocalCache('cache_all_department',300,fetchAllDepartmentData);
    }
    proc().then((result) => {
      setDepartmentData(translateToFlatStruct(result));
    })
  },[])

  const initialValues = {
     "id":userInfo.id,
    "userName":userInfo.userName,
    "departmentName":userInfo.departmentName,
    "phone":userInfo.phone,
    "email":userInfo.email,
    "createdTime":userInfo.createdTime,
    "state":userInfo.state,
  }


  function onSubmitClick() {
      setFormLoading(true);
      formRef.current.validate().then((values) => {
          const proc = async () =>{
              const result = await requestUpdateById(values);
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

  const handleReset = () => {
    form.resetFields();
  };

  const loadingNode = (rows = 1) => {
    return (
      <Skeleton
        text={{
          rows,
          width: new Array(rows).fill('100%'),
        }}
        animation
      />
    );
  };

  return (
      <Form
          style={{ width: '60%', marginTop: '30px' }}
          form={form}
          ref={formRef}
          initialValues = {initialValues}
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
              {loading ? (
                  loadingNode()
              ) : (
                  <Input disabled={true} placeholder={t['userSetting.info.nickName.placeholder']}  />
              )}
          </Form.Item>

        <Form.Item
            label={t['userSetting.info.userName']}
            field="userName"
            rules={[
              {
                required: true,
                message: t['userSetting.info.userName.placeholder'],
              },
            ]}
        >
          {loading ? (
              loadingNode()
          ) : (
              <Input placeholder={t['userSetting.info.nickName.placeholder']}  />
          )}
        </Form.Item>
        <Form.Item
            label={t['userSetting.info.email']}
            field="email"
            rules={[
              {
               required: true,
                message: t['userSetting.info.email.placeholder'],
              },
            ]}
        >
          {loading ? (
              loadingNode()
          ) : (
              <Input placeholder={t['userSetting.info.email.placeholder']} />
          )}
        </Form.Item>

        <Form.Item
            label={t['userSetting.info.phone']}
            field="phone"
            rules={[
              {
                  required: true,
                message: t['userSetting.info.phone.placeholder'],
              },
            ]}
        >
          {loading ? (
              loadingNode()
          ) : (
              <Input placeholder={t['userSetting.info.phone.placeholder']} />
          )}
        </Form.Item>

          <Form.Item label={t['userSetting.info.department']} field="department"
                     rules={[
                         {
                             required: true,
                         },
                     ]}>
              <TreeSelect
                  placeholder={"Please select"}
                  treeData={departmentData}
                  allowClear={true}
                  onChange={(e,v) => {
                      if(!e || e.length == '0'){
                          form.resetFields();
                          return;
                      }
                  }}
                  style={{ width: '100%'}}
              />
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

export default InfoForm;
