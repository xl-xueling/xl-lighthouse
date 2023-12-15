import React, {useEffect, useMemo, useState} from 'react';
import {
    Radio,
    Button,
    Card,
    Grid,
    PaginationProps,
    Space,
    Table,
    TableColumnProps,
} from '@arco-design/web-react';
import SearchForm from "@/pages/components/filter/list/form";
import FilterAddPanel from "@/pages/components/filter/add/filter_add";

export default function FilterList() {

    const [formParams, setFormParams] = useState({});

    const [pagination, setPatination] = useState<PaginationProps>({
        sizeCanChange: true,
        showTotal: true,
        pageSize: 10,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const [showAddPanel, setShowsAddPanel] = useState(false);

    function onChangeTable({ current, pageSize }) {
        setPatination({
            ...pagination,
            current,
            pageSize,
        });
    }

    function handleSearch(params) {
        setPatination({ ...pagination, current: 1 });
        setFormParams(params);
    }

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
            <SearchForm onSearch={handleSearch} />
            <Grid.Row justify="space-between" align="center">
                <Grid.Col span={16} style={{ textAlign: 'left' }}>
                </Grid.Col>
                <Grid.Col span={8} style={{ textAlign: 'right' }}>
                    <Space>
                        <Button size={"small"} type="primary" onClick={() => setShowsAddPanel(true)}>创建</Button>
                    </Space>
                </Grid.Col>
            </Grid.Row>
            <Table
                style={{ marginTop:12}}
                columns={columns} data={data} />

            {showAddPanel && <FilterAddPanel onClose={() => setShowsAddPanel(false)}/>}
        </Card>
    );
}