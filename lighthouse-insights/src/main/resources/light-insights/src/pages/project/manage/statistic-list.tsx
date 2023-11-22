import {Select, Table, Tag, Typography} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
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
import 'ace-builds/src-noconflict/theme-dreamweaver';
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/theme-sqlserver';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/mode-xml';
import "ace-builds/webpack-resolver";
import styles from './style/index.module.less';
import {useTheme} from "bizcharts";
import {IconPenFill} from "@arco-design/web-react/icon";
import {Stat} from "@/types/insights-web";

export default function GroupStatistics(statsInfo) {
  const t = useLocale(locale);

  const [statsData,setStatsData] = useState<Array<Stat>>([]);

  const data = [
    {
      key:2,
      cover:
          'http://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/c788fc704d32cf3b1136c7d45afc2669.png~tplv-uwbnlip3yd-webp.webp',
      name: '视频直播',
      duration: '00:05:19',
      id: '123',
      timeparam:2,
      tsss:3,
      template:'<stat-item title="每分钟_各省份_uv统计" stat="bitcount(userId)" dimens="province"/>',
      status: -1,
    },
  ];

  const data2 = [
    {
      key:2,
      id: 123,
      timeparam:2,
      tsss:3,
      template:'<stat-item title="每分钟_各省份_uv统计" stat="bitcount(userId)" dimens="province"/>',
      status: -1,
    },
  ];

  useEffect(() => {
    setTimeout(() => {
      setStatsData(data2);
    },50)

  },[statsInfo])

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      headerCellStyle: { width:'7%' },
      render: (_col, _record, index) => {
       return <Typography>{_col}</Typography>
      },
    },
    {
      title: 'Template',
      dataIndex: 'template',
      headerCellStyle: { width:'60%'},
      render: (_col, record) => {
        const theme = document.body.getAttribute('arco-theme');
        if(theme === "dark"){
          return <div >
            <AceEditor
                enableSnippets={false}
                style={{ height:20,width:'100%'}}
                mode="xml"
                value={_col}
                showPrintMargin={false}
                showGutter={false}
                theme="dracula"
                highlightActiveLine={false}
                enableBasicAutocompletion={true}
                editorProps={{ $blockScrolling: true }}
            />
          </div>
        }else{
          return <div >
            <AceEditor
                style={{ height:20,width:'100%'}}
                mode="xml"
                value={_col}
                showPrintMargin={false}
                showGutter={false}
                theme={"textmate"}
                highlightActiveLine={false}
                enableBasicAutocompletion={true}
                editorProps={{ $blockScrolling: true }}
            />
          </div>
        }
      },
    },

    {
      title: 'Expired',
      headerCellStyle: { width:'100px' },
      dataIndex: 'tsss',
      render: (_col, record) => {
        return <Select
            size={"mini"}
            defaultValue={_col}
            placeholder='Please select'
            style={{ width: '100%' }}
        >
          <Select.Option key={1} value={1}>1 Minute</Select.Option>
          <Select.Option key={2} value={2}>2 Minute</Select.Option>
          <Select.Option key={3} value={3}>10 Minute</Select.Option>
        </Select>
      }
    },

    {
      title: 'TimeParam',
      headerCellStyle: { width:'100px' },
      dataIndex: 'timeparam',
      render: (_col, record) => {
        return <Select
            size={"mini"}
            defaultValue={_col}
            placeholder='Please select'
            style={{ width: '100%' }}
        >
          <Select.Option key={1} value={1}>1-Minute</Select.Option>
          <Select.Option key={2} value={2}>2-Minute</Select.Option>
          <Select.Option key={3} value={3}>10-Minute</Select.Option>
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




  return (
    <div className={styles['']}>
      <Table
        style={{ paddingTop:0,paddingBottom:0 }}
        hover={false}
        size={"small"}
        className={'statistic-wrapper'}
        columns={columns}
        data={statsData}
        border={true}
        pagination={false}
      />

    </div>
  );
}
