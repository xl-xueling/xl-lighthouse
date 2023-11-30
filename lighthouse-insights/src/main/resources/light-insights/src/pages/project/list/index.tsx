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
import {Department, PrivilegeEnum, Project, ProjectPagination} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useSelector} from "react-redux";
import ProjectCreate from "@/pages/project/list/create";
import ProjectUpdate from "@/pages/project/list/update";
import {requestDeleteById} from "@/api/project";
import {requestQueryProjectIds} from "@/api/favorites";

const { Title } = Typography;

export default function Index() {
  const t = useLocale(locale);

  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  const [listData, setListData] = useState([]);
  const [initReady,setInitReady] = useState<boolean>(false);
  const [favoriteIds,setFavoriteIds] = useState([]);

  const fetchFavoritesData = useCallback(async () => {
    try {
      const response = await requestQueryProjectIds();
      const data = response.data;
      setFavoriteIds(data);
      setTimeout(() => {
        setInitReady(true);
      },0)
    } catch (error) {
      console.error(error);
    }
  }, []);

  useEffect(() => {
    fetchFavoritesData().then();
  },[fetchFavoritesData])

  const [owner, setOwner] = useState(true);
  const [updateId, setUpdateId] = useState(0);
  const [form] = useForm();
  const [createVisible, setCreateVisible] = React.useState(false);
  const [updateVisible, setUpdateVisible] = React.useState(false);

  const tableCallback = async (record, type) => {
    if(type == 'update'){
      setUpdateVisible(!updateVisible);
      setUpdateId(record.id);
    }else if(type == 'delete'){
      await handlerDeleteProject(record.id).then();
    }
  };

  const columns = useMemo(() => getColumns(t, tableCallback), [t]);
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

  const hideCreateModal = () => {
    setCreateVisible(false);
  };

  const hideUpdateModal = () => {
    setUpdateVisible(false);
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
    setLoading(true);
    if(!initReady){
      return;
    }
    fetchData().then().catch(error => {
      console.log(error);
      Message.error(t['system.error']);
    }).finally(() => {
      setLoading(false);
    })
  }, [initReady,owner,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


  const fetchData = async (): Promise<void> => {
    const {current, pageSize} = pagination;
    const fetchProjectsInfo:Promise<{list:Array<Project>,total:number}> = new Promise<{list:Array<Project>,total:number}>((resolve) => {
      const proc = async () => {
        const result = await requestList({
          params: {
            page: current,
            pageSize,
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
      const projectIds = projectsInfo!.map(z => z.id);
      const proc = async () => {
        const result:ResultData<Record<number,PrivilegeEnum[]>> = await requestPrivilegeCheck({type:"project",items:projectIds});
        resolve(result.data);
      }
      proc().then();
    })
    Promise.all([fetchPrivilegeInfo])
        .then(([r1]) => {
          const paginationData = projectsInfo.reduce((result:ProjectPagination[],item:Project) => {
            const department = allDepartInfo.find(x => String(x.id) == String(item.departmentId));
            const combinedItem = { ...item, ...{"key":item.id,"permissions":r1[item.id],"department":department}};
            result.push(combinedItem);
            return result;
          },[]);
          setListData(paginationData);
          setPagination({
            ...pagination,
            current,
            pageSize,
            total: total});
        })
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

  function handleReset(){
    form.resetFields();
    handleSearch({});
  }

  function onClickRadio(p){
    setOwner(p==1);
    handleReset();
  }


  return (
    <Card>
      <SearchForm onSearch={handleSearch} onClear={handleReset} form={form}/>
      <Grid.Row justify="space-between" align="center">
        <Grid.Col span={16} style={{ textAlign: 'left' }}>
          <Space>
            <Radio.Group defaultValue={'0'} name='button-radio-group' onChange={onClickRadio}>
              {[{value:"0",label:"全部工程"}, {value:"1",label:"我的工程"}].map((item) => {
                return (
                    <Radio key={item.value} value={item.value}>
                      {({ checked }) => {
                        return (
                            <Button size={"small"} tabIndex={-1} key={item.value} shape='round'
                              style={checked ? {color:'rgb(var(--primary-6)',fontWeight:500}:{fontWeight:500}}>
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
      <ProjectCreate createVisible={createVisible} onHide={hideCreateModal} />
      <ProjectUpdate updateId={updateId} updateVisible={updateVisible} onHide={hideUpdateModal}/>
    </Card>
  );

}