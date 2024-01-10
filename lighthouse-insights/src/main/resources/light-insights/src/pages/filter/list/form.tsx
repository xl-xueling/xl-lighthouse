import React, { useContext } from 'react';
import dayjs from 'dayjs';
import {
  Form,
  Input,
  Select,
  DatePicker,
  Button,
  Grid,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';

const { Row, Col } = Grid;
const { useForm } = Form;

function SearchForm(props: {
  onSearch: (values: Record<string, any>) => void;
}):any {
  const { lang } = useContext(GlobalContext);

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
            <Form.Item label={'UserName'} field="username">
              <Input
                allowClear
                placeholder={t['searchForm.name.placeholder']}
              />
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item
                label={'OrderType'}
                field="orderType"
            >
              <Select
                  placeholder={t['userList.state.placeholder']}
                  mode="multiple"
                  allowClear
              >
                <Select.Option value={0}>PENDING</Select.Option>
                <Select.Option value={1}>NORMAL</Select.Option>
                <Select.Option value={2}>APPROVED</Select.Option>
              </Select>
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item
                label={'State'}
                field="states"
            >
              <Select
                  placeholder={t['userList.state.placeholder']}
                  mode="multiple"
                  allowClear
              >
                <Select.Option value={0}>PENDING</Select.Option>
                <Select.Option value={1}>NORMAL</Select.Option>
                <Select.Option value={2}>APPROVED</Select.Option>
              </Select>
            </Form.Item>
          </Col>
          <Col span={colSpan}>
            <Form.Item
              label={'CreateTime'}
              field="createTime"
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
          {'搜索'}
        </Button>
        <Button icon={<IconRefresh />} onClick={handleReset}>
          {'重置'}
        </Button>
      </div>
    </div>
  );
}

export default SearchForm;
