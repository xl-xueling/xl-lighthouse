import React, { useEffect, useState } from 'react';
import {Card, Collapse, Divider, Table, TableColumnProps, Tabs, Tree} from "@arco-design/web-react";
const TreeNode = Tree.Node;
import styles from "./style/index.module.less";
import {PermissionPanel} from "@/pages/permission/PermissionPanel";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/metricset/preview/locale";
const CollapseItem = Collapse.Item;
const TabPane = Tabs.TabPane;

export default function MetricSetPermissionsPanel({resourceId,resourceType}) {

    const t = useLocale(locale);

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
            <Tabs type={"card-gutter"} defaultActiveTab='1'>
                <TabPane key='1' title= {'部门访问权限'}>
                    <PermissionPanel type={1} resourceType={resourceType} resourceId={resourceId}/>
                </TabPane>
                <TabPane key='2' title={'用户访问权限'}>
                    <PermissionPanel type={2} resourceType={resourceType} resourceId={resourceId}/>
                </TabPane>
                <TabPane key='3' title={'用户管理权限'}>
                    <PermissionPanel type={3} resourceType={resourceType} resourceId={resourceId}/>
                </TabPane>
            </Tabs>
        </Card>
    );
}