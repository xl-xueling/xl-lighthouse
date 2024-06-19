import React, { useEffect, useState } from 'react';
import {
    Tabs,
    Card,
    Input,
    Typography,
    Grid,
    PaginationProps,
    Pagination,
    Breadcrumb, Notification, Spin, Space, Modal
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import AddCard from './card-add';
import MetricSetAddPanel from "@/pages/metricset/create";
import {MetricSet, Project, TreeNode} from "@/types/insights-web";
import {requestStarById, requestList, requestUnStarById} from "@/api/metricset";
import {IconHome} from "@arco-design/web-react/icon";
import {requestDeleteById} from "@/api/metricset";
import {useDispatch,useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {updateStoreStaredMetricInfo} from "@/store";
import MetricSetCardBox from "@/pages/metricset/list/MetricSetCardBox";
import {getRandomString} from "@/utils/util";
const { Title } = Typography;
const { Row, Col } = Grid;


export default function ListCard() {
    const t = useLocale(locale);
    const [loading, setLoading] = useState(true);
    const [showCreatePanel,setShowCreatePanel] = useState<boolean>(false);
    const [currentItem,setCurrentItem] = useState<MetricSet>(null);
    const [listData,setListData] = useState<MetricSet[]>([]);
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const { Meta } = Card;
    const [activeKey, setActiveKey] = useState('0');
    const [formParams, setFormParams] = useState<any>({});
    const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,30],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const handlerDelete = async (id: number) => {
        await requestDeleteById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['metricSetList.operations.delete.submit.success']});
                const updatedList = listData.filter(x => x.id != id);
                setListData(updatedList);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    };

  const fetchData = async () => {
      setLoading(true);
      const {current, pageSize} = pagination;
      const combineParams = {
          search:formParams.search,
          ownerFlag:activeKey == '1'?1:0,
      }
      await requestList({
          queryParams:combineParams,
          pagination:{
              pageSize:pageSize,
              pageNum:current,
          },
      }).then((response) => {
          const {code, data ,message} = response;
          if(code == '0'){
              setListData(data.list);
              setPagination({
                  ...pagination,
                  current,
                  pageSize,
                  total: data.total});
              setLoading(false);
          }else{
              Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
              setLoading(false);
          }
      }).catch((error) => {
          console.log(error);
      })
  }

    const handlerSearch = (search) => {
        setFormParams({search});
    }

    const changePage = (pageNum,pageSize) => {
        setPagination({ ...pagination, current: pageNum, pageSize:pageSize});
    }

    useEffect(() => {
        fetchData().then();
    }, [reloadTime,activeKey,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);

  const handleShowCreatePanel = () => {
      setShowCreatePanel(true);
  }

  const handlerReloadList = () => {
      setReloadTime(Date.now);
  }

    const getCardList = () => {
        return (
            <Space size={16} direction="vertical" style={{width:'100%'}}>
                <Row gutter={16} className={styles['card-content']}>
                    <Col span={6} key={getRandomString()}>
                        <AddCard description={t['metricSetList.button.createMetric']} onShow={handleShowCreatePanel}/>
                    </Col>
                    {listData.map((item, index) => (
                        <Col span={6} key={index}>
                            <MetricSetCardBox key={index} item={item}/>
                        </Col>
                    ))}
                </Row>
            </Space>
        );
    };

  return (
      <>
      <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
          <Breadcrumb.Item>
              <IconHome />
          </Breadcrumb.Item>
          <Breadcrumb.Item style={{fontWeight:20}}>{t['metricSetList.breadcrumb.title']}</Breadcrumb.Item>
      </Breadcrumb>
      <Spin loading={loading} style={{ display: 'block' }}>
        <Card>
          <Tabs
            type="rounded"
            size={"small"}
            activeTab={activeKey}
            onChange={setActiveKey}
            extra={
              <Input.Search
                style={{ width: '280px',paddingRight:'24px'}} allowClear={true} onSearch={handlerSearch}
              />
            }>
              <Tabs.TabPane key={"0"} title={t['metricSetList.tab.title.all']} />
              <Tabs.TabPane key={"1"} title={t['metricSetList.tab.title.owner']} />
          </Tabs>
          <div className={styles.container}>
              <div>
                  {getCardList()}
              </div>
              <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                  <Pagination sizeOptions={[15,30]} sizeCanChange={true} defaultCurrent={pagination.current} total={pagination.total}
                              showTotal={true}
                              onChange={changePage}
                  />
              </div>
          </div>
            {showCreatePanel && <MetricSetAddPanel onClose={() => setShowCreatePanel(false)} onSuccess={handlerReloadList}/>}
        </Card>
      </Spin>
      </>
  );
}
