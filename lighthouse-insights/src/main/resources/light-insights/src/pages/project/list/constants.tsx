import React from 'react';
import {Button, Tooltip, Typography, Space, Popconfirm, Message, Link} from '@arco-design/web-react';
import {
    IconAlignCenter,
    IconAttachment,
    IconFullscreen,
    IconInfoCircle,
    IconInfoCircleFill, IconLink, IconPushpin,
    IconRotateRight,
    IconStar,
    IconStarFill
} from "@arco-design/web-react/icon";
import IconHorizontalVideo from "@/pages/list/search-table/icons/horizontal.svg";
import {doc} from "prettier";
import cursor = doc.builders.cursor;
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";






export function getColumns(t: any,favoriteIds:Array<number>, callback: (record: Record<string, any>, type: string) => Promise<void>) {
  return [
      {
          title: '',
          dataIndex: 'favorite',
          headerCellStyle: { width:'5px' },
          render: (_, record) => {
              if(favoriteIds.includes(record.id)){
                  return <Space size={16} direction="horizontal">
                      <Popconfirm
                          focusLock
                          position={"bl"}
                          title='Confirm'
                          content= {t['projectList.columns.operations.unfavorite.confirm']}
                          onOk={() => callback(record, 'unFavorite')}
                      >
                          <PiLinkSimple  style={{ cursor:"pointer"}}/>
                      </Popconfirm>
                  </Space>
              }else{
                  return <Space size={16} direction="horizontal">
                      <Popconfirm
                          focusLock
                          position={"bl"}
                          title='Confirm'
                          content= {t['projectList.columns.operations.favorite.confirm']}
                          onOk={() => callback(record, 'favorite')}
                      >
                          <PiLinkSimple style={{ cursor:"pointer"}}/>
                      </Popconfirm>
                  </Space>
              }
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
      title: t['projectList.columns.name'],
      dataIndex: 'name',
      render: (value,record) =>
          // <Tooltip color={'#2d3a57'} content={record.desc + record.desc+ record.desc+ record.desc}>
          //     <IconInfoCircle /><Text>{value}</Text>
          // </Tooltip>
      {
          //return (<div><Text>{value}</Text><IconInfoCircle fontSize={14} style={{ cursor: "pointer" }}/></div>)
          return (<div onClick={() => callback(record, 'detail')} style={{ cursor: "pointer" }} ><Text>{value}</Text></div>)
      }
              ,
    },
      {
          title: t['projectList.columns.department'],
          dataIndex: 'department.name',
          render: (value) => <Text>{value}</Text>,
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
          <Space size={16} direction="horizontal">
              <Popconfirm
                  focusLock
                  position={"tr"}
                  title='Confirm'
                  content='Are you sure to reset this user password2?'
                  onCancel={() => {
                      Message.error({
                          content: 'cancel',
                      });
                  }}
              >
                  <Button
                      type="secondary"
                      size="mini">
                      {t['projectList.columns.operations.view']}
                  </Button>
              </Popconfirm>
              <Button
                  onClick={() => callback(record, 'update')}
                  type="secondary"
                  size="mini">
                {t['projectList.columns.operations.update']}
              </Button>
              <Link target={"_blank"} href={'/project/manage/' + record.id}>
              <Button
                  type="secondary"
                  size="mini">
                {t['projectList.columns.operations.manage']}
              </Button>
              </Link>
            <Popconfirm
                position={"tr"}
                focusLock
                title='Confirm'
                content='Are you sure to delete this project?'
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
                {t['projectList.columns.operations.delete']}
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
