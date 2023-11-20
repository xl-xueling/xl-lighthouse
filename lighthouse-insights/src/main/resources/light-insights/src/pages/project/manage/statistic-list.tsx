import { Table, Tag, Typography } from '@arco-design/web-react';
import React from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/ace'
import 'ace-builds/src-noconflict/mode-xml';// jsx模式的包
import 'ace-builds/src-noconflict/theme-monokai';// monokai的主题样式
import 'ace-builds/src-noconflict/ext-language_tools'; // 代码联想

const jsx = `import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/mode-golang'; // sql模式的包
import 'ace-builds/src-noconflict/mode-jsx';// mysql模式的包`;


export default function QuickOperation() {
  const t = useLocale(locale);
  const columns = [
    {
      title: t['monitor.list.title.order'],
      render: (_col, _record, index) => <span>{index + 1}</span>,
    },
    {
      title: t['monitor.list.title.cover'],
      dataIndex: 'cover',
      render: (_col, record) => (
          <div>

            <AceEditor
                mode="XML"
                theme="monokai"
                enableBasicAutocompletion={true}
                wrapEnabled={true}
                name="UNIQUE_ID_OF_DIV"
                editorProps={{ $blockScrolling: true }}
            />

          </div>
      ),
    },
    {
      title: t['monitor.list.title.name'],
      dataIndex: 'name',
    },
    {
      dataIndex: 'duration',
      title: t['monitor.list.title.duration'],
    },
    {
      dataIndex: 'id',
      title: t['monitor.list.title.id'],
    },
  ];
  const data = [
    {
      cover:
        'http://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/c788fc704d32cf3b1136c7d45afc2669.png~tplv-uwbnlip3yd-webp.webp',
      name: '视频直播',
      duration: '00:05:19',
      id: '54e23ade',
      status: -1,
    },
  ];
  return (
    <div className={styles['']}>
      <Table
        columns={columns}
        data={data}
        rowKey="id"
        rowSelection={{
          type: 'checkbox',
        }}
        border={false}
        pagination={false}
      />
      <Typography.Text
        type="secondary"
        className={styles['data-statistic-list-tip']}
      >
        {t['monitor.list.tip.rotations']}
        {data.length}
        {t['monitor.list.tip.rest']}
      </Typography.Text>
    </div>
  );
}
