import React, { useState, useEffect, ReactNode } from 'react';
import {
  Grid,
  Card,
  Typography,
  Divider,
  Skeleton,
  Link, Notification,
} from '@arco-design/web-react';
import { useSelector } from 'react-redux';
import {IconCamera, IconCaretUp} from '@arco-design/web-react/icon';
import OverviewAreaLine from '@/components/Chart/overview-area-line';
import axios from 'axios';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import styles from './style/overview.module.less';
import IconCalendar from './assets/calendar.svg';
import IconComments from './assets/comments.svg';
import IconContent from './assets/content.svg';
import IconIncrease from './assets/increase.svg';
import {GlobalState} from "@/store";
import {requestStructure} from "@/api/department";
import {ResultData} from "@/types/insights-common";
import {requestOverView} from "@/api/home";
import {HomeData, Stat} from "@/types/insights-web";
import ChartPanel from "@/pages/stat/display/chart_panel";
import {requestQueryById} from "@/api/stat";

const { Row, Col } = Grid;

type StatisticItemType = {
  icon?: ReactNode;
  title?: ReactNode;
  count?: ReactNode;
  loading?: boolean;
  unit?: ReactNode;
};

function StatisticItem(props: StatisticItemType) {
  const { icon, title, count, loading, unit } = props;
  return (
    <div className={styles.item}>
      <div className={styles.icon}>{icon}</div>
      <div>
        <Skeleton loading={loading} text={{ rows: 2, width: 60 }} animation>
          <div className={styles.title}>{title}</div>
          <div className={styles.count}>
            {count}
            <span className={styles.unit}>{unit}</span>
          </div>
        </Skeleton>
      </div>
    </div>
  );
}


function Overview() {
  const [data, setData] = useState<HomeData>({});
  const [loading, setLoading] = useState(true);
  const t = useLocale(locale);
  const userInfo = useSelector((state: GlobalState) => state.userInfo || {});
  const [statInfo,setStatInfo] = useState<Stat>(null);

  const fetchStatData = async () => {
    setLoading(true);
    await requestQueryById({id:1011}).then((response) => {
      const {code, data ,message} = response;
      if(code == '0'){
        setStatInfo(data)
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
      setLoading(false);
    }).catch((error) => {
      console.log(error);
    })
  }

  const fetchData = async () => {
    setLoading(true);
    await requestOverView().then((response:ResultData) => {
      const {code, data ,message} = response;
      if(code == '0'){
        console.log("data is:" + JSON.stringify(response));
        setData(data)
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
    }).catch((error) => {
      console.log(error)
    }).finally(() => {
      setLoading(false);
    })
  };

  useEffect(() => {
    fetchData().then();
    fetchStatData().then();
  }, []);

  return (
    <Card>
      <Typography.Title heading={5}>
        {t['workplace.welcomeBack']}
        {userInfo?.username}
      </Typography.Title>
      <Divider />
      <Row>
        <Col flex={1}>
          <StatisticItem
            icon={<IconCalendar />}
            title={t['workplace.label.projectCount']}
            count={data?.projectCount}
            loading={loading}
            unit={t['workplace.pecs']}
          />
        </Col>
        <Divider type="vertical" className={styles.divider} />
        <Col flex={1}>
          <StatisticItem
            icon={<IconContent />}
            title={t['workplace.label.statCount']}
            count={data?.statCount}
            loading={loading}
            unit={t['workplace.pecs']}
          />
        </Col>
        <Divider type="vertical" className={styles.divider} />
        <Col flex={1}>
          <StatisticItem
            icon={<IconIncrease />}
            title={t['workplace.label.metricCount']}
            count={data?.metricCount}
            loading={loading}
            unit={t['workplace.pecs']}
          />
        </Col>
        <Divider type="vertical" className={styles.divider} />
        <Col flex={1}>
          <StatisticItem
              icon={<IconComments />}
              title={t['workplace.label.userCount']}
              count={data?.userCount}
              loading={loading}
              unit={t['workplace.pecs']}
          />
        </Col>
      </Row>
      <Divider />
      <div>
        <div className={styles.ctw}>
          <Typography.Paragraph
            className={styles['chart-title']}
            style={{ marginBottom: 0 }}
          >
            {t['workplace.systemMonitor']}
            <span className={styles['chart-sub-title']}>
              ({t['workplace.1minute.message.size']})
            </span>
          </Typography.Paragraph>
          <Link>{t['workplace.seeMore']}</Link>
        </div>
        {statInfo && <ChartPanel parentLoading={false} statInfo={statInfo} size={'default'}/>}
      </div>
    </Card>
  );
}

export default Overview;
