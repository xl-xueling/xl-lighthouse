import React, {useEffect, useState} from 'react';
import {Card, Grid, Space} from '@arco-design/web-react';
import Overview from './overview';
import PopularContents from './popular-contents';
import ContentPercentage from './content-percentage';
import Shortcuts from './shortcuts';
import Announcement from './announcement';
import Carousel from './carousel';
import Docs from './docs';
import styles from './style/index.module.less';
import './mock';
import {useSelector} from "react-redux";
import {MetricSet} from "@/types/insights-web";
import CardBlock from "@/pages/metricset/list/card-block";
import MetricSetCardBox from "@/pages/metricset/list/MetricSetCardBox";
import {getRandomString} from "@/utils/util";
const { Row, Col } = Grid;
const gutter = 16;

function Workplace() {

  const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);

  const [listData,setListData] = useState<MetricSet[]>(staredMetricInfo.slice(0,8));

  useEffect(() => {
      console.log("listData:" + JSON.stringify(listData));
  },[])

  return (
    <div className={styles.wrapper}>
      <Space size={16} direction="vertical" className={styles.left}>
        <Overview />
          <Row gutter={gutter}>
              {listData.map((item, index) => (
                      <Col span={6} key={index}>
                          <MetricSetCardBox key={index} item={item}/>
                      </Col>
              ))}
          </Row>
      </Space>

      <Space className={styles.right} size={16} direction="vertical">
        <Shortcuts />
        <Docs />
      </Space>
    </div>
  );
}

export default Workplace;
