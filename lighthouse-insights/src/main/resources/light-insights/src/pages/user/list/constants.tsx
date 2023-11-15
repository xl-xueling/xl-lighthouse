import React from 'react';
import {Button, Typography, Badge, Space} from '@arco-design/web-react';
import IconText from './icons/text.svg';
import IconHorizontalVideo from './icons/horizontal.svg';
import IconVerticalVideo from './icons/vertical.svg';
import dayjs from 'dayjs';
import styles from './style/index.module.less';

const { Text } = Typography;

export const ContentType = ['图文', '横版短视频', '竖版短视频'];
export const FilterType = ['规则筛选', '人工'];
export const Status = ['未上线', '已上线'];
export const StateType = ['未审核', '正常', '冻结', '注销'];


export function getColumns(t: any,callback: (record: Record<string, any>, type: string) => Promise<void>) {
  return [
    {
      title: t['userList.columns.id'],
      dataIndex: 'id',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.userName'],
      dataIndex: 'userName',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.department'],
      dataIndex: 'departmentName',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.email'],
      dataIndex: 'email',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.phone'],
      dataIndex: 'phone',
      render: (value,record) => <Text>{record.userName}</Text>,
    },
    {
      title: t['userList.columns.createdTime'],
      dataIndex: 'createdTime',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.state'],
      dataIndex: 'state',
      render: (value) => {
        if (value === 1) {
          return <Badge status="success" text={StateType[value]}/>;
        }else if(value === 0){
          return <Badge status="processing" text={StateType[value]}/>;
        }else{
          return <Badge status="error" text={StateType[value]}/>;
        }
      },
    },
    {
      title: t['userList.columns.operations'],
      dataIndex: 'operations',
      headerCellStyle: { paddingLeft: '15px',width:'280px' },
      render: (_, record) => (
          <Space size={16} direction="horizontal">
            <Button
                type="secondary"
                size="mini"
                onClick={() => callback(record, 'view')}
            >
              {'密码重置'}
            </Button>
            <Button
                type="secondary"
                size="mini"
                onClick={() => callback(record, 'view')}>
              {'冻结'}
            </Button>
            <Button
                type="secondary"
                size="mini"
                onClick={() => callback(record, 'view')}>
              {'删除'}
            </Button>
          </Space>
      ),
    },
  ]
  // return [
  //   {
  //     title: t['userList.columns.id'],
  //     dataIndex: 'id',
  //     render: (value) => <Text copyable>{value}</Text>,
  //   },
  //   {
  //     title: t['userList.columns.name'],
  //     dataIndex: 'name',
  //   },
  //   {
  //     title: t['userList.columns.contentType'],
  //     dataIndex: 'contentType',
  //     render: (value) => (
  //       <div className={styles['content-type']}>
  //         {ContentIcon[value]}
  //         {ContentType[value]}
  //       </div>
  //     ),
  //   },
  //   {
  //     title: t['userList.columns.filterType'],
  //     dataIndex: 'filterType',
  //     render: (value) => FilterType[value],
  //   },
  //   {
  //     title: t['userList.columns.contentNum'],
  //     dataIndex: 'count',
  //     sorter: (a, b) => a.count - b.count,
  //     render(x) {
  //       return Number(x).toLocaleString();
  //     },
  //   },
  //   {
  //     title: t['userList.columns.createdTime'],
  //     dataIndex: 'createdTime',
  //     render: (x) => dayjs().subtract(x, 'days').format('YYYY-MM-DD HH:mm:ss'),
  //     sorter: (a, b) => b.createdTime - a.createdTime,
  //   },
  //   {
  //     title: t['userList.columns.status'],
  //     dataIndex: 'status',
  //     render: (x) => {
  //       if (x === 0) {
  //         return <Badge status="error" text={Status[x]}></Badge>;
  //       }
  //       return <Badge status="success" text={Status[x]}></Badge>;
  //     },
  //   },

  //   {
  //     title: t['userList.columns.operations'],
  //     dataIndex: 'operations',
  //     headerCellStyle: { paddingLeft: '15px' },
  //     render: (_, record) => (
  //       <Button
  //         type="text"
  //         size="small"
  //         onClick={() => callback(record, 'view')}
  //       >
  //         {t['userList.columns.operations.view']}
  //       </Button>
  //     ),
  //   },
  // ];
}
