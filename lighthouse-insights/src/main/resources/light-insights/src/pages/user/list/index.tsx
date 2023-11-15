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

  const [initFlag, setInitFlag] = useState(false);

  const [departmentMap, setDepartmentMap] = useState(new Map());

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
  const columns = useMemo(() => getColumns(t,tableCallback), [t]);

  useEffect(() => {
    fetchAllDepartmentData();
  }, []);

  useEffect(() => {
    if(initFlag){
      fetchData();
    }
  }, [initFlag,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);



  function fetchAllDepartmentData() {
    try {
      const a:any = queryDepartmentAll().then((res:any) => {
        const {code, msg} = res;
        const data = res.data;
        if (code === '0' && data) {
          const departmentMap = new Map();
          data.forEach(x => {
            departmentMap.set(x.id,x);
          })
          setDepartmentMap(departmentMap);
          setInitFlag(true);
        }else{
          Message.error("System Error,fetch department data failed!")
        }
      });
    } catch (error) {
      console.error("error is:" + error);
      Message.error("System Error,fetch department data failed!")
    }finally {
    }
  }

  function translateTableData(data){
    if(data){
      data.forEach(z => {
        const department = departmentMap.get(z.departmentId+ "");
        if(department){
          z.departmentName = department.name;
        }
      })
    }
    return data;
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
        const {code, data ,msg} = res;
        if (code === '0') {
          setData(translateTableData(res.data.list));
          setPagination({
            ...pagination,
            current,
            pageSize,
            total: res.data.total,
          });
        }else{
          Message.error("System Error,fetch data failed!")
        }
      });
    } catch (error) {
      console.error("error is:" + error);
      Message.error("System Error,fetch data failed!")
    } finally {
      setLoading(false);
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
