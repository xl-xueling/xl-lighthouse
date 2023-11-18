import React, {useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
  Form,
  Input,
  Select,
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
import {useSelector} from "react-redux";
import {Department, User} from "@/types/insights-web";
import {translateToTreeStruct} from "@/pages/department/common";
import InfoForm from "@/pages/user/setting/info";
import Security from "@/pages/user/setting/security";

const { Row, Col } = Grid;
const { useForm } = Form;

function SearchForm(props: {
  onSearch: (values: Record<string, any>) => void;
  form;
  onClear;
}):any {
  const { lang } = useContext(GlobalContext);
  const treeRef = useRef(null);
  const t = useLocale(locale);


  const handleSubmit = () => {
    const values = props.form.getFieldsValue();
    props.onSearch(values);
  };

  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

  const [departData, setDepartData] = useState([]);
  useEffect(() => {
    setDepartData(translateToTreeStruct(allDepartInfo,'0'));
  },[])

  // const handleReset = () => {
  //   props.form.resetFields();
  //   props.onSearch({});
  // };

  const colSpan = lang === 'zh-CN' ? 8 : 12;

  return (
    <div className={styles['search-form-wrapper']}>
      <Form
        form={props.form}
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
                  treeData={departData}
                  onChange={(e,v) => {
                    if(!e || e.length == '0'){
                      props.form.resetFields();
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
        <Button type="primary" icon={<IconSearch />} onClick={handleSubmit}>
          {t['projectList.form.search']}
        </Button>
        <Button icon={<IconRefresh />} onClick={props.onClear}>
          {t['projectList.form.reset']}
        </Button>
      </div>
    </div>
  );
}

export default SearchForm;
