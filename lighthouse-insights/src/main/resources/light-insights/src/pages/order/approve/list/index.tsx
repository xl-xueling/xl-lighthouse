import React, {useEffect, useMemo, useState} from 'react';
import {
    Button,
    Card,
    Grid,
    PaginationProps,
    Space,
    Table,
    Message,
} from '@arco-design/web-react';
import SearchForm from "@/pages/components/filter/list/form";
import FilterAddPanel from "@/pages/components/filter/add/filter_add";
import {Project} from "@/types/insights-web";
import {requestApproveList} from "@/api/order";
import {getColumns} from "@/pages/order/approve/list/constants";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/order/approve/list/locale";

export default function FilterList() {
    const t = useLocale(locale);
    const [formParams, setFormParams] = useState({});
    const [listData, setListData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const [showAddPanel, setShowsAddPanel] = useState(false);

    const tableCallback = async (record, type) => {
        console.log("table callback!");
    };

    const columns = useMemo(() => getColumns(t, tableCallback), [t]);

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    function handleSearch(params) {
        setPagination({ ...pagination, current: 1 });
        setFormParams(params);
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const {current, pageSize} = pagination;
        const combineParam:any = {}
        const fetchApproveList:Promise<{list:Array<Project>,total:number}> = new Promise<{list:Array<Project>,total:number}>((resolve) => {
            const proc = async () => {
                const result = await requestApproveList({
                    // queryParams:combineParam,
                        pagination:{
                            pageSize:pageSize,
                            pageNum:current,
                        }
                    }
                );
                resolve(result.data);
            }
            proc().then();
        })
        const result = await Promise.all([fetchApproveList]);
        const {list,total}:{list:Array<Project>,total:number} = result[0];
        setListData(list);
        setPagination({
            ...pagination,
            current,
            pageSize,
            total: total});
        setLoading(false);
    }

    useEffect(() => {
        fetchData().then().catch(error => {
            console.log(error);
            Message.error(t['system.error']);
        })
    }, [pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


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
                rowKey="id"
                style={{ marginTop:12}}
                columns={columns} data={listData} />
            {showAddPanel && <FilterAddPanel onClose={() => setShowsAddPanel(false)}/>}
        </Card>
    );
}