import React, { useState, useEffect, useMemo } from 'react';
import {
  Table,
  Card,
  PaginationProps,
  Button,
  Space,
  Typography, Message,
} from '@arco-design/web-react';
import PermissionWrapper from '@/components/PermissionWrapper';
import { IconDownload, IconPlus } from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import styles from './style/index.module.less';
import '../../department/mock';
import { getColumns } from './constants';
import { queryList } from "@/api/user";
import {queryAll as queryDepartmentAll} from "@/api/department";
import {stringifyMap} from "@/utils/util";
const { Title } = Typography;

function ProjectList() {
  const t = useLocale(locale);
  const tableCallback = async (record, type) => {
    console.log(record, type);
  };
  const columns = useMemo(() => getColumns(t, tableCallback), [t]);
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

  const [departmentMap, setDepartmentMap] = useState(new Map());

  useEffect(() => {
    fetchData();
  }, [pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


  useEffect(() => {
    fetchAllDepartmentData();
  }, []);

  async function fetchAllDepartmentData() {
    setLoading(true);
    try {
      const a:any = await queryDepartmentAll().then((res:any) => {
        const {code, msg} = res;
        const data = res.data;
        if (code === '0' && data) {
          const departmentMap = new Map();
          data.forEach(x => {
            departmentMap.set(x.id,x);
          })
          setDepartmentMap(departmentMap);
        }else{
          Message.error("System Error,fetch department data failed!")
        }
      });
    } catch (error) {
      console.error("error is:" + error);
    }finally {
      setLoading(false);
    }
  }

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
    console.log("search params is:" + JSON.stringify(params));
    setPagination({ ...pagination, current: 1 });
    setFormParams(params);
  }

  return (
    <Card>
      <SearchForm departmentMap={departmentMap} onSearch={handleSearch} />
      <PermissionWrapper
        requiredPermissions={[
          { resource: 'menu.list.searchTable', actions: ['write'] },
        ]}
      >
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
