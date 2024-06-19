import React, {useState, useEffect, ReactNode, useContext} from 'react';
import {
  Grid,
  Card,
  Typography,
  Divider,
  Skeleton,
  Link, Notification,
} from '@arco-design/web-react';
import { useSelector } from 'react-redux';
import axios from 'axios';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import styles from './style/overview.module.less';
import IconCalendar from './assets/calendar.svg';
import IconComments from './assets/comments.svg';
import IconContent from './assets/content.svg';
import IconIncrease from './assets/increase.svg';
import {GlobalState} from "@/store";
import ChartPanel from "@/pages/stat/preview/chart_panel";
import {HomePageContext} from "@/pages/common/context";

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


export default function Overview({loading = false}) {
  const t = useLocale(locale);
  const userInfo = useSelector((state: GlobalState) => state.userInfo || {});
  const { homeData, statInfo } = useContext(HomePageContext);

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
            count={homeData?.projectCount}
            loading={loading}
            unit={t['workplace.pecs']}
          />
        </Col>
        <Divider type="vertical" className={styles.divider} />
        <Col flex={1}>
          <StatisticItem
            icon={<IconContent />}
            title={t['workplace.label.statCount']}
            count={homeData?.statCount}
            loading={loading}
            unit={t['workplace.pecs']}
          />
        </Col>
        <Divider type="vertical" className={styles.divider} />
        <Col flex={1}>
          <StatisticItem
            icon={<IconIncrease />}
            title={t['workplace.label.metricCount']}
            count={homeData?.metricCount}
            loading={loading}
            unit={t['workplace.pecs']}
          />
        </Col>
        <Divider type="vertical" className={styles.divider} />
        <Col flex={1}>
          <StatisticItem
              icon={<IconComments />}
              title={t['workplace.label.userCount']}
              count={homeData?.userCount}
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
          <Link href={'/project/preview/1'}>{t['workplace.seeMore']}</Link>
        </div>
        {statInfo && <ChartPanel parentLoading={false} statInfo={statInfo} size={'default'}/>}
      </div>
    </Card>
  );
}
