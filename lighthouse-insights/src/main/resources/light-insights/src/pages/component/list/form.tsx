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

      {/*<div className={styles['right-button']}>*/}
      {/*  <Button type="primary" icon={<IconSearch />} onClick={handleSubmit}>*/}
      {/*    {t['componentList.form.button.search']}*/}
      {/*  </Button>*/}
      {/*  <Button icon={<IconRefresh />} onClick={handleReset}>*/}
      {/*    {t['componentList.form.button.reset']}*/}
      {/*  </Button>*/}
      {/*</div>*/}
    </div>
  );
}

export default SearchForm;
