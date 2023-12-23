import React, {useContext, useEffect, useRef, useState} from 'react';
import {
  Form,
  Input,
  Select,
  Button,
  Grid, TreeSelect,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import {getDataWithLocalCache} from "@/utils/localCache";
import {fetchAllDepartmentData, translate} from "@/pages/department/common";
import {ArcoTreeNode} from "@/types/insights-web";

const { Row, Col } = Grid;

function SearchForm(props: {onSearch: (values: Record<string, any>) => void;}) {
  const { lang } = useContext(GlobalContext);
  const [loading, setLoading] = useState(false);
  const [treeData, setTreeData] = useState([]);
  const treeRef = useRef(null);
  const t = useLocale(locale);
  const [form] = Form.useForm();
  const handleSubmit = () => {
    const values = form.getFieldsValue();
    props.onSearch(values);
  };

  const handleReset = () => {
    form.resetFields();
    props.onSearch({});
  };

  const colSpan = lang === 'zh-CN' ? 8 : 12;

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


  return (
    <div className={styles['search-form-wrapper']}>
      <Form
        form={form}
        className={styles['search-form']}
        labelAlign="left"
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 19 }}
      >
        <Row gutter={24}>
          <Col span={colSpan}>
            <Form.Item label={t['userList.columns.id']} field="id">
              <Input placeholder={t['userList.id.placeholder']} allowClear />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label={t['userList.columns.userName']} field="username">
              <Input
                allowClear
                placeholder={t['userList.userName.placeholder']}
              />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item
              label={t['userList.columns.state']}
              field="state"
            >
              <Select
                placeholder={t['userList.state.placeholder']}
                mode="multiple"
                allowClear
              >
                <Select.Option value={0}>{t['userList.columns.state.pending']}</Select.Option>
                <Select.Option value={1}>{t['userList.columns.state.normal']}</Select.Option>
                <Select.Option value={2}>{t['userList.columns.state.frozen']}</Select.Option>
              </Select>
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label={t['userList.columns.department']} field="departmentId">
              <TreeSelect
                  ref={treeRef}
                  placeholder={"Please Select"}
                  multiple={true}
                  allowClear={true}
                  treeData={treeData}
              />
            </Form.Item>
          </Col>
        </Row>
      </Form>
      <div className={styles['right-button']}>
        <Button type="primary" icon={<IconSearch />} onClick={handleSubmit}>
          {t['userList.form.search']}
        </Button>
        <Button icon={<IconRefresh />} onClick={handleReset}>
          {t['userList.form.reset']}
        </Button>
      </div>
    </div>
  );
}

export default SearchForm;
