import React, {useEffect, useMemo, useState} from 'react';
import {Badge, Form, Input, Steps, Table, TableColumnProps, Typography} from "@arco-design/web-react";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStamp} from "@/utils/util";
import useLocale from "@/utils/useLocale";
const { Text } = Typography;
import locale from "@/pages/order/approve/list/locale";

export default function OrderDetail({orderInfo}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState([]);
    const columns: TableColumnProps[] = [
            {
                title: t['approveList.columns.id'],
                dataIndex: 'id',
                render: (value,record) =>
                    <Text>{value}</Text>
                ,
            },
            {
                title: t['approveList.columns.user'],
                dataIndex: 'user',
                render: (value,record) =>
                    <UserGroup users={[value]}/>
                ,
            },
            {
                title: t['approveList.columns.type'],
                dataIndex: 'orderType',
                render: (value) => {
                    if(value == '1'){
                        return <Text>{t['approveList.columns.type.project.access']}</Text>;
                    }else if(value == '2'){
                        return <Text>{t['approveList.columns.type.stat.access']}</Text>;
                    }else if(value == '3'){
                        return <Text>{t['approveList.columns.type.metrics.access']}</Text>;
                    }else if(value == '4'){
                        return <Text>{t['approveList.columns.type.adjust.limited.threshold']}</Text>;
                    }else if(value == '5'){
                        return <Text>{t['approveList.columns.type.stat.pend.approve']}</Text>;
                    }else if(value == '6'){
                        return <Text>{t['approveList.columns.type.user.pend.approve']}</Text>;
                    }
                },
            },
            {
                title: t['approveList.columns.desc'],
                dataIndex: 'detail',
                render: (value,record) =>
                {
                    return "--";
                }
            },
            {
                title: t['approveList.columns.createTime'],
                dataIndex: 'createTime',
                render: (value) => {return formatTimeStamp(value)},
            },
            {
                title: t['approveList.columns.state'],
                dataIndex: 'state',
                render: (value) => {
                    if(value === 0){
                        return <Badge status="processing" text={t['approveList.columns.state.pending']}/>;
                    }else if (value === 1) {
                        return <Badge status="success" text={t['approveList.columns.state.approved']}/>;
                    }else if(value === 2){
                        return <Badge status="error" text={t['approveList.columns.state.rejected']}/>;
                    }else if(value === 3){
                        return <Badge status="error" text={t['approveList.columns.state.retracted']}/>;
                    }
                },
            }
    ];

    useEffect(() => {
        console.log("orderInfo is:" + JSON.stringify(orderInfo));
        setListData([orderInfo]);
    },[])

    return (
      <div>
          <Typography.Title
              style={{ marginTop: 0, marginBottom: 16 }}
              heading={6}
          >
              工单信息
          </Typography.Title>
          <Table rowKey="id" pagination={false} columns={columns} data={listData} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              用户信息
          </Typography.Title>
          <Table rowKey="id" pagination={false} columns={columns} data={listData} />

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