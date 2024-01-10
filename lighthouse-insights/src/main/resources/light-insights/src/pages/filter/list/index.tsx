import React, {useEffect, useMemo, useState} from 'react';
import {
    Radio,
    Button,
    Card,
    Grid,
    PaginationProps,
    Space,
    Table,
    Tabs,
    Typography,
    Modal,
    Divider,
    Steps,
    AutoComplete,
    Select,
    Cascader,
    Form,
    Input,
    InputNumber,
    TreeSelect,
    Switch,
    Message,
    TableColumnProps, Breadcrumb,
} from '@arco-design/web-react';

import SearchForm from "@/pages/filter/list/form";
import FilterAddPanel from "@/pages/filter/add/filter_add";
import {IconHome} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";

export default function FilterList() {

    const [formParams, setFormParams] = useState({});
    const t = useLocale(locale);
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
    ];

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <Breadcrumb.Item>
                    <IconHome />
                </Breadcrumb.Item>
                <Breadcrumb.Item style={{fontWeight:20}}>{t['filterList.breadcrumb.title']}</Breadcrumb.Item>
            </Breadcrumb>
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
        </>
    );
}