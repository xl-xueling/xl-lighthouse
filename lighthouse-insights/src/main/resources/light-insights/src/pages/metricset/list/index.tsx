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
            <AddCard description={t['cardList.add.quality']} />
          </Col>
        )}
        {list.map((item, index) => (
          <Col xs={24} sm={12} md={8} lg={6} xl={6} xxl={6} key={index}>
            <CardBlock card={item} type={type} loading={loading} />
          </Col>
        ))}
      </Row>
      //   <Row key={new Date().getTime()}>
      //       <Card
      //           className='card-with-icon-hover'
      //           style={{ width: 360 }}
      //           cover={
      //               <div style={{ height: 204, overflow: 'hidden' }}>
      //                   <img
      //                       style={{ width: '100%', transform: 'translateY(-20px)' }}
      //                       alt='dessert'
      //                       src='//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a20012a2d4d5b9db43dfc6a01fe508c0.png~tplv-uwbnlip3yd-webp.webp'
      //                   />
      //               </div>
      //           }
      //           actions={[
      //               <span className='icon-hover'>
      //     <IconThumbUp />
      //   </span>,
      //               <span className='icon-hover'>
      //     <IconShareInternal />
      //   </span>,
      //               <span className='icon-hover'>
      //     <IconMore />
      //   </span>,
      //           ]}
      //       >
      //           <Meta
      //               avatar={
      //                   <Space>
      //                       <Avatar size={24}>A</Avatar>
      //                       <Typography.Text>Username</Typography.Text>
      //                   </Space>
      //               }
      //               title='Card Title'
      //               description='This is the description'
      //           />
      //       </Card>
      //   </Row>
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

      <MetricSetAddPanel onClose={null}/>
    </Card>
  );
}
