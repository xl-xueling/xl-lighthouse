import { Button, Card, Radio, Tabs } from '@arco-design/web-react';
import React from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import QuickOperation from "./statistic-list";
import GroupBasicInfo from "@/pages/project/manage/group_basic";


export default function GroupAddPanel() {
  const t = useLocale(locale);
  return (
    <Card>
        <GroupBasicInfo/>
        {/*<div className={styles['data-statistic-list-content']}>*/}
        {/*    <QuickOperation />*/}
        {/*</div>*/}
    </Card>
  );
}
   

