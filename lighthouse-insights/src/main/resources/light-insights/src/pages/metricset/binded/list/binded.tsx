import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card, Table, TableColumnProps, Space, Divider
} from '@arco-design/web-react';
import {
    IconDownCircle, IconPlus, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import GroupBasicPanel from "@/pages/group/basic";
import useForm from "@arco-design/web-react/es/Form/useForm";
import StatAddPanel from "@/pages/stat/add/stat_add";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/edit";
import AddBindedPanel from "@/pages/metricset/binded/binded";
const { Row, Col } = Grid;
const { Text } = Typography;

export default function BindedList({metricId}) {

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
        <div>
            <Form
                className={styles['search-form']}
                labelAlign="left"
                style={{marginTop:8}}
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 19 }}
            >
                <Row gutter={24}>
                    <Col span={8}>
                        <Form.Item field="Title">
                            <Input.Search  placeholder={'Search Title'} allowClear />
                        </Form.Item>
                    </Col>
                    <Col span={13}>
                        <Text>Item 1</Text>
                        <Divider type='vertical' />
                        <Text>Item 2</Text>
                        <Divider type='vertical' />
                        <Text>Item 3</Text>
                        <Divider type='vertical' />
                        <Text>Item 3</Text>
                        <Divider type='vertical' />
                        <Text>Item 3</Text>
                        <Divider type='vertical' />
                        <Text>Item 2</Text>
                        <Divider type='vertical' />
                        <Text>Item 3</Text>
                        <Divider type='vertical' />
                        <Text>Item 3</Text>
                        <Divider type='vertical' />
                        <Text>Item 3</Text>
                    </Col>
                    <Grid.Col span={3} style={{ textAlign: 'right' }}>
                        <Space>
                            <Button size={"small"} type="primary" icon={<IconPlus />}>绑定</Button>
                        </Space>
                    </Grid.Col>
                </Row>
            </Form>
        <Table size={"small"} columns={columns} data={data} />
        {/*<AddBindedPanel metricId={0} />*/}
        </div>
    );
}