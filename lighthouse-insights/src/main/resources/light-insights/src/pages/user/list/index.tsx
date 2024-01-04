import React, {useEffect, useMemo, useState} from 'react';
import {Breadcrumb, Card, Message, Notification, PaginationProps, Table, Typography,} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import {getColumns} from './constants';
import {requestChangeState, requestDeleteById, requestList, requestResetPasswd} from "@/api/user";
import {Department, User} from "@/types/insights-web";
import {useSelector} from "react-redux";
import {ResultData} from "@/types/insights-common";
import SearchForm from "@/pages/user/list/form";
import {IconHome} from "@arco-design/web-react/icon";
import { useHistory } from 'react-router-dom';
import {handleWarningCode} from "@/pages/common/prompt";
import ErrorPage from "@/pages/common/error";
import {GlobalErrorCodes} from "@/utils/constants";

const { Title } = Typography;

export default function UserList() {

  const t = useLocale(locale);
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  const [initReady,setInitReady] = useState<boolean>(false);
  const [errorCode,setErrorCode] = useState<string>(null);
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
    sizeOptions: [15,20,30,50],
    sizeCanChange: true,
    showTotal: true,
    pageSize: 15,
    current: 1,
    pageSizeChangeResetCurrent: true,
  });

  const [loading, setLoading] = useState(true);
  const [formParams, setFormParams] = useState({});
  const columns = useMemo(() => getColumns(t,tableCallback), [t]);

  const resetPasswd = async (userId: string) => {
    await requestResetPasswd({id:userId}).then((response) => {
      const {code, data ,message} = response;
      if(code == '0'){
        Notification.info({style: { width: 420 }, title: 'Notification', content: t['userList.columns.resetPasswd.success']});
      }else if(GlobalErrorCodes.includes(code)){
        setErrorCode(code);
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
    }).catch((error)=>{
      console.log(error);
    })
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

  function handleSearch(params) {
    setFormParams(params);
    setPagination({ ...pagination, current: 1 });
  }


  useEffect(() => {
    if(allDepartInfo && allDepartInfo.length > 0){
      setInitReady(true);
    }
  },[allDepartInfo])

  const history = useHistory();

  useEffect(() => {
    if(!initReady){
      return;
    }
    const promiseOfFetchUserData:Promise<Array<User>> = new Promise((resolve,reject) => {
      setLoading(true);
      const proc = async () => {
          let result = [];
          const {current, pageSize} = pagination;
          await requestList({
            queryParams:formParams,
            pagination:{
              pageSize:pageSize,
              pageNum:current,
            }
          }).then((response:ResultData) => {
            const {code, data ,message} = response;
            if (code === '0') {
              result = data.list;
              const {current, pageSize} = pagination;
              setPagination({
                ...pagination,
                current,
                pageSize,
                total: data.total,
              })
              resolve(result);
            }else if(GlobalErrorCodes.includes(code)){
              setErrorCode(code);
            }else{
              Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
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

  return (
      <>
        {errorCode ? <ErrorPage errorCode={errorCode}/>
          :
            <>
              <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <Breadcrumb.Item>
                  <IconHome />
                </Breadcrumb.Item>
                <Breadcrumb.Item style={{fontWeight:20}}>{t['userList.breadcrumb.title']}</Breadcrumb.Item>
              </Breadcrumb>
              <Card>
              <SearchForm onSearch={handleSearch} />
                <Table
                rowKey="id"
                size={"small"}
                loading={loading}
                onChange={onChangeTable}
                pagination={pagination}
                columns={columns}
                data={userData}
                />
          </Card>
            </>
        }
      </>
  );
}

