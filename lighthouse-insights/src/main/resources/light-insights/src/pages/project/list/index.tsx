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
import {useSelector} from "react-redux";
import {IconHome} from "@arco-design/web-react/icon";
import {GlobalState} from "@/store";
import ProjectListPanel from "@/pages/project/list/ProjectListPanel";
import ProjectCreatePanel from "@/pages/project/create";

const BreadcrumbItem = Breadcrumb.Item;

export default function Index() {
  const t = useLocale(locale);
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
  const [listData, setListData] = useState<Project[]>([]);
  const [owner, setOwner] = useState<number>(1);
  const [selectedProject,setSelectedProject] = useState<Project>(null);
  const [form] = useForm();
  const [createVisible, setCreateVisible] = React.useState(false);
  const [updateVisible, setUpdateVisible] = React.useState(false);
  const [detailVisible, setDetailVisible] = React.useState(false);
  const [bindedVisible,setBindedVisible] = React.useState(false);
  const [applyVisible,setApplyVisible] = React.useState(false);
  const [reloadTime,setReloadTime] = useState<number>(Date.now);
  const userInfo = useSelector((state: GlobalState) => state.userInfo);

  const tableCallback = async (record, type) => {
    if(type == 'update'){
      setSelectedProject(record);
      setUpdateVisible(!updateVisible);
    }else if(type == 'delete'){
      await handlerDeleteProject(record.id).then();
    }else if(type == 'binded'){
      setSelectedProject(record);
      await handlerBindedProject().then();
    }else if(type == 'detail'){
      setSelectedProject(record);
      setDetailVisible(!detailVisible);
    }else if(type == 'apply'){
      setSelectedProject(record);
      setApplyVisible(!applyVisible);
    }
  };

  const hideCreateModal = () => {
    setCreateVisible(false);
  };

  const hideUpdateModal = () => {
    setUpdateVisible(false);
  };

  const columns = useMemo(() => getColumns(t, tableCallback), [t,listData]);

  const [loading, setLoading] = useState(true);
  const [formParams, setFormParams] = useState<any>({});

  function handleSearch(params) {
    setFormParams(params);
  }

  function handleReset(){
    form.resetFields();
    handleSearch({});
  }

  function onClickRadio(p){
    setOwner(p);
    handleReset();
  }

  const handlerReloadList = () => {
    setReloadTime(Date.now);
  }

  const handlerBindedProject = async () => {
    setBindedVisible(true);
  };

  const handlerDeleteProject = async (id: number) => {
    await requestDeleteById({id}).then((response) => {
      const {code, data ,message} = response;
      if(code == '0'){
        Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.delete.submit.success']});
        const updatedList = listData.filter(x => x.id != id);
        setListData(updatedList);
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
    }).catch((error) => {
      console.log(error);
    })
  };

  return (
      <>
      <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
        <BreadcrumbItem>
            <IconHome />
        </BreadcrumbItem>
        <BreadcrumbItem style={{fontWeight:20}}>{t['projectList.breadcrumb.title']}</BreadcrumbItem>
      </Breadcrumb>
    <Card>
      <SearchForm onSearch={handleSearch} onClear={handleReset} allDepartInfo={allDepartInfo} form={form}/>
      <Grid.Row justify="space-between" align="center" style={{marginBottom:'15px'}}>
        <Grid.Col span={16} style={{ textAlign: 'left' }}>
          <Space>
            <Radio.Group defaultValue={"1"} name='button-radio-group' onChange={onClickRadio}>
              {[{value:"1",label:t['projectList.operations.my.projects']},{value:"0",label:t['projectList.operations.all.projects']}].map((item) => {
                return (
                    <Radio key={item.value} value={item.value}>
                      {({ checked }) => {
                        return (
                            <Button size={"small"} tabIndex={-1} key={item.value} shape='round' style={checked ? {color:'rgb(var(--primary-6)',fontWeight:500}:{fontWeight:500}}>
                              {item.label}
                            </Button>
                        );
                      }
                      }
                    </Radio>
                );
              })}
            </Radio.Group>
          </Space>
        </Grid.Col>
        <Grid.Col span={8} style={{ textAlign: 'right' }}>
          <Button size={"small"} type="primary" onClick={() => setCreateVisible(true)}>{t['projectList.operations.create.project']}</Button>
        </Grid.Col>
      </Grid.Row>
      <ProjectListPanel formParams={formParams}/>
      {createVisible && <ProjectCreatePanel allDepartInfo={allDepartInfo} onClose={() => setCreateVisible(false)} onSuccess={handlerReloadList}/>}
    </Card>
      </>
  );

}