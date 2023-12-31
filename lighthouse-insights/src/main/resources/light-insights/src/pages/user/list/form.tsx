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
import {translate} from "@/pages/department/common";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {useSelector} from "react-redux";
const { Row, Col } = Grid;

function SearchForm(props: {onSearch: (values: Record<string, any>) => void;}) {

  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  const { lang } = useContext(GlobalContext);
  const [loading, setLoading] = useState(false);
  const treeRef = useRef(null);
  const formRef = useRef(null);
  const t = useLocale(locale);
  const [form] = Form.useForm();
  const handleSubmit = () => {
    const values = form.getFieldsValue();
    props.onSearch(values);
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      const formInstance = formRef.current;
      formInstance.submit();
    }
  };

  const handleReset = () => {
    form.resetFields();
    props.onSearch({});
  };

  const colSpan = 12;

  useEffect(() => {
    document.addEventListener('keydown', handleKeyPress);
    return () => {
      document.removeEventListener('keydown', handleKeyPress);
    };
  },[])


  return (
    <div className={styles['search-form-wrapper']}>
      <Form
        form={form}
        ref={formRef}
        className={styles['search-form']}
        labelAlign="left"
        onSubmit={() => {
          handleSubmit();
        }}
        colon={" :"}
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 19 }}
      >
        <Row gutter={24}>
          <Col span={colSpan}>
            <Form.Item label={t['userList.columns.userName']} field="search">
              <Input
                allowClear
                placeholder={t['userList.userName.placeholder']}
              />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item
              label={t['userList.columns.state']}
              field="states"
            >
              <Select
                placeholder={t['userList.state.placeholder']}
                mode="multiple"
                allowClear
              >
                <Select.Option value={1}>{t['userList.columns.state.normal']}</Select.Option>
                <Select.Option value={2}>{t['userList.columns.state.frozen']}</Select.Option>
              </Select>
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label={t['userList.columns.department']} field="departmentIds">
              <TreeSelect
                  ref={treeRef}
                  placeholder={"Please Select"}
                  multiple={true}
                  allowClear={true}
                  treeData={translate(allDepartInfo)}
              />
            </Form.Item>
          </Col>
        </Row>
      </Form>
      <div className={styles['right-button']}>
        <Button type="primary" icon={<IconSearch />} htmlType="submit" onClick={handleSubmit}>
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
