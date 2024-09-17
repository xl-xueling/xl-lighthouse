import React, {useEffect, useMemo, useState} from 'react';
import {Breadcrumb, Card, Notification, PaginationProps, Table, Typography,} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import {getColumns} from './constants';
import {requestChangeState, requestDeleteById, requestList, requestResetPasswd} from "@/api/user";
import {Department, User} from "@/types/insights-web";
import {useSelector} from "react-redux";
import {ResultData, UserStateEnum} from "@/types/insights-common";
import SearchForm from "@/pages/user/list/form";
import {IconHome} from "@arco-design/web-react/icon";
import ErrorPage from "@/pages/common/error";
import {GlobalErrorCodes} from "@/utils/constants";

const { Title } = Typography;

export default function UserList() {

  const t = useLocale(locale);
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  const [errorCode,setErrorCode] = useState<string>(null);
  const [userData, setUserData] = useState<Array<User>>([]);

  const tableCallback = async (record, type) => {
    if(type == 'resetPasswd'){
      await resetPasswd(record.id);
    }else if(type == 'delete'){
      await deleteUser(record.id);
    }else if(type == 'frozen'){
      await changeState(record.id,UserStateEnum.USER_FROZEN);
    }else if(type == 'activation'){
      await changeState(record.id,UserStateEnum.USR_NORMAL);
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
  const columns = useMemo(() => getColumns(t,tableCallback), [t,userData]);

  const resetPasswd = async (userId: number) => {
    await requestResetPasswd({id:userId}).then((response) => {
      const {code, data ,message} = response;
      if(code == '0'){
        Notification.info({style: { width: 420 }, title: 'Notification', content: t['userList.columns.resetPasswd.success']});
      }else if(GlobalErrorCodes.includes(String(code))){
        setErrorCode(code);
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
    }).catch((error)=>{
      console.log(error);
    })
  };

  const changeState = async (userId: number,state:UserStateEnum) => {
    await requestChangeState({"id":userId,"state":state}).then((response) => {
      const {code, data ,message} = response;
      if(code == '0'){
        let message;
        if(state == UserStateEnum.USER_FROZEN){
          message = t['userList.columns.frozen.success'];
        }else if(state == UserStateEnum.USR_NORMAL){
          message = t['userList.columns.activation.success'];
        }else if(state == UserStateEnum.USER_REJECT){
          message = t['userList.columns.activation.success'];
        }
        Notification.info({style: { width: 420 }, title: 'Notification', content: message});
        const updatedUsers = userData.map((user) => user.id == userId ? { ...user, state: state } : user);
        setUserData(updatedUsers);
      }else if(GlobalErrorCodes.includes(String(code))){
        setErrorCode(code);
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
    }).catch((error) => {
      console.log(error);
    })
  };

  const deleteUser = async (userId: number) => {
    await requestDeleteById({"id":userId}).then((response) => {
      const {code, data ,message} = response;
      if(code == '0'){
        Notification.info({style: { width: 420 }, title: 'Notification', content: t['userList.columns.delete.success']});
        setTimeout(() => {
          handleSearch({...formParams,t:Date.now()});
        },1000)
      }else if(GlobalErrorCodes.includes(String(code))){
        setErrorCode(code);
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
      }).catch((error) => {
        console.log(error);
      })
  };

  function handleSearch(params) {
    setFormParams(params);
    setPagination({ ...pagination, current: 1 });
  }

  useEffect(() => {
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
            }else if(GlobalErrorCodes.includes(String(code))){
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
  }, [pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


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

