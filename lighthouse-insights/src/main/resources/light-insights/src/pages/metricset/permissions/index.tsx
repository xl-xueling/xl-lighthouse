import React, { useEffect, useState } from 'react';
import {Card, Collapse, Divider, Table, TableColumnProps, Tree} from "@arco-design/web-react";
const TreeNode = Tree.Node;
import styles from "./style/index.module.less";
const CollapseItem = Collapse.Item;

export default function MetricSetPermissionsPanel({resourceId,resourceType}) {

    useEffect(() => {
        console.log("resourceId:" + resourceId + ",resourceType:" + resourceType)

    },[])

    const columns: TableColumnProps[] = [
        {
            title: 'Name',
            dataIndex: 'name',
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
            name: 'Jane Doe',
            salary: 23000,
            address: '32 Park Road, London',
            email: 'jane.doe@example.com',
        },
        {
            key: '2',
            name: 'Alisa Ross',
            salary: 25000,
            address: '35 Park Road, London',
            email: 'alisa.ross@example.com',
        },
        {
            key: '3',
            name: 'Kevin Sandra',
            salary: 22000,
            address: '31 Park Road, London',
            email: 'kevin.sandra@example.com',
        },
        {
            key: '4',
            name: 'Ed Hellen',
            salary: 17000,
            address: '42 Park Road, London',
            email: 'ed.hellen@example.com',
        },
        {
            key: '5',
            name: 'William Smith',
            salary: 27000,
            address: '62 Park Road, London',
            email: 'william.smith@example.com',
        },
    ];


    return (
        <Card  style={{width:'100%'}}>
            <Collapse
                defaultActiveKey={['1', '2']}
            >
                <CollapseItem header='Beijing Toutiao Technology Co., Ltd.' name='1'>
                    <Table data={data} columns={columns} />
                </CollapseItem>

                <CollapseItem header='Introduce' name='2'>
                    <Table data={data} columns={columns} />
                </CollapseItem>

                <CollapseItem header='The Underlying AI Technology' name='3'>
                    <Table data={data} columns={columns} />
                </CollapseItem>
            </Collapse>
        </Card>
    );
}