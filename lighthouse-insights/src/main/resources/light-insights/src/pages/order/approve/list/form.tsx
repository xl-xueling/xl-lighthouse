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
              <Form.Item label={t['searchForm.username.label']} field="username">
                <Input allowClear />
              </Form.Item>
            </Col>
            <Col span={colSpan}>
              <Form.Item
                  label={t['searchForm.orderType.label']}
                  field="types"
              >
                <Select
                    mode="multiple"
                    allowClear
                >
                  <Select.Option value={1}>{t['approveList.columns.type.project.access']}</Select.Option>
                  <Select.Option value={2}>{t['approveList.columns.type.stat.access']}</Select.Option>
                  <Select.Option value={3}>{t['approveList.columns.type.metrics.access']}</Select.Option>
                  <Select.Option value={4}>{t['approveList.columns.type.adjust.limited.threshold']}</Select.Option>
                  <Select.Option value={5}>{t['approveList.columns.type.stat.pend.approve']}</Select.Option>
                  <Select.Option value={6}>{t['approveList.columns.type.user.pend.approve']}</Select.Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={colSpan}>
              <Form.Item
                  label={t['searchForm.state.label']}
                  field="states"
              >
                <Select
                    mode="multiple"
                    allowClear
                >
                  <Select.Option value={0}>{t['basic.orderState.description.processing']}</Select.Option>
                  <Select.Option value={2}>{t['basic.orderState.description.approved']}</Select.Option>
                  <Select.Option value={3}>{t['basic.orderState.description.rejected']}</Select.Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={colSpan}>
              <Form.Item
                  label={t['searchForm.createTime.label']}
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
