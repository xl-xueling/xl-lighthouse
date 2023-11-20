import {Select, Table, Tag, Typography} from '@arco-design/web-react';
import React from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';


import "ace-builds";
import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/ace'
import 'ace-builds/src-noconflict/theme-tomorrow';
import 'ace-builds/src-noconflict/theme-clouds_midnight';
import 'ace-builds/src-noconflict/theme-ambiance';
import 'ace-builds/src-noconflict/theme-chaos';
import 'ace-builds/src-noconflict/theme-cloud9_night';
import 'ace-builds/src-noconflict/theme-cobalt';
import 'ace-builds/src-noconflict/theme-clouds';
import 'ace-builds/src-noconflict/theme-crimson_editor';
import 'ace-builds/src-noconflict/theme-dawn';
import 'ace-builds/src-noconflict/theme-twilight';
import 'ace-builds/src-noconflict/theme-nord_dark';
import 'ace-builds/src-noconflict/theme-kuroir';
import 'ace-builds/src-noconflict/theme-dracula';
import 'ace-builds/src-noconflict/theme-katzenmilch';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/mode-xml';
import "ace-builds/webpack-resolver";
import styles from './style/index.module.less';
import {useTheme} from "bizcharts";
import {IconPenFill} from "@arco-design/web-react/icon";

export default function QuickOperation() {
  const t = useLocale(locale);

  const columns = [
    {
      title: 'ID',
      headerCellStyle: { width:'7%' },
      render: (_col, _record, index) => <span>{index + 1}</span>,
    },
    {
      title: 'Template',
      dataIndex: 'template',
      headerCellStyle: { width:'60%'},
      render: (_col, record) => {
        const theme = document.body.getAttribute('arco-theme');
        console.log("theme is:" + JSON.stringify(theme))
        if(theme === "dark"){
          return <div >
            <AceEditor
                style={{ height:20,width:'100%'}}
                mode="xml"
                showPrintMargin={false}
                showGutter={false}
                theme="dracula"
                highlightActiveLine={false}
                enableBasicAutocompletion={true}
                name="UNIQUE_ID_OF_DIV"
                editorProps={{ $blockScrolling: true }}
            />
          </div>
        }else{
          return <div >
            <AceEditor
                style={{ height:20,width:'100%'}}
                mode="xml"
                showPrintMargin={false}
                showGutter={false}
                theme="dawn"
                highlightActiveLine={false}
                enableBasicAutocompletion={true}
                name="UNIQUE_ID_OF_DIV"
                editorProps={{ $blockScrolling: true }}
            />
          </div>
        }

      },
    },
    {
      title: 'Period',
      headerCellStyle: { width:'8%' },
      dataIndex: 'Period',
      render: (_col, record) => {
        return <Select
            size={"mini"}
            placeholder='Please select'
            style={{ width: '100%' }}
        >
          <Select.Option value='1'>1-Minute</Select.Option>
          <Select.Option value='3'>2-Minute</Select.Option>
          <Select.Option value='4'>10-Minute</Select.Option>
        </Select>
      }
    },
    {
      dataIndex: 'Expire',
      headerCellStyle: { width:'8%' },
      title: 'Expire',
      render: (_col, record) => {
        return <Select
            size={"mini"}
            placeholder='Please select'
            style={{ width: '100%' }}
        >
          <Select.Option value='1'>3 day</Select.Option>
          <Select.Option value='3'>14 day</Select.Option>
          <Select.Option value='4'>24 month</Select.Option>
        </Select>
      }
    },
    {
      dataIndex: 'Operate',
      headerCellStyle: { width:'2%'},
      title: 'Operate',
      render: (_col, record) => {
        return <IconPenFill/>
      }
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
          style={{ paddingTop:0,paddingBottom:0 }}
          hover={false}
          className={'statistic-wrapper'}
        columns={columns}
        data={data}
        border={true}
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
