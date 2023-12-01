import React, {useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
  Form,
  Input,
  DatePicker,
  Button,
  Grid, TreeSelect, Tabs,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import {stringifyObj} from "@/utils/util";
import {translate, translateToTreeStruct} from "@/pages/department/common";

const { Row, Col } = Grid;
const { useForm } = Form;

function SearchForm({
  onSearch,
  form,
  onClear,
  allDepartInfo,
}) {
  const { lang } = useContext(GlobalContext);
  const treeRef = useRef(null);
  const t = useLocale(locale);

  const handleSubmit = () => {
    const values = form.getFieldsValue();
    onSearch(values);
  };

  const handleReset = () => {
    form.resetFields();
    onSearch({});
  };

  const colSpan = 12;

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
            <Form.Item label={t['projectList.columns.id']} field="id">
              <Input placeholder={t['projectList.id.placeholder']} allowClear />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label={t['projectList.columns.name']} field="name">
              <Input
                allowClear
                placeholder={t['projectList.name.placeholder']}
              />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label={t['projectList.columns.department']} field="department">
              <TreeSelect
                  ref={treeRef}
                  placeholder={"Please select"}
                  multiple={true}
                  allowClear={true}
                  treeData={translate(allDepartInfo)}
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
          </Col>
          <Col span={colSpan}>
            <Form.Item
              label={t['projectList.columns.createdTime']}
              field="createdTime"
            >
              <DatePicker.RangePicker
                  allowClear
                  style={{ width: '100%' }}
                  disabledDate={(date) => dayjs(date).isAfter(dayjs())}
              />
            </Form.Item>
          </Col>
        </Row>
      </Form>
      <div className={styles['right-button']}>
        <Button size={"small"} type="primary" icon={<IconSearch />} onClick={handleSubmit}>
          {t['projectList.form.search']}
        </Button>
        <Button size={"small"} icon={<IconRefresh />} onClick={onClear}>
          {t['projectList.form.reset']}
        </Button>
      </div>
    </div>
  );
}

export default SearchForm;
