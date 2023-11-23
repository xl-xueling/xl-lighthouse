import {Button, Form, Grid, Select, Table, Tag, Tooltip, Typography} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
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
import {IconPenFill, IconPlus} from "@arco-design/web-react/icon";
import {Stat} from "@/types/insights-web";
import EditTable, {EditTableColumnProps, EditTableComponentEnum} from "@/pages/components/edittable/EditTable";

export default function GroupStatistics({statsInfo}) {

  const t = useLocale(locale);

  const [statsData,setStatsData] = useState<Array<Stat>>([]);

  const editTableRef = useRef(null);

  useEffect(() => {
    if(statsInfo != null){
      statsInfo.forEach(z => {
        z.key = z.id;
      })
      setStatsData(statsInfo);
    }
  },[statsInfo])

  const columns : EditTableColumnProps[] = [
    {
      title: 'Template',
      dataIndex: 'template',
      editable: true,
      componentType:EditTableComponentEnum.ACE_EDITOR,
      headerCellStyle: { width:'60%'},
      render: (_col, record) => {
        const theme = document.body.getAttribute('arco-theme');
        if(theme === "dark"){
          return <div>
            <AceEditor

                enableSnippets={false}
                style={{ height:30,width:'100%'}}
                mode="xml"
                value={_col}
                showPrintMargin={false}
                name="UNIQUE_ID_OF_DIV"
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
                focus={true}
                style={{ height:30,width:'100%',marginTop:'8px'}}
                mode="xml"
                value={_col}
                showPrintMargin={false}
                showGutter={false}
                name="UNIQUE_ID_OF_DIV"
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
      title: 'TimeParam',
      headerCellStyle: { width:'5%' },
      dataIndex: 'timeparam',
      editable: true,
      componentType:EditTableComponentEnum.SELECT,
      render: (_col, record) => {
        return <Select
            size={"mini"}
            defaultValue={_col}
            placeholder='1-hour'
            style={{ width: '100%' }}
        >
          <Select.Option key={1} value={1}>1-Minute</Select.Option>
          <Select.Option key={2} value={2}>2-Minute</Select.Option>
          <Select.Option key={3} value={3}>10-Minute</Select.Option>
        </Select>
      }
    },
    {
      title: 'Expired',
      headerCellStyle: { width:'5%' },
      dataIndex: 'expired',
      editable: true,
      componentType:EditTableComponentEnum.SELECT,
      render: (_col, record) => {
        return <Select
            size={"mini"}
            defaultValue={_col}
            placeholder='2 周'
            style={{ width: '100%' }}
        >
          <Select.Option key={1} value={1}>1 Minute</Select.Option>
          <Select.Option key={2} value={2}>2 Minute</Select.Option>
          <Select.Option key={3} value={3}>10 Minute</Select.Option>
        </Select>
      }
    },
    {
      dataIndex: 'Operate',
      headerCellStyle: { width:'5%'},
      title: 'Operate',
      componentType:EditTableComponentEnum.BUTTON,
      render: (_col, record) => {
        return <IconPenFill/>
      }
    },
  ];


  return (
        <div>
        <Grid.Row>
          <Grid.Col span={16}>
            <Typography.Title
                style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
            >
              {'Templates'}
            </Typography.Title>
          </Grid.Col>
          <Grid.Col span={8} style={{ textAlign: 'right' }}>
            <Button type={"secondary"} size={"mini"} onClick={() => editTableRef.current.addRow()}>添加</Button>
          </Grid.Col>
        </Grid.Row>
      <EditTable ref={editTableRef} columns={columns} initData={statsData}/>
    </div>
  );
}
