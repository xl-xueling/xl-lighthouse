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
import {IconCheck, IconClose, IconDownload, IconPlus, IconRefresh, IconSearch} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import styles from './style/index.module.less';
import '../mock';
import {getColumns} from './constants';
import {requestQueryList} from "@/api/project";
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

const { Title } = Typography;

function ProjectList() {
  const t = useLocale(locale);
  const tableCallback = async (record, type) => {
    console.log("record is:" + JSON.stringify(record));
    console.log(record, type);
  };
  const columns = useMemo(() => getColumns(t, tableCallback), [t]);
  const [data, setData] = useState([]);
  const [owner, setOwner] = useState(true);
  const [form] = useForm();
  const [form2] = useForm();
  const Step = Steps.Step;
  const [visible, setVisible] = React.useState(false);
  const [disabled, setDisabled] = React.useState(true);
  const [departData, setDepartData] = useState([]);
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
    setDepartData(translateToTreeStruct(allDepartInfo,'0'));
    console.log("formParams is:" + JSON.stringify(formParams));
    setLoading(true);
    fetchDepartData().then().catch(error => {
      console.log("error:" + error)
    });
    fetchData().then().catch(error => {
      console.log("error:" + error)
    }).finally(() => {
      setLoading(false);
    })
  }, [owner,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

  const fetchDepartData = async ():Promise<void> => {
    const departData = await getDataWithLocalCache('cache_all_department',300,fetchAllDepartmentData);

  }

  const fetchProjectsData = async ():Promise<ResultData<{list:Array<Project>,total:number}>> => {
    return new Promise((resolve) => {
       const proc = async () => {
         const {current, pageSize} = pagination;
         const result =  await requestQueryList({
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
            <Button size={"small"} type="primary" onClick={() => setVisible(true)}>创建</Button>
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

      <Modal
          title='创建工程'
          style={{ width:'650px' }}
          visible={visible}
          className='modal-demo-without-content-spacing'
          onOk={createProjectSubmit}
          onCancel={() => setVisible(false)}
      >
        <div>
        <Form
            form={form2}
            id={"ssss"}
            autoComplete='off'
            initialValues={{
              slider: 20,
              'a.b[0].c': ['b'],
            }}
            scrollToFirstError
        >
          <Form.Item label='Name' field='name' rules={[{ required: true }]}>
            <Input placeholder='please enter...' />
          </Form.Item>
          <Form.Item label={t['projectList.columns.department']} field="department" rules={[{ required: true }]}>
            <TreeSelect
                placeholder={"Please select"}
                multiple={true}
                allowClear={true}
                treeData={departData}
                style={{ width: '100%'}}
            />
          </Form.Item>
          <Form.Item label={'Description'} field="desc" rules={[{ required: true }]}>
            <Input.TextArea placeholder='Please enter ...' style={{ minHeight: 64}} />
          </Form.Item>
          <Form.Item label={'IsPrivate'} field="isPrivate" rules={[{ required: true }]}>
            <Radio.Group defaultValue='a' style={{ marginBottom: 20 }}>
              <Radio value='0'>Private</Radio>
              <Radio value='1'>Public</Radio>
            </Radio.Group>
          </Form.Item>
        </Form>
        </div>
      </Modal>
    </Card>
  );
}

export default ProjectList;
