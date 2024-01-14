import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
    Tabs,
    Card,
    Input,
    Typography,
    Grid,
    Space,
    Avatar,
    PaginationProps,
    Pagination,
    Breadcrumb, Notification, Divider
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import AddCard from './card-add';
import { QualityInspection, BasicCard } from './interface';
import MetricSetAddPanel from "@/pages/metricset/create";
import {MetricSet, Project} from "@/types/insights-web";
import {requestList} from "@/api/metricset";
import {IconHome} from "@arco-design/web-react/icon";

const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});
export default function ListCard() {
  const t = useLocale(locale);
  const [loading, setLoading] = useState(true);
  const [showCreatePanel,setShowCreatePanel] = useState<boolean>(false);
  const [listData,setListData] = useState<MetricSet[]>([]);
  const [reloadTime,setReloadTime] = useState<number>(Date.now);
  const [activeKey, setActiveKey] = useState('all');
  const { Meta } = Card;

    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });
    const [formParams, setFormParams] = useState({});

    const tableCallback = async (record, type) => {
        console.log("record:" + record + ",type:" + type);
    };

  const fetchData = async () => {
      setLoading(true);
      const {current, pageSize} = pagination;
      await requestList({
          queryParams:formParams,
          pagination:{
              pageSize:pageSize,
              pageNum:current,
          },
      }).then((response) => {
          const {code, data ,message} = response;
          console.log("response is:" + JSON.stringify(response));
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

    useEffect(() => {
        fetchData().then();
    }, [reloadTime]);

  const handleShowCreatePanel = () => {
      setShowCreatePanel(true);
  }

  const handlerReloadList = () => {
      setReloadTime(Date.now);
  }

    const getCardList = () => {
        return (
            <Row gutter={24} className={styles['card-content']}>
                <Col xs={24} sm={12} md={8} lg={6} xl={6} xxl={6}>
                    <AddCard description={t['metricSetList.button.createMetric']} onShow={handleShowCreatePanel}/>
                </Col>
                {listData.map((item, index) => (
                    <Col xs={24} sm={12} md={8} lg={6} xl={6} xxl={6} key={index}>
                        <CardBlock item={item} loading={loading} callback={tableCallback} />
                    </Col>
                ))}
            </Row>
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
    <Card>
      <Title heading={6}>{t['menu.list.card']}</Title>
      <Tabs
        activeTab={activeKey}
        type="rounded"
        size={"small"}
        defaultActiveTab={'all'}
        onChange={setActiveKey}
        extra={
          <Input.Search
            style={{ width: '260px',paddingRight:'24px'}}
            placeholder={t[`cardList.tab.${activeKey}.placeholder`]}
          />
        }
      >
        <Tabs.TabPane key="all" title={t['metricSetList.tab.title.all']} />
        <Tabs.TabPane key="owner" title={t['metricSetList.tab.title.owner']} />
      </Tabs>
      <div className={styles.container}>
          <div>
              {getCardList()}
          </div>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Pagination defaultCurrent={pagination.current} total={pagination.total} showTotal={true}/>
          </div>
      </div>
        {showCreatePanel && <MetricSetAddPanel onClose={() => setShowCreatePanel(false)} onSuccess={handlerReloadList}/>}
    </Card>
      </>
  );
}
