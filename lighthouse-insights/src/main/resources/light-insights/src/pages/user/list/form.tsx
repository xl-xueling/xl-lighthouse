import React, {useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
  Form,
  Input,
  Select,
  DatePicker,
  Button,
  Grid, TreeSelect, Spin,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import {stringifyMap, stringifyObj} from "@/utils/util";

const { Row, Col } = Grid;
const { useForm } = Form;

function SearchForm(props: {
  departmentMap:Map<any,any>;
  onSearch: (values: Record<string, any>) => void;
}):any {
  const { lang } = useContext(GlobalContext);
  const [loading, setLoading] = useState(false);
  const [treeData, setTreeData] = useState([]);
  const treeRef = useRef(null);
  const t = useLocale(locale);
  const [form] = useForm();

  const handleSubmit = () => {
    const values = form.getFieldsValue();
    props.onSearch(values);
  };

  const handleReset = () => {
    form.resetFields();
    props.onSearch({});
  };

  const colSpan = lang === 'zh-CN' ? 8 : 12;

  function translateData(list, rootPid) {
    const nodeArr = []
    list.forEach(item => {
      if (item.pid === rootPid) {
        item.key = item.id;
        item.title = item.name;
        nodeArr.push(item)
        const children = translateData(list, item.id)
        if (children.length) {
          item.children = children
        }
      }
    })
    return nodeArr;
  }

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
            <Form.Item label={t['userList.columns.userName']} field="userName">
              <Input
                allowClear
                placeholder={t['userList.userName.placeholder']}
              />
            </Form.Item>
          </Col>
          {/*<Col span={colSpan}>*/}
          {/*  <Form.Item*/}
          {/*    label={t['userList.columns.state']}*/}
          {/*    field="state"*/}
          {/*  >*/}
          {/*    <Select*/}
          {/*      placeholder={t['userList.state.placeholder']}*/}
          {/*      options={ContentType.map((item, index) => ({*/}
          {/*        label: item,*/}
          {/*        value: index,*/}
          {/*      }))}*/}
          {/*      mode="multiple"*/}
          {/*      allowClear*/}
          {/*    />*/}
          {/*  </Form.Item>*/}
          {/*</Col>*/}
          <Col span={colSpan}>
            <Form.Item
              label={t['userList.columns.createdTime']}
              field="createdTime"
            >
              <DatePicker.RangePicker
                  allowClear
                  style={{ width: '100%' }}
                  disabledDate={(date) => dayjs(date).isAfter(dayjs())}
              />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item label={t['userList.columns.department']} field="department">
              <TreeSelect
                  ref={treeRef}
                  placeholder={"Please select"}
                  multiple={true}
                  allowClear={true}
                  treeData={translateData([...props.departmentMap.values()],"0")}
                  onChange={(e,v) => {
                    if(!e || e.length == '0'){
                      console.log("---reset fields...." + stringifyObj(v))
                      console.log("ref:" + treeRef.current)
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
