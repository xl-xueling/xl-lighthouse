import React, { useState, useEffect, useMemo } from 'react';
import {
  Table,
  Card,
  PaginationProps,
  Button,
  Space,
  Typography,
} from '@arco-design/web-react';
import PermissionWrapper from '@/components/PermissionWrapper';
import { IconDownload, IconPlus } from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import styles from './style/index.module.less';
import '../mock';
import { getColumns } from './constants';
import { queryList } from "@/api/project";
const { Title } = Typography;

function ProjectList() {
  const t = useLocale(locale);
  const tableCallback = async (record, type) => {
    console.log("record is:" + JSON.stringify(record));
    console.log(record, type);
  };
  const columns = useMemo(() => getColumns(t, tableCallback), [t]);
  console.log("columns:" + JSON.stringify(columns))
  const [data, setData] = useState([]);
  const [pagination, setPagination] = useState<PaginationProps>({
    sizeCanChange: true,
    showTotal: true,
    pageSize: 10,
    current: 1,
    pageSizeChangeResetCurrent: true,
  });
  const [loading, setLoading] = useState(true);
  const [formParams, setFormParams] = useState({});

  useEffect(() => {
    console.log("current:" + pagination.current + ",page size:" + pagination.pageSize)
    fetchData();
  }, [pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

  async function fetchData() {
    const {current, pageSize} = pagination;
    setLoading(true);
    try {
      const a:any = await queryList({
        params: {
          page: current,
          pageSize,
          ...formParams,
        },
      }).then((res:any) => {
        setData(res.data.list);
        setPagination({
          ...pagination,
          current,
          pageSize,
          total: res.data.total,
        });
        setLoading(false);
      });
    } catch (error) {
      console.error("error is:" + error);
    }
  }

  function onChangeTable({ current, pageSize }) {
    setPagination({
      ...pagination,
      current,
      pageSize,
    });
  }

  function handleSearch(params) {
    setPagination({ ...pagination, current: 1 });
    setFormParams(params);
  }

  return (
    <Card>
      <Title heading={6}>{t['menu.list.searchTable']}</Title>
      <SearchForm onSearch={handleSearch} />
      <PermissionWrapper
        requiredPermissions={[
          { resource: 'menu.list.searchTable', actions: ['write'] },
        ]}
      >
        <div className={styles['button-group']}>
          <Space>
            <Button type="primary" icon={<IconPlus />}>
              {t['searchTable.operations.add']}
            </Button>
            <Button>{t['searchTable.operations.upload']}</Button>
          </Space>
          <Space>
            <Button icon={<IconDownload />}>
              {t['searchTable.operation.download']}
            </Button>
          </Space>
        </div>
      </PermissionWrapper>
      <Table
        rowKey="id"
        loading={loading}
        onChange={onChangeTable}
        pagination={pagination}
        columns={columns}
        data={data}
      />
    </Card>
  );
}

export default ProjectList;
