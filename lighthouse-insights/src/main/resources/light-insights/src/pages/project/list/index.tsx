import React, {useEffect, useMemo, useState} from 'react';
import {Button, Card, PaginationProps, Space, Table, Typography,} from '@arco-design/web-react';
import PermissionWrapper from '@/components/PermissionWrapper';
import {IconDownload, IconPlus} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import styles from './style/index.module.less';
import '../mock';
import {getColumns} from './constants';
import {requestQueryList} from "@/api/project";
import {ResultData} from "@/types/insights-common";
import {PrivilegeEnum, Project, ProjectPagination} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";

const { Title } = Typography;

function ProjectList() {
  const t = useLocale(locale);
  const tableCallback = async (record, type) => {
    console.log("record is:" + JSON.stringify(record));
    console.log(record, type);
  };
  const columns = useMemo(() => getColumns(t, tableCallback), [t]);
  console.log("columns:" + JSON.stringify(columns))
  const [data, setData] = useState([]);
  const [pagination, setPagination] = useState<PaginationProps>({
    sizeCanChange: true,
    showTotal: true,
    pageSize: 10,
    current: 1,
    pageSizeChangeResetCurrent: true,
  });
  const [loading, setLoading] = useState(true);
  const [formParams, setFormParams] = useState({});

  useEffect(() => {
    setLoading(true);
    fetchData().then().catch(error => {
      console.log("error:" + error)
    }).finally(() => {
      setLoading(false);
    })
  }, [pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

  const fetchProjectsData = async ():Promise<ResultData<{list:Array<Project>,total:number}>> => {
    return new Promise((resolve) => {
       const proc = async () => {
         const {current, pageSize} = pagination;
         const result =  await requestQueryList({
           params: {
             page: current,
             pageSize,
             ...formParams,
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
      //item.permissions = r2[item.id] ? r2[item.id]:[];
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

  return (
    <Card>
      <Title heading={6}>{t['menu.list.searchTable']}</Title>
      <SearchForm onSearch={handleSearch} />
      <PermissionWrapper
        requiredPermissions={[
          { resource: 'menu.list.searchTable', actions: ['write'] },
        ]}
      >
        <div className={styles['button-group']}>
          <Space>
            <Button type="primary" icon={<IconPlus />}>
              {t['searchTable.operations.add']}
            </Button>
            <Button>{t['searchTable.operations.upload']}</Button>
          </Space>
          <Space>
            <Button icon={<IconDownload />}>
              {t['searchTable.operation.download']}
            </Button>
          </Space>
        </div>
      </PermissionWrapper>
      <Table
        rowKey="id"
        loading={loading}
        onChange={onChangeTable}
        pagination={pagination}
        columns={columns}
        data={data}
      />
    </Card>
  );
}

export default ProjectList;
