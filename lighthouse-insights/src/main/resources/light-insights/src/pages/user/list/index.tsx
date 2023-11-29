import React, { useState, useEffect, useMemo } from 'react';
import {
  Table,
  Card,
  PaginationProps,
  Typography, Message, List,
} from '@arco-design/web-react';
import PermissionWrapper from '@/components/PermissionWrapper';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import { getColumns } from './constants';
import {requestList, requestChangeState, requestDeleteById, requestResetPasswd} from "@/api/user";
import {Department, User} from "@/types/insights-web";
import {useSelector} from "react-redux";
import {translateToMapStruct} from "@/pages/department/common";
import {ResultData} from "@/types/insights-common";
const { Title } = Typography;

export default function UserList() {

  const t = useLocale(locale);
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  const [initReady,setInitReady] = useState<boolean>(false);
  const [userData, setUserData] = useState<Array<User>>([]);

  const tableCallback = async (record, type) => {
    if(type == 'resetPasswd'){
      await resetPasswd(record.id);
    }else if(type == 'delete'){
      await deleteUser(record.id);
    }else if(type == 'frozen'){
      await frozenUser(record.id);
    }
  };

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

  const resetPasswd = async (userId: string) => {
    try{
      const result = await requestResetPasswd(userId);
      if(result.code == '0'){
          Message.success(t['userList.columns.resetPasswd.success']);
      }else{
          Message.error(result.message || t['system.error']);
      }
    }catch (error){
      console.log(error);
      Message.error(t['system.error']);
    }
  };

  const frozenUser = async (userId: string) => {
    try{
      const result = await requestChangeState({"id":userId,"state":3});
      if(result.code == '0'){
        Message.success(t['userList.columns.frozen.success']);
      }else{
        Message.error(result.message || t['system.error']);
      }
    }catch (error){
      console.log(error);
      Message.error(t['system.error']);
    }
  };

  const deleteUser = async (userId: number) => {
    try{
      const result = await requestDeleteById(userId);
      if(result.code == '0'){
        Message.success(t['userList.columns.delete.success']);
      }else{
        Message.error(result.message || t['system.error']);
      }
    }catch (error){
      console.log(error);
      Message.error(t['system.error']);
    }
  };

  useEffect(() => {
    if(allDepartInfo && allDepartInfo.length > 0){
      setInitReady(true);
    }
  },[allDepartInfo])

  useEffect(() => {
    if(!initReady){
      return;
    }
    const departmentMap = translateToMapStruct(allDepartInfo);
    const promiseOfFetchUserData:Promise<Array<User>> = new Promise((resolve,reject) => {
      setLoading(true);
      const proc = async () => {
          let result = [];
          const {current, pageSize} = pagination;
          await requestList({
            params: {
              page: current,
              pageSize,
              ...formParams,
            },
          }).then((response:ResultData) => {
            const {code, data ,message} = response;
            if (code === '0') {
              result = data.list;
            }else{
              Message.error(message || t['system.error']);
            }
            const {current, pageSize} = pagination;
            setPagination({
              ...pagination,
              current,
              pageSize,
              total: data.total,
            })
            resolve(result);
          }).catch(error => {
            console.log(error);
          }).finally(() => {
            setLoading(false);
          })
      }
      proc().then();
    })

    const promiseAll:Promise<[Array<User>]> = Promise.all([
         promiseOfFetchUserData
    ]);

    promiseAll.then(([result]) => {
      result?.forEach(z => {
        const department = departmentMap.get(String(z.departmentId));
        if(department){
          z.departmentName = department.name;
        }
      })
      setUserData(result);
    })
  }, [initReady,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


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
      {/*<SearchForm departmentMap={[]} onSearch={handleSearch} />*/}
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

