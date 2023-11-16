import React, {useContext, useEffect, useState} from 'react';
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
import {fetchAllData as fetchAllDepartmentData, translateToTreeStruct} from "@/pages/department/common";

function InfoForm() {

  const t = useLocale(locale);
  const [form] = Form.useForm();
  const { lang } = useContext(GlobalContext);

  const userInfo = useSelector((state: {userInfo:User}) => state.userInfo);
  const loading = useSelector((state: {userLoading:boolean}) => state.userLoading);

  const [departmentData, setDepartmentData] = useState<Array<DepartmentArcoTreeNode>>(null);

  useEffect(() => {
    const proc = async ():Promise<Array<Department>> => {
      return await getDataWithLocalCache('cache_all_department',300,fetchAllDepartmentData);
    }
    proc().then((result) => {
      setDepartmentData(translateToTreeStruct(result,"0"));
    })
  },[])

  const initialValues = {
    "userName":userInfo.userName,
    "departmentName":userInfo.departmentName,
    "phone":userInfo.phone,
    "email":userInfo.email,
    "createdTime":userInfo.createdTime,
    "state":userInfo.state,
  }

  const handleSave = async () => {
    try {
      await form.validate();
      Message.success('userSetting.saveSuccess');
    } catch (_) {}
  };

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
      style={{ width: '500px', marginTop: '6px' }}
      form={form}
      initialValues = {initialValues}
      labelCol={{ span: lang === 'en-US' ? 7 : 6 }}
      wrapperCol={{ span: lang === 'en-US' ? 17 : 18 }}
    >
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
            <Input disabled={true} placeholder={t['userSetting.info.nickName.placeholder']}  />
        )}
      </Form.Item>
      <Form.Item
        label={t['userSetting.info.email']}
        field="email"
        rules={[
          {
            type: 'email',
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
              type: 'phone',
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

      <Form.Item label={t['userSetting.info.department']} field="department">
        <TreeSelect
            placeholder={"Please select"}
            multiple={true}
            treeData={departmentData}
            allowClear={true}
            onChange={(e,v) => {
              if(!e || e.length == '0'){
                form.resetFields();
                return;
              }
            }}
            onClear={(z) => {
              console.log("----z is:" + stringifyObj(z));
            }}
            style={{ width: '100%'}}
        />
      </Form.Item>

      <Form.Item label=" ">
        <Space>
          <Button type="primary" onClick={handleSave}>
            {t['userSetting.save']}
          </Button>
          <Button onClick={handleReset}>{t['userSetting.reset']}</Button>
        </Space>
      </Form.Item>
    </Form>
  );
}

export default InfoForm;
