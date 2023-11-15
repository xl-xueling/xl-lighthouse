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
import {stringifyMap, stringifyObj} from "@/utils/util";
import {NodeProps, TreeProps} from "@arco-design/web-react/es/Tree/interface";
import {Department, User} from "@/types/insights";
const { Title } = Typography;

function ProjectList() {
  const t = useLocale(locale);
  const tableCallback = async (record, type) => {
    console.log(record, type);
  };

  const [departmentData, setDepartmentData] = useState<Map<string,Department>>(new Map());

  const [userData, setUserData] = useState<Array<User>>([]);

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
    const promiseOfFetchDepartData:Promise<Map<string,Department>> = new Promise((resolve,reject) => {
      let result;
      if(departmentData.size == 0){
        const proc = async () => {
          return await fetchDepartData();
        }
        result = proc();
      }else{
        result = departmentData;
      }
      resolve(result);
    });

    const promiseOfFetchUserData:Promise<Array<User>> = new Promise((resolve,reject) => {
      const proc = async () => {
        return await fetchUserData();
      }
      resolve(proc());
    })

    const promiseAll:Promise<[Map<string,Department>,Array<User>]> = Promise.all([
         promiseOfFetchDepartData,
         promiseOfFetchUserData
    ]);

    promiseAll.then(([r1,r2]) => {
      const departmentMap = r1;
      const userList = r2;
      if(userList){
        userList.forEach(z => {
          const department = departmentMap.get(z.departmentId+ "");
          if(department){
            z.departmentName = department.name;
          }
        })
      }
      setUserData(userList);
    })
  }, [pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

  async function fetchDepartData(): Promise<Map<string,Department>> {
    const departmentMap = new Map<string,Department>();
    try {
      const a:any = await queryDepartmentAll().then((res:any) => {
        const {code, msg} = res;
        const data = res.data;
        if (code === '0' && data) {
          data.forEach(x => {
            departmentMap.set(x.id,x);
          })
          setDepartmentData(departmentMap);
        }else{
          Message.error("System Error,fetch department data failed!")
        }
      });
    } catch (error) {
      console.error("error is:" + error);
      Message.error("System Error,fetch department data failed!")
    }
    return departmentMap;
  }

  async function fetchUserData():Promise<Array<User>> {
    const {current, pageSize} = pagination;
    setLoading(true);
    let result;
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
          result = res.data.list;
        }else{
          Message.error("System Error,fetch data failed!")
        }
        const {current, pageSize} = pagination;
        setPagination({
          ...pagination,
          current,
          pageSize,
          total: res.data.total,
        })
      });
    } catch (error) {
      console.error("error is:" + error);
      Message.error("System Error,fetch data failed!")
    } finally {
      setLoading(false);
    }
    return result;
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
      <SearchForm departmentMap={departmentData} onSearch={handleSearch} />
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
        data={userData}
      />
    </Card>
  );
}

export default ProjectList;
