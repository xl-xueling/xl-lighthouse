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
  Modal, Divider, Steps, AutoComplete, Select, Cascader, Form, Input, InputNumber, TreeSelect, Switch,
} from '@arco-design/web-react';
import PermissionWrapper from '@/components/PermissionWrapper';
import {
  IconCheck,
  IconClose,
  IconDown,
  IconDownload,
  IconPlus,
  IconRefresh, IconRight,
  IconSearch
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import styles from './style/index.module.less';
import '../mock';
import {getColumns} from './constants';
import {requestList} from "@/api/project";
import {ResultData} from "@/types/insights-common";
import {Department, PrivilegeEnum, Project, ProjectPagination} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import {getDataWithLocalCache} from "@/utils/localCache";
import {fetchAllData as fetchAllDepartmentData, translateToTreeStruct} from "@/pages/department/common";
import InfoForm from "@/pages/user/setting/info";
import Security from "@/pages/user/setting/security";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {stringifyObj} from "@/utils/util";
import {useSelector} from "react-redux";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import ProjectCreate from "@/pages/project/list/create";
import ProjectUpdate from "@/pages/project/list/update";

const { Title } = Typography;

function ProjectList() {
  const t = useLocale(locale);
  const tableCallback = async (record, type) => {
    if(type == 'update'){
      setUpdateVisible(!updateVisible);
      setUpdateId(record.id);
    }
    console.log("record is:" + JSON.stringify(record));
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
    console.log("formParams is:" + JSON.stringify(formParams));
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
