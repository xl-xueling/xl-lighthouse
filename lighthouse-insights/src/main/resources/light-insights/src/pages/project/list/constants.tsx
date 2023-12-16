import React from 'react';
import {Button, Typography, Space, Popconfirm, Message, Link} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
  return [
      {
          title: '',
          dataIndex: 'binded',
          headerCellStyle: { width:'20px' },
          render: (_, record) => {
              return <Button icon={<PiLinkSimple/>} size={"mini"} shape={"round"} onClick={() => {callback(record, 'binded')}}/>
          }
      },
    {
      title: t['projectList.columns.id'],
      dataIndex: 'id',
      render: (value,record) =>
            <Text>{value}</Text>
              ,
    },
    {
      title: <>{t['projectList.columns.name']}<IoInformationCircleOutline style={{fontSize:12}}/></>,
      dataIndex: 'name',
      render: (value,record) =>
      {
          return (<div onClick={() => callback(record, 'detail')} style={{ cursor: "pointer" }} >
              <Text>{value}</Text></div>)
      }
              ,
    },
      {
          title: t['projectList.columns.department'],
          dataIndex: 'department.name',
          render: (value) => <Text>{value}</Text>,
      },
    {
          title: t['projectList.columns.admins'],
          dataIndex: 'admins',
          render: (value) => {
              return (<UserGroup users={value}/>);
          },
    },
    {
      title: t['projectList.columns.createdTime'],
      dataIndex: 'createdTime',
      render: (value) => <Text>{value}</Text>,
    },
    {
      title: t['projectList.columns.operations'],
      dataIndex: 'operations',
      headerCellStyle: {width:'250px' },
      render: (_, record) => (
          <Space size={0} direction="horizontal">
              <Link target={"_blank"} href={'/project/display/' + record.id}>
                  <Button
                      type="text"
                      size="mini">
                      {t['projectList.columns.operations.view']}
                  </Button>
              </Link>
              <Button
                  onClick={() => callback(record, 'update')}
                  type="text"
                  size="mini">
                {t['projectList.columns.operations.update']}
              </Button>
              <Link target={"_blank"} href={'/project/manage/' + record.id}>
                  <Button
                      type="text"
                      size="mini">
                    {t['projectList.columns.operations.manage']}
                  </Button>
              </Link>
              <Button
                  onClick={() => callback(record, 'apply')}
                  type="text"
                  size="mini">
                  {t['projectList.columns.operations.apply']}
              </Button>
            {/*<Popconfirm*/}
            {/*    position={"tr"}*/}
            {/*    focusLock*/}
            {/*    title='Confirm'*/}
            {/*    content='Are you sure to delete this project?'*/}
            {/*    onOk={() => callback(record, 'delete')}*/}
            {/*    onCancel={() => {*/}
            {/*      Message.error({*/}
            {/*        content: 'cancel',*/}
            {/*      });*/}
            {/*    }}*/}
            {/*>*/}
            {/*  <Button*/}
            {/*      type="text"*/}
            {/*      size="mini">*/}
            {/*    {t['projectList.columns.operations.delete']}*/}
            {/*  </Button>*/}
            {/*</Popconfirm>*/}
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
