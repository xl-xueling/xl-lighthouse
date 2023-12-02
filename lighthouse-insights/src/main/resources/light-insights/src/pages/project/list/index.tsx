import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {
  Radio,
  Button,
  Card,
  Grid,
  PaginationProps,
  Space,
  Table,
  Typography,
  Message,
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import {getColumns} from './constants';
import {requestList} from "@/api/project";
import {ResultData} from "@/types/insights-common";
import {Department, PrivilegeEnum, Project, ProjectPagination, User} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useSelector} from "react-redux";
import ProjectCreatePanel from "@/pages/project/create";
import ProjectUpdatePanel from "@/pages/project/update";
import {requestDeleteById} from "@/api/project";
import {requestFavoriteProject, requestQueryProjectIds, requestUnFavoriteProject} from "@/api/favorites";
import Detail from "@/pages/project/list/detail";
import {requestQueryByIds} from "@/api/user";
import {stringifyMap, stringifyObj} from "@/utils/util";

const { Title } = Typography;

export default function Index() {
  const t = useLocale(locale);
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  const [listData, setListData] = useState([]);
  const [favoriteIds,setFavoriteIds] = useState<Array<number>>([]);
  const fetchFavoritesData = useCallback(async () => {
    try {
      const response = await requestQueryProjectIds();
      const data = response.data;
      setFavoriteIds(data);
    } catch (error) {
      console.error(error);
    }
  }, []);

  useEffect(() => {
    fetchFavoritesData().then();
  },[fetchFavoritesData])

  const [owner, setOwner] = useState(true);
  const [updateId, setUpdateId] = useState(0);
  const [selectedProject,setSelectedProject] = useState<ProjectPagination>(null);
  const [form] = useForm();
  const [createVisible, setCreateVisible] = React.useState(false);
  const [updateVisible, setUpdateVisible] = React.useState(false);
  const [detailVisible, setDetailVisible] = React.useState(false);

  const tableCallback = async (record, type) => {
    if(type == 'update'){
      setUpdateVisible(!updateVisible);
      setUpdateId(record.id);
    }else if(type == 'delete'){
      await handlerDeleteProject(record.id).then();
    }else if(type == 'favorite'){
      await handlerFavoriteProject(record.id).then();
    }else if(type == 'unFavorite'){
      await handlerUnFavoriteProject(record.id).then();
    }else if(type == 'detail'){
      setSelectedProject(record);
      setDetailVisible(!detailVisible);
    }
  };

  const hideCreateModal = () => {
    setCreateVisible(false);
  };

  const hideUpdateModal = () => {
    setUpdateVisible(false);
  };

  const columns = useMemo(() => getColumns(t,favoriteIds, tableCallback), [t,favoriteIds]);
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

  function handleReset(){
    form.resetFields();
    handleSearch({});
  }

  function onClickRadio(p){
    setOwner(p==1);
    handleReset();
  }

  const handlerFavoriteProject = async (id: number) => {
    try{
      const result = await requestFavoriteProject(id);
      if(result.code == '0'){
        Message.success("收藏工程成功！");
        setFavoriteIds([...favoriteIds,id]);
      }else{
        Message.error(result.message || "System Error!");
      }
    }catch (error){
      console.log(error);
      Message.error("System Error!");
    }
  };

  const handlerUnFavoriteProject = async (id: number) => {
    try{
      const result = await requestUnFavoriteProject(id);
      if(result.code == '0'){
        Message.success("已取消收藏该工程！");
        const newArr = favoriteIds.filter((item) => item !== id);
        setFavoriteIds(newArr);
      }else{
        Message.error(result.message || "System Error!");
      }
    }catch (error){
      console.log(error);
      Message.error("System Error!");
    }
  };

  const handlerDeleteProject = async (id: number) => {
    try{
      const result = await requestDeleteById(id);
      if(result.code == '0'){
        Message.success("删除工程成功！");
      }else{
        Message.error(result.message || "System Error!");
      }
    }catch (error){
      console.log(error);
      Message.error("System Error!");
    }
  };


  useEffect(() => {
    fetchData().then().catch(error => {
      console.log(error);
      Message.error(t['system.error']);
    })
  }, [owner,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


  const fetchData = async (): Promise<void> => {
    setLoading(true);
    const {current, pageSize} = pagination;
    const fetchProjectsInfo:Promise<{list:Array<Project>,total:number}> = new Promise<{list:Array<Project>,total:number}>((resolve) => {
      const proc = async () => {
        const result = await requestList({
          params: {
            page: current,
            pageSize,
            owner:owner?1:0,
            ...formParams,
          },
        });
        resolve(result.data);
      }
      proc().then();
    })

    const result = await Promise.all([fetchProjectsInfo]);
    const {list,total}:{list:Array<Project>,total:number} = result[0];
    const projectsInfo = list;
    const fetchPrivilegeInfo:Promise<Record<number,PrivilegeEnum[]>> = new Promise<Record<number,PrivilegeEnum[]>>((resolve) => {
      const projectIds = projectsInfo?.map(z => z.id);
      const proc = async () => {
        const result:ResultData<Record<number,PrivilegeEnum[]>> = await requestPrivilegeCheck({type:"project",items:projectIds});
        resolve(result.data);
      }
      proc().then();
    })
    const fetchUsersInfo:Promise<Record<number,User>> = new Promise<Record<number,User>>((resolve) => {
      const userIds = Array.from(new Set(projectsInfo?.map(z => z.adminIds).flatMap(z => z)));
      const proc = async () => {
        const result:ResultData<Record<number,User>> = await requestQueryByIds(userIds);
        resolve(result.data)
      }
      proc().then();
    })
    Promise.all([fetchPrivilegeInfo,fetchUsersInfo])
        .then(([r1,r2]) => {
          const paginationData = projectsInfo.reduce((result:ProjectPagination[],item:Project) => {
            console.log("item:" + JSON.stringify(item));
            const department = allDepartInfo.find(x => String(x.id) == String(item.departmentId));
            const users = r2 as Record<number,User>;
            const admins = Object.entries(users).map(([k,v]) => v).filter((user) => item.adminIds?.includes(Number(user.id)));
            const combinedItem = { ...item, ...{"key":item.id,"permissions":r1[item.id],"department":department,"admins":admins}};
            result.push(combinedItem);
            return result;
          },[]);
          setListData(paginationData);
          setPagination({
            ...pagination,
            current,
            pageSize,
            total: total});
          setLoading(false);
        })
  }


  return (
    <Card>
      <SearchForm onSearch={handleSearch} onClear={handleReset} allDepartInfo={allDepartInfo} form={form}/>
      <Grid.Row justify="space-between" align="center">
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
          <Space>
            <Button size={"small"} type="primary" onClick={() => setCreateVisible(true)}>创建</Button>
          </Space>
        </Grid.Col>
      </Grid.Row>

      <Table
          rowKey="id"
          style={{ marginTop:12}}
          size={"default"}
          loading={loading}
          onChange={onChangeTable}
          pagination={pagination}
          columns={columns}
          data={listData}
      />
      {createVisible && <ProjectCreatePanel allDepartInfo={allDepartInfo} onClose={() => setCreateVisible(false)} />}
      {updateVisible && <ProjectUpdatePanel updateId={updateId} allDepartInfo={allDepartInfo} onClose={() => setUpdateVisible(false)}/>}
      {detailVisible && <Detail projectInfo={selectedProject} onClose={() => setDetailVisible(false)}/>}
    </Card>
  );

}