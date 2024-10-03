import React, {useEffect, useMemo, useState} from 'react';
import {
  Breadcrumb,
  Button,
  Card,
  Grid,
  Message,
  Notification,
  PaginationProps,
  Radio,
  Space,
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import {getColumns} from './constants';
import {requestDeleteById, requestList} from "@/api/project";
import {Project, TreeNode} from "@/types/insights-web";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useDispatch, useSelector} from "react-redux";
import {IconHome} from "@arco-design/web-react/icon";
import {GlobalState} from "@/store";
import ProjectListPanel from "@/pages/project/list/ProjectListPanel";
import ProjectCreatePanel from "@/pages/project/create";
import ProjectList from "@/pages/project/list/list";
import KeepAlive from "react-activation";

const BreadcrumbItem = Breadcrumb.Item;

export default function Index() {
  const t = useLocale(locale);
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
  const [listData, setListData] = useState<Project[]>([]);
  const [selectedProject,setSelectedProject] = useState<Project>(null);
  const [form] = useForm();
  const [createVisible, setCreateVisible] = React.useState(false);
  const [updateVisible, setUpdateVisible] = React.useState(false);
  const [detailVisible, setDetailVisible] = React.useState(false);
  const [bindedVisible,setBindedVisible] = React.useState(false);
  const [applyVisible,setApplyVisible] = React.useState(false);
  const [reloadTime,setReloadTime] = useState<number>(Date.now);
  const userInfo = useSelector((state: GlobalState) => state.userInfo);
  const [owner,setOwner] = useState(1);
  const dispatch = useDispatch();
  const [formParams, setFormParams] = useState<any>({ownerId:userInfo.id});

  const hideCreateModal = () => {
    setCreateVisible(false);
  };

  const hideUpdateModal = () => {
    setUpdateVisible(false);
  };

  const [loading, setLoading] = useState(true);

  function handleSearch(params) {
    setFormParams({...params,t:Date.now()});
  }

  function handleReset(){
    form.resetFields();
    handleSearch({})
  }

  function handleChangeOwnerType(p){
    setOwner(p);
  }

  const handlerReloadList = () => {
    setFormParams({...formParams,t:Date.now()});
  }

  return (
      <>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
          <BreadcrumbItem>
            <IconHome />
          </BreadcrumbItem>
          <BreadcrumbItem style={{fontWeight:20}}>{t['projectList.breadcrumb.title']}</BreadcrumbItem>
        </Breadcrumb>
        <KeepAlive name="ProjectListKeepAlive" cacheKey={"ProjectListKeepAlive"} id={"ProjectListKeepAlive"} when={true} autoFreeze={true}>
          <ProjectList />
        </KeepAlive>
      </>
  );

}