import React from 'react';
import {Button, Typography, Badge, Space, Popconfirm, Message} from '@arco-design/web-react';
import IconText from './icons/text.svg';
import IconHorizontalVideo from './icons/horizontal.svg';
import IconVerticalVideo from './icons/vertical.svg';
import dayjs from 'dayjs';
import styles from './style/index.module.less';
import {IconHeart, IconHeartFill, IconStar, IconStarFill, IconUser} from "@arco-design/web-react/icon";

const { Text } = Typography;


export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
  return [
      {
          title: '',
          dataIndex: 'fav',
          render: (_, record) => {
              return <Space size={16} direction="horizontal">
                  <Popconfirm
                      focusLock
                      position={"bl"}
                      title='Confirm'
                      content='Are you sure to reset this user password?'
                      onOk={() => callback(record, 'resetPasswd')}
                      onCancel={() => {
                          Message.error({
                              content: 'cancel',
                          });
                      }}
                  >
                      <IconStarFill style={{ color:"oranged"}}/>
                  </Popconfirm>
              </Space>
            }
      },
    {
      title: t['projectList.columns.id'],
      dataIndex: 'id',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['projectList.columns.name'],
      dataIndex: 'name',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['projectList.columns.createdTime'],
      dataIndex: 'createdTime',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['projectList.columns.department'],
      dataIndex: 'departmentId',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['projectList.columns.desc'],
      dataIndex: 'desc',
      render: (value) => <Text>{value}</Text>,
    },

    {
      title: t['userList.columns.operations'],
      dataIndex: 'operations',
      headerCellStyle: { paddingLeft: '15px',width:'280px' },
      render: (_, record) => (
          <Space size={16} direction="horizontal">
              <Popconfirm
                  focusLock
                  position={"bl"}
                  title='Confirm'
                  content='Are you sure to reset this user password?'
                  onOk={() => callback(record, 'resetPasswd')}
                  onCancel={() => {
                      Message.error({
                          content: 'cancel',
                      });
                  }}
              >
                  <Button
                      type="secondary"
                      size="mini">
                      {'查看'}
                  </Button>
              </Popconfirm>
            <Popconfirm
                focusLock
                position={"bl"}
                title='Confirm'
                content='Are you sure to reset this user password?'
                onOk={() => callback(record, 'resetPasswd')}
                onCancel={() => {
                  Message.error({
                    content: 'cancel',
                  });
                }}
            >
              <Button
                  type="secondary"
                  size="mini">
                {'修改'}
              </Button>
            </Popconfirm>

            <Popconfirm
                focusLock
                title='Confirm'
                content='Are you sure to frozen this user?'
                onOk={() => callback(record, 'frozen')}
                onCancel={() => {
                  Message.error({
                    content: 'cancel',
                  });
                }}
            >
              <Button
                  type="secondary"
                  size="mini">
                {'管理'}
              </Button>
            </Popconfirm>

            <Popconfirm
                focusLock
                title='Confirm'
                content='Are you sure to delete this user?'
                onOk={() => callback(record, 'delete')}
                onCancel={() => {
                  Message.error({
                    content: 'cancel',
                  });
                }}
            >
              <Button
                  type="secondary"
                  size="mini">
                {'删除'}
              </Button>
            </Popconfirm>
          </Space>
      ),
    },


  ]
  // return [
  //   {
  //     title: t['searchTable.columns.id'],
  //     dataIndex: 'id',
  //     render: (value) => <Text copyable>{value}</Text>,
  //   },
  //   {
  //     title: t['searchTable.columns.name'],
  //     dataIndex: 'name',
  //   },
  //   {
  //     title: t['searchTable.columns.contentType'],
  //     dataIndex: 'contentType',
  //     render: (value) => (
  //       <div className={styles['content-type']}>
  //         {ContentIcon[value]}
  //         {ContentType[value]}
  //       </div>
  //     ),
  //   },
  //   {
  //     title: t['searchTable.columns.filterType'],
  //     dataIndex: 'filterType',
  //     render: (value) => FilterType[value],
  //   },
  //   {
  //     title: t['searchTable.columns.contentNum'],
  //     dataIndex: 'count',
  //     sorter: (a, b) => a.count - b.count,
  //     render(x) {
  //       return Number(x).toLocaleString();
  //     },
  //   },
  //   {
  //     title: t['searchTable.columns.createdTime'],
  //     dataIndex: 'createdTime',
  //     render: (x) => dayjs().subtract(x, 'days').format('YYYY-MM-DD HH:mm:ss'),
  //     sorter: (a, b) => b.createdTime - a.createdTime,
  //   },
  //   {
  //     title: t['searchTable.columns.status'],
  //     dataIndex: 'status',
  //     render: (x) => {
  //       if (x === 0) {
  //         return <Badge status="error" text={Status[x]}></Badge>;
  //       }
  //       return <Badge status="success" text={Status[x]}></Badge>;
  //     },
  //   },
  //   {
  //     title: t['searchTable.columns.operations'],
  //     dataIndex: 'operations',
  //     headerCellStyle: { paddingLeft: '15px' },
  //     render: (_, record) => (
  //       <Button
  //         type="text"
  //         size="small"
  //         onClick={() => callback(record, 'view')}
  //       >
  //         {t['searchTable.columns.operations.view']}
  //       </Button>
  //     ),
  //   },
  // ];
}
