import React from 'react';
import {Button, Typography, Badge, Space, Message, Popconfirm, Modal} from '@arco-design/web-react';
import IconText from './icons/text.svg';
import IconHorizontalVideo from './icons/horizontal.svg';
import IconVerticalVideo from './icons/vertical.svg';
import dayjs from 'dayjs';
import styles from './style/index.module.less';
import DepartmentLabel from "@/pages/department/common/depart";
import {formatTimeStamp} from "@/utils/util";

const { Text } = Typography;

export function getColumns(t: any,callback: (record: Record<string, any>, type: string) => Promise<void>) {
  return [
    {
      title: t['userList.columns.id'],
      dataIndex: 'id',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.userName'],
      dataIndex: 'username',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.department'],
      dataIndex: 'departmentId',
      render: (value) => <DepartmentLabel departmentId={value}/>,
    },
    {
      title: t['userList.columns.email'],
      dataIndex: 'email',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['userList.columns.createdTime'],
      dataIndex: 'createTime',
      render: (value) => {return formatTimeStamp(value)},
    },
    {
      title: t['userList.columns.state'],
      dataIndex: 'state',
      render: (value) => {
        if(value === 0){
          return <Badge status="processing" text={t['userList.columns.state.pending']}/>;
        }else if (value === 1) {
          return <Badge status="success" text={t['userList.columns.state.normal']}/>;
        }else if(value === 2){
          return <Badge status="error" text={t['userList.columns.state.frozen']}/>;
        }else if(value === 3){
          return <Badge status="error" text={t['userList.columns.state.deleted']}/>;
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
                content= {t['userList.form.resetpasswd.confirm']}
                onOk={() => callback(record, 'resetPasswd')}
            >
              <Button
                  type="secondary"
                  size="mini">
                {t['userList.columns.operations.resetpasswd']}
              </Button>
            </Popconfirm>

            <Popconfirm
                focusLock
                title='Confirm'
                content={t['userList.form.frozen.confirm']}
                onOk={() => callback(record, 'frozen')}
            >
              <Button
                  type="secondary"
                  size="mini">
                  {t['userList.columns.operations.frozen']}
              </Button>
            </Popconfirm>

            <Popconfirm
                focusLock
                title='Confirm'
                content={t['userList.form.delete.confirm']}
                onOk={() => callback(record, 'delete')}
            >
              <Button
                  type="secondary"
                  size="mini">
                  {t['userList.columns.operations.delete']}
              </Button>
            </Popconfirm>
          </Space>
      ),
    },
  ]
}
