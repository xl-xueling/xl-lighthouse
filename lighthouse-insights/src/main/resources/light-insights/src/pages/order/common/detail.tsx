import React, {useEffect, useMemo, useState} from 'react';
import {Badge, Form, Input, Steps, Table, TableColumnProps, Typography} from "@arco-design/web-react";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStamp} from "@/utils/util";
import useLocale from "@/utils/useLocale";
const { Text } = Typography;
import locale from "./locale/index";
import DepartmentLabel from "@/pages/department/common/depart";

export default function OrderDetail({orderInfo}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState([]);
    const [userListData, setUserListData] = useState([]);
    const orderColumns: TableColumnProps[] = [
            {
                title: t['order.columns.id'],
                dataIndex: 'id',
                render: (value,record) =>
                    <Text>{value}</Text>
                ,
            },
            {
                title: t['order.columns.user'],
                dataIndex: 'user',
                render: (value,record) =>
                    <UserGroup users={[value]}/>
                ,
            },
            {
                title: t['order.columns.type'],
                dataIndex: 'orderType',
                render: (value) => {
                    if(value == '1'){
                        return <Text>{t['order.columns.type.project.access']}</Text>;
                    }else if(value == '2'){
                        return <Text>{t['order.columns.type.stat.access']}</Text>;
                    }else if(value == '3'){
                        return <Text>{t['order.columns.type.metrics.access']}</Text>;
                    }else if(value == '4'){
                        return <Text>{t['order.columns.type.adjust.limited.threshold']}</Text>;
                    }else if(value == '5'){
                        return <Text>{t['order.columns.type.stat.pend.approve']}</Text>;
                    }else if(value == '6'){
                        return <Text>{t['order.columns.type.user.pend.approve']}</Text>;
                    }
                },
            },
            {
                title: t['order.columns.createTime'],
                dataIndex: 'createTime',
                render: (value) => {return formatTimeStamp(value)},
            },
            {
                title: t['order.columns.state'],
                dataIndex: 'state',
                render: (value) => {
                    if(value === 0){
                        return <Badge status="processing" text={t['order.columns.state.pending']}/>;
                    }else if (value === 1) {
                        return <Badge status="success" text={t['order.columns.state.approved']}/>;
                    }else if(value === 2){
                        return <Badge status="error" text={t['order.columns.state.rejected']}/>;
                    }else if(value === 3){
                        return <Badge status="error" text={t['order.columns.state.retracted']}/>;
                    }
                },
            }
    ];

    const userApproveColumns: TableColumnProps[] = [
        {
            title: t['order.user.approve.columns.id'],
            dataIndex: 'id',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.username'],
            dataIndex: 'username',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.email'],
            dataIndex: 'email',
            render: (value,record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['order.user.approve.columns.department'],
            dataIndex: 'department',
            render: (value,record) =>
                <DepartmentLabel department={value}/>
            ,
        },
        {
            title: t['order.user.approve.columns.createTime'],
            dataIndex: 'createTime',
            render: (value) => {return formatTimeStamp(value)},
        },
        {
            title: t['order.user.approve.columns.state'],
            dataIndex: 'state',
            render: (value) => {
                if(value === 0){
                    return <Badge status="processing" text={t['order.user.approve.columns.state.pending']}/>;
                }else if (value === 1) {
                    return <Badge status="success" text={t['order.user.approve.columns.state.normal']}/>;
                }else if(value === 2){
                    return <Badge status="error" text={t['order.user.approve.columns.state.frozen']}/>;
                }else if(value === 3){
                    return <Badge status="error" text={t['order.user.approve.columns.state.deleted']}/>;
                }
            },
        }
    ];

    useEffect(() => {
        console.log("orderInfo is:" + JSON.stringify(orderInfo));
        setListData([orderInfo]);
        setUserListData([orderInfo.user])
    },[])

    return (
      <div>
          <Typography.Title
              style={{ marginTop: 0, marginBottom: 16 }}
              heading={6}
          >
              工单信息
          </Typography.Title>
          <Table rowKey="id" pagination={false} columns={orderColumns} data={listData} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              用户信息
          </Typography.Title>
          <Table rowKey="id" pagination={false} columns={userApproveColumns} data={userListData} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              申请原因
          </Typography.Title>
          <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              流程信息
          </Typography.Title>
          <Steps current={2} status='error' style={{ maxWidth: 780, marginBottom: 40 }}>
              <Steps.Step title='Succeeded' description='This is a description' />
              <Steps.Step title='Processing' description='This is a description' />
              <Steps.Step title='Processing' description='This is a description' />
              <Steps.Step title='Processing' description='This is a description' />
              <Steps.Step title='Processing' description='This is a description' />
              <Steps.Step title='Pending' description='This is a description' />
          </Steps>

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              审核批复
          </Typography.Title>
          <Input.TextArea maxLength={200} rows={2}  showWordLimit={true}/>
      </div>
    );
}