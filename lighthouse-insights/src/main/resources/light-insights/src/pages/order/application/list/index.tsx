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
import {getRandomString} from "@/utils/util";

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
                // const result = await requestList({
                //     // params: {
                //     //     page: current,
                //     //     pageSize,
                //     //     owner:owner?1:0,
                //     //     ...formParams,
                //     // },
                // });
                // resolve(result.data);
                console.log("");
            }
            proc().then();
        })

        Promise.all([fetchOrdersInfo]).then((result) => {
            const orders = result[0].list;
            const paginationData = orders.reduce((result:Order[],item:Order) => {
                const combineItem = {...item ,"key":getRandomString()};
                result.push(combineItem);
                return result;
            },[])
            setListData(paginationData)
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
                columns={columns} data={listData} />

            {showAddPanel && <FilterAddPanel onClose={() => setShowsAddPanel(false)}/>}
        </Card>
    );
}