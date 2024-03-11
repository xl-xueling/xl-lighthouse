import React from 'react';
import { Link, Card, Typography } from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/docs.module.less';
import StatPieChart from "@/pages/dashboard/workplace/StatPieChart";

const links = {
  react: 'https://arco.design/react/docs/start',
  vue: 'https://arco.design/vue/docs/start',
  designLab: 'https://arco.design/themes',
  materialMarket: 'https://arco.design/material/',
};
function QuickOperation() {
  const t = useLocale(locale);

  return (
    <Card>
        <StatPieChart />
    </Card>
  );
}

export default QuickOperation;
