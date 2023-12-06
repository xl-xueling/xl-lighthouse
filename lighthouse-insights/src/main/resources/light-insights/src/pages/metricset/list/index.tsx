import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {Tabs, Card, Input, Typography, Grid, Space, Avatar} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import AddCard from './card-add';
import { QualityInspection, BasicCard } from './interface';
import './mock';
import {IconMore, IconShareInternal, IconThumbUp} from "@arco-design/web-react/icon";
import MetricSetAddPanel from "@/pages/metricset/create";

const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});
export default function ListCard() {
  const t = useLocale(locale);
  const [loading, setLoading] = useState(true);
  const [showCreatePanel,setShowCreatePanel] = useState<boolean>(false);
  const [data, setData] = useState({
    quality: defaultList,
    service: defaultList,
    rules: defaultList,
  });

  const [activeKey, setActiveKey] = useState('all');
    const { Meta } = Card;

  const getData = () => {
    axios
      .get('/api/cardList')
      .then((res) => {
        setData(res.data);
      })
      .finally(() => setLoading(false));
  };

  const handleShowCreatePanel = () => {
      setShowCreatePanel(true);
  }

  const handleHideCreatePanel = () => {
      setShowCreatePanel(false);
  }


  useEffect(() => {
    getData();
  }, []);
  7;
  const getCardList = (
    list: Array<BasicCard & QualityInspection>,
    type: keyof typeof data
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
        onChange={setActiveKey}
        extra={
          <Input.Search
            style={{ width: '240px' }}
            placeholder={t[`cardList.tab.${activeKey}.placeholder`]}
          />
        }
      >
        <Tabs.TabPane key="all" title={t['cardList.tab.title.all']} />
        <Tabs.TabPane key="quality" title={t['cardList.tab.title.quality']} />
        <Tabs.TabPane key="service" title={t['cardList.tab.title.service']} />
        <Tabs.TabPane key="rules" title={t['cardList.tab.title.rules']} />
      </Tabs>
      <div className={styles.container}>
        {activeKey === 'all' ? (
          Object.entries(data).map(([key, list]) => (
            <div key={key}>
              <Title heading={6}>{t[`cardList.tab.title.${key}`]}</Title>
              {getCardList(list, key as keyof typeof data)}
            </div>
          ))
        ) : (
          <div className={styles['single-content']}>
            {getCardList(data[activeKey], activeKey as keyof typeof data)}
          </div>
        )}
      </div>

        {showCreatePanel && <MetricSetAddPanel onClose={handleHideCreatePanel}/>}

    </Card>
  );
}
