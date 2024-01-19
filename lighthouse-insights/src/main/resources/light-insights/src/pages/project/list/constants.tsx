import React from 'react';
import {Button, Typography, Space, Link, Popconfirm} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import { CiLock } from "react-icons/ci";
import { LuLock } from "react-icons/lu";
import { PiLockBold } from "react-icons/pi";
import {RiAppsLine} from "react-icons/ri";

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
      dataIndex: 'title',
      render: (value,record) =>
      {
          return (
              <div onClick={() => callback(record, 'detail')} style={{ cursor: "pointer" }} >
                  <span style={{display:"inline-flex",alignItems:"center"}}>{value}{record.privateType == 0 ?<CiLock style={{marginLeft:'5px'}}/>:null}</span>
              </div>)
      }
              ,
    },
      {
          title: t['projectList.columns.department'],
          dataIndex: 'departmentId',
          render: (value,record) => <DepartmentLabel departmentId={value}/> ,
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
      dataIndex: 'createTime',
      render: (value) => {return formatTimeStampBackUp(value)},
    },
    {
      title: t['projectList.columns.operations'],
      dataIndex: 'operations',
      headerCellStyle: {width:'250px' },
      render: (_, record) => {
          let viewButton;
          let updateButton;
          let manageButton;
          let deleteButton;
          let applyButton;
          if(record.permissions.includes('ManageAble')){
              viewButton = <Button key={getRandomString()}
                  type="text"
                  onClick={() => window.open('/project/display/' + record.id)}
                  size="mini">
                  {t['projectList.columns.operations.view']}
              </Button>;
                updateButton = <Button key={getRandomString()}
                    onClick={() => callback(record, 'update')}
                    type="text"
                    size="mini">
                    {t['projectList.columns.operations.update']}
                </Button>;
              manageButton = <Link key={getRandomString()} target={"_blank"} href={'/project/manage/' + record.id}>
                  <Button
                      type="text"
                      size="mini">
                      {t['projectList.columns.operations.manage']}
                  </Button>
              </Link>
              deleteButton =
                  <Popconfirm key={getRandomString()}
                              focusLock
                              position={"tr"}
                              title='Confirm'
                              content={t['projectList.form.delete.confirm']}
                              onOk={() => callback(record, 'delete')}
                  >
                  <Button
                      type="text"
                      size="mini">
                      {t['projectList.columns.operations.delete']}
                  </Button>
                  </Popconfirm>
          }else if(record.permissions.includes('AccessAble')){
              viewButton = <Button key={getRandomString()}
                      type="text"
                      onClick={() => window.open('/project/display/' + record.id)}
                      size="mini">
                      {t['projectList.columns.operations.view']}
                  </Button>;
          }else{
              applyButton =
                  <Popconfirm key={getRandomString()}
                              focusLock
                              title='Confirm'
                              content={t['projectList.form.apply.confirm']}
                              onOk={() => callback(record, 'delete')}
                  >
                      <Button
                      onClick={() => callback(record, 'apply')}
                      type="text"
                      size="mini">
                      {t['projectList.columns.operations.apply']}
                  </Button>
                  </Popconfirm>
          }
          return  <Space size={0} direction="horizontal">{[viewButton,updateButton,manageButton,deleteButton,applyButton]}</Space>;
      }
    },
  ]
}
