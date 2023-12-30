import React, {useEffect, useMemo, useState} from 'react';
import {Form, Input, Steps, Table, TableColumnProps, Typography} from "@arco-design/web-react";

export default function OrderDetail() {

    const columns: TableColumnProps[] = [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Salary',
            dataIndex: 'salary',
        },
        {
            title: 'Address',
            dataIndex: 'address',
        },
        {
            title: 'Email',
            dataIndex: 'email',
        },
    ];
    const data = [
        {
            key: '1',
            id: 'Jane Doe',
            salary: 23000,
            address: '32 Park Road, London',
            email: 'jane.doe@example.com',
        },
    ];

    return (
      <div>
          <Typography.Title
              style={{ marginTop: 0, marginBottom: 16 }}
              heading={6}
          >
              工单信息
          </Typography.Title>
          <Table pagination={false} columns={columns} data={data} />

          <Typography.Title
              style={{ marginTop: 30 }}
              heading={6}
          >
              用户信息
          </Typography.Title>
          <Table pagination={false} columns={columns} data={data} />

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