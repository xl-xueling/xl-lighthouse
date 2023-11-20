import { Button, Card, Radio, Tabs } from '@arco-design/web-react';
import React from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import QuickOperation from "./statistic-list";



export default function GroupManage() {
  const t = useLocale(locale);
  return (
    <Card>
      <div className={styles['data-statistic-content']}>
        <div className={styles['data-statistic-list-wrapper']}>
          <div className={styles['data-statistic-list-header']}>
            <Button type="text">{}</Button>
            <Button>{'创建'}</Button>
          </div>
          <div className={styles['data-statistic-list-content']}>
            <QuickOperation />
          </div>
          <div>
          </div>
        </div>
      </div>
    </Card>
  );
}
   

