import React, {useEffect, useRef, useState} from 'react';
import {
    Button,
    Grid,
    Message,
    Modal,
    Popconfirm,
    Select,
    Space,
    Table,
    TableColumnProps,
    Tabs,
    Typography
} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import {getRandomString} from "@/utils/util";

const Option = Select.Option;
const options = ['Beijing', 'Shanghai', 'Guangzhou', 'Disabled'];
const { Col, Row } = Grid;

export function DimensManageModal({groupInfo,onClose}){

    const t = useLocale(locale);
    const TabPane = Tabs.TabPane;

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
        {
            title: 'Operations',
            dataIndex: 'operations',
            headerCellStyle: { paddingLeft: '15px',width:'150px' },
            render: (value, record) => {
                let deleteButton;
                deleteButton = <Button key={getRandomString()}
                        type="text"
                        size="mini">
                    {'删除'}
                </Button>
                return  <Space key={getRandomString()} size={0} direction="horizontal">{[deleteButton]}</Space>;
            }
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
        {
            key: '6',
            name: 'Jane Doe',
            salary: 23000,
            address: '32 Park Road, London',
            email: 'jane.doe@example.com',
        },
        {
            key: '7',
            name: 'Alisa Ross',
            salary: 25000,
            address: '35 Park Road, London',
            email: 'alisa.ross@example.com',
        },
        {
            key: '8',
            name: 'Kevin Sandra',
            salary: 22000,
            address: '31 Park Road, London',
            email: 'kevin.sandra@example.com',
        },
        {
            key: '9',
            name: 'Ed Hellen',
            salary: 17000,
            address: '42 Park Road, London',
            email: 'ed.hellen@example.com',
        },
        {
            key: '10',
            name: 'William Smith',
            salary: 27000,
            address: '62 Park Road, London',
            email: 'william.smith@example.com',
        },
    ];

    return (
        <Modal
            title= {t['dimensManage.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '150px' }}
            visible={true}
            onCancel={onClose}
            footer={null}>
            <Tabs size={'small'} defaultActiveTab='1' tabPosition={'top'} type={'card-gutter'}>
                <TabPane key='1' title='键值清理' style={{padding:'6px'}}>
                    <Space size={10} direction={"vertical"} style={{width:'100%'}}>
                        <Row>
                            <Col span={17}>
                                <Select
                                    size={'mini'}
                                    placeholder='Select Dimension'
                                    style={{ width: 300 }}
                                    onChange={(value) =>
                                        Message.info({
                                            content: `You select ${value}.`,
                                            showIcon: true,
                                        })
                                    }>
                                    {options.map((option, index) => (
                                        <Option key={option} disabled={index === 3} value={option}>
                                            {option}
                                        </Option>
                                    ))}
                                </Select>
                            </Col>
                            <Col span={7} style={{ textAlign:'right' }}>
                                <Button size={'mini'}>删除</Button>
                            </Col>
                        </Row>
                        <Table size={'mini'} columns={columns} data={data} />
                    </Space>
                </TabPane>
                <TabPane key='2' title='批量删除' style={{padding:'10px'}}>
                    <Button size={'mini'}>全部删除</Button>
                    <Typography.Text style={{fontSize:12}}>
                        （将清除当前统计组所有维度筛选参数，随着原始消息的后续接入会重新累积维度数据，只清除筛选参数，原统计结果不受影响！）
                    </Typography.Text>
                </TabPane>
            </Tabs>
        </Modal>
    );
}