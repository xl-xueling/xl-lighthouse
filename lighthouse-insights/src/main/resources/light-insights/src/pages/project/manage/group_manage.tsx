import { Button, Card, Radio, Tabs } from '@arco-design/web-react';
import React from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import DataStatisticList from './statistic-list';
import styles from './style/index.module.less';

import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/ace'
import 'ace-builds/src-noconflict/theme-monokai';// monokai的主题样式
import 'ace-builds/src-noconflict/ext-language_tools'; // 代码联想
import 'ace-builds/src-noconflict/mode-xml';

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
          {/*<div className={styles['data-statistic-list-content']}>*/}
          {/*  <DataStatisticList />*/}
          {/*</div>*/}
          <div>
            <AceEditor
                mode="xml"
                theme="monokai"
                enableBasicAutocompletion={true}
                wrapEnabled={true}
                name="UNIQUE_ID_OF_DIV"
                editorProps={{ $blockScrolling: true }}
            />
          </div>
        </div>
      </div>
    </Card>
  );
}
   

