import React, {useEffect, useMemo, useState} from 'react';
import {
  Radio,
  Button,
  Card,
  Grid,
  PaginationProps,
  Space,
  Table,
  Tabs,
  Typography,
  Steps, Message,
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import {getColumns} from './constants';
import {requestList} from "@/api/project";
import {ResultData} from "@/types/insights-common";
import {Department, Project, ProjectPagination} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useSelector} from "react-redux";
import ProjectCreate from "@/pages/project/list/create";
import ProjectUpdate from "@/pages/project/list/update";
import {requestDeleteById} from "@/api/project";

const { Title } = Typography;

function ProjectList() {
  const t = useLocale(locale);
  const tableCallback = async (record, type) => {
    if(type == 'update'){
      setUpdateVisible(!updateVisible);
      setUpdateId(record.id);
    }else if(type == 'delete'){
      await handlerDeleteProject(record.id).then();
    }
    console.log(record, type);
  };
  const columns = useMemo(() => getColumns(t, tableCallback), [t]);
  const [data, setData] = useState([]);
  const [owner, setOwner] = useState(true);
  const [updateId, setUpdateId] = useState(0);
  const [form] = useForm();
  const [form2] = useForm();
  const Step = Steps.Step;
  const [createVisible, setCreateVisible] = React.useState(false);
  const [updateVisible, setUpdateVisible] = React.useState(false);
  const [disabled, setDisabled] = React.useState(true);
  const [pagination, setPagination] = useState<PaginationProps>({
    sizeCanChange: true,
    showTotal: true,
    pageSize: 15,
    current: 1,
    pageSizeChangeResetCurrent: true,
  });
  const [loading, setLoading] = useState(true);
  const [formParams, setFormParams] = useState({});
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  useEffect(() => {
    setLoading(true);
    fetchData().then().catch(error => {
      console.log("error:" + error)
    }).finally(() => {
      setLoading(false);
    })
  }, [owner,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


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
      console.log("error is:" + error);
      Message.error("System Error!");
    }
  };

  const fetchProjectsData = async ():Promise<ResultData<{list:Array<Project>,total:number}>> => {
    return new Promise((resolve) => {
       const proc = async () => {
         const {current, pageSize} = pagination;
         const result =  await requestList({
           params: {
             page: current,
             pageSize,
             ...formParams,
             owner:owner?1:0,
           },
         });
         setPagination({
           ...pagination,
           current,
           pageSize,
           total: result.data.total,});
         resolve(result);
       }
       proc();
    });
  }

  const fetchPrivilegeData = async ({type,items}):Promise<ResultData> => {
    return new Promise((resolve) => {
      const proc = async () => {
        const result = await requestPrivilegeCheck({type:type,items:items});
        resolve(result);
      }
      proc();
    })
  }

  const combineListData = (p1:Array<Project>,p2:Record<string, Array<number>>) => {
    return  p1.reduce((result:ProjectPagination[],item:Project) => {
      const combinedItem = { ...item, ...{"permissions":p2[item.id]} };
      result.push(combinedItem);
      return result;
    },[])
  }

  const fetchData = async (): Promise<void> => {
    const projectData = await fetchProjectsData();
    const projectIds = projectData.data.list!.map(z => z.id);
    const privilegeData = await fetchPrivilegeData({type:"project",items:projectIds});
    const listData = combineListData(projectData.data.list,privilegeData.data);
    setData(listData);
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

  function createProjectSubmit(){
    const values = form2.getFieldsValue();
    console.log("form2 values is:" + JSON.stringify(values));
  }

  const components = {
    header: {
      operations: ({ selectionNode, expandNode }) => [
        {
          node: (
              <th>
                <div className='arco-table-th-item'>Index</div>
              </th>
          ),
          width: 40,
        },
        {
          name: 'selectionNode',
          node: selectionNode,
        },
        {
          name: 'expandNode',
          node: expandNode,
        },
      ],
    },
    body: {
      operations: ({ selectionNode, expandNode }) => [
        {
          node: (record) => <td>{record.key}</td>,
          width: 40,
        },
        {
          name: 'selectionNode',
          node: selectionNode,
        },
        {
          name: 'expandNode',
          node: expandNode,
        },
      ],
    },
  };

  const [selectedRowKeys, setSelectedRowKeys] = useState(['4']);

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
                            <Button size={"mini"} tabIndex={-1} key={item.value} shape='round'
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
          data={data}
      />
      <ProjectCreate createVisible={createVisible} onHide={hideCreateModal} />
      <ProjectUpdate updateId={updateId} updateVisible={updateVisible} onHide={hideUpdateModal}/>
    </Card>
  );

}

export default ProjectList;
