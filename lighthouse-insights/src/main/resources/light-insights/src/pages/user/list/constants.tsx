import React from 'react';
import {Button, Typography, Badge, Space, Message, Popconfirm, Modal} from '@arco-design/web-react';
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



// function delayClose(callback: (record: Record<string, any>, type: string) => Promise<void>) {
//   console.log("delay close.....record:" + JSON.stringify(record) + ",type:" + type)
//   callback(record,type);
//   // return new Promise((resolve, reject) => {
//   //
//   //   // setTimeout(() => {
//   //   //   resolve(null);
//   //   //   Message.info({
//   //   //     content: 'ok',
//   //   //   });
//   //   // }, 2000);
//   // });
//
// }

function confirm() {
  Modal.confirm({
    title: 'Confirm deletion',
    content:
        'Are you sure you want to delete the 3 selected items? ',
    okButtonProps: {
      status: 'default',
    },
    onOk: () => {
      return new Promise((resolve, reject) => {
        setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
      }).catch((e) => {
        Message.error({
          content: 'Error occurs!',
        });
        throw e;
      });
    },
  });
}

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
            <Popconfirm
                focusLock
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
                {'密码重置'}
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
                  {'冻结'}
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
}
