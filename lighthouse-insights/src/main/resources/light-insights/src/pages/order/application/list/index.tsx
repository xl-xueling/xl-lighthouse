import React, {useEffect, useMemo, useState} from 'react';
import {
    Button,
    Card,
    Grid,
    PaginationProps,
    Space,
    Table,
} from '@arco-design/web-react';
import SearchForm from "@/pages/components/filter/list/form";
import FilterAddPanel from "@/pages/components/filter/add/filter_add";
import {getColumns} from "./constants";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import {Order, Project} from "@/types/insights-web";
import {requestList} from "@/api/application";

export default function Index() {

    const t = useLocale(locale);
    const [listData, setListData] = useState([]);

    const [formParams, setFormParams] = useState({});
    const tableCallback = async (record, type) => {
        console.log("record:" + record + ",type:" + type)
    }
    const columns = useMemo(() => getColumns(t, tableCallback), [t]);
    const [pagination, setPatination] = useState<PaginationProps>({
        sizeCanChange: true,
        showTotal: true,
        pageSize: 10,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const fetchData = async (): Promise<void> => {
        console.log("fetch data...")
        const fetchOrdersInfo:Promise<{list:Array<Order>,total:number}> = new Promise<{list:Array<Order>,total:number}>((resolve) => {
            const proc = async () => {
                const result = await requestList({
                    // params: {
                    //     page: current,
                    //     pageSize,
                    //     owner:owner?1:0,
                    //     ...formParams,
                    // },
                });
                console.log("result is:" + JSON.stringify(result));
                resolve(result.data);
            }
            proc().then();
        })

        Promise.all([fetchOrdersInfo]).then((result) => {
            console.log("result is:" + JSON.stringify(result));
        })
    }

    useEffect(() => {
        fetchData().then();
    },[])

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