import React, {useContext, useEffect, useRef, useState} from 'react';
import {Button, Card, Grid, Table, TableColumnProps, Typography} from "@arco-design/web-react";
import {VscTools} from "react-icons/vsc";
import Text from "@arco-design/web-react/es/Typography/text";
const { Row, Col } = Grid;
import { IoAdd } from "react-icons/io5";
import AuthAdd from "@/pages/caller/manage/auth_add";
import {CallerManageContext} from "@/pages/common/context";


export default function AuthList({}){

    useEffect(() => {
        console.log("---Authorize Index---");
    },[])

    const {callerInfo} = useContext(CallerManageContext);

    useEffect(() => {
        console.log("callerInfo is11:" + JSON.stringify(callerInfo));
    },[callerInfo])


    const [showAddAuthModal,setShowAddAuthModal] = useState<boolean>(false);

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
        <Card>
            <Row style={{marginBottom:'10px'}}>
                <Grid.Col span={15}>
                </Grid.Col>
                <Grid.Col span={9} style={{textAlign:"right" }}>
                    <Button type={'primary'} size={"mini"} onClick={() => setShowAddAuthModal(true)}>新增授权</Button>
                </Grid.Col>
            </Row>
            <Table  size={"small"} columns={columns} data={data} />
            {showAddAuthModal && <AuthAdd onClose={() => {setShowAddAuthModal(false)}} />}
        </Card>
    );
}