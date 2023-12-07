import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {Tabs, Card, Input, Typography, Grid, Space, Avatar, PaginationProps} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import AddCard from './card-add';
import { QualityInspection, BasicCard } from './interface';
import MetricSetAddPanel from "@/pages/metricset/create";
import {MetricSet, Project} from "@/types/insights-web";
import {requestList} from "@/api/metricset";

const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});
export default function ListCard() {
  const t = useLocale(locale);
  const [loading, setLoading] = useState(true);
  const [showCreatePanel,setShowCreatePanel] = useState<boolean>(false);
  // const [data, setData] = useState({
  //   quality: defaultList,
  //   service: defaultList,
  //   rules: defaultList,
  // });
  const [listData,setListData] = useState<{quality:MetricSet[]}>({quality:[]});

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

  // const getData = () => {
  //   axios
  //     .get('/api/cardList')
  //     .then((res) => {
  //       setData(res.data);
  //     })
  //     .finally(() => setLoading(false));
  // };

  const fetchData = async () => {
      setLoading(true);
      const {current, pageSize} = pagination;
      const fetchMetricSetsInfo:Promise<{list:Array<MetricSet>,total:number}> = new Promise<{list:Array<MetricSet>,total:number}>((resolve) => {
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

      const result = await Promise.all([fetchMetricSetsInfo]);
      const {list,total}:{list:Array<MetricSet>,total:number} = result[0];
      console.log("result is:" + JSON.stringify(result));
      setListData({"quality":list});
      setLoading(false);
  }

    useEffect(() => {
        fetchData().then();
    }, []);

  const handleShowCreatePanel = () => {
      setShowCreatePanel(true);
  }

  const handleHideCreatePanel = () => {
      setShowCreatePanel(false);
  }



  const getCardList = (
    list: Array<BasicCard & QualityInspection>,
    type: keyof typeof listData
  ) => {
    return (
      <Row gutter={24} className={styles['card-content']}>
        {type === 'quality' && (
          <Col xs={24} sm={12} md={8} lg={6} xl={6} xxl={6}>
            <AddCard description={t['cardList.add.quality']} onShow={handleShowCreatePanel}/>
          </Col>
        )}
        {list.map((item, index) => (
          <Col xs={24} sm={12} md={8} lg={6} xl={6} xxl={6} key={index}>
            <CardBlock card={item} type={type} loading={loading} />
          </Col>
        ))}
      </Row>
    );
  };

  return (
    <Card>
      <Title heading={6}>{t['menu.list.card']}</Title>
      <Tabs
        activeTab={activeKey}
        type="rounded"
        size={"small"}
        onChange={setActiveKey}
        extra={
          <Input.Search
            style={{ width: '240px' }}
            placeholder={t[`cardList.tab.${activeKey}.placeholder`]}
          />
        }
      >
        <Tabs.TabPane key="all" title={t['metricSetList.tab.title.all']} />
        <Tabs.TabPane key="quality" title={t['metricSetList.tab.title.owner']} />
      </Tabs>
      <div className={styles.container}>
          {
              Object.entries(listData).map(([key, list]) => (
                  <div key={key}>
                      <Title heading={6}>{t[`cardList.tab.title.${key}`]}</Title>
                      {getCardList(list, key as keyof typeof listData)}
                  </div>
              ))
          }
      </div>

        {showCreatePanel && <MetricSetAddPanel onClose={handleHideCreatePanel}/>}

    </Card>
  );
}
