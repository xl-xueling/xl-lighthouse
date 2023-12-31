import React, {useEffect, useMemo, useState} from 'react';
import {
    Card,
    PaginationProps,
    Table,
    Message,
} from '@arco-design/web-react';
import {Order, Project} from "@/types/insights-web";
import {requestApproveList} from "@/api/order";
import {getColumns} from "@/pages/order/approve/list/constants";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/order/approve/list/locale";
import OrderProcessPanel from "@/pages/order/approve/list/process";
import SearchForm from "@/pages/order/approve/list/form";

export default function ApproveList() {
    const t = useLocale(locale);
    const [formParams, setFormParams] = useState<any>({});
    const [listData, setListData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentOrder,setCurrentOrder] = useState<Order>();
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const [showProcessPanel, setShowProcessPanel] = useState(false);

    const tableCallback = async (record, type) => {
        if(type == 'process'){
            console.log("process is ...")
            setShowProcessPanel(true);
            setCurrentOrder(record);
        }
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
        console.log("formParams is:" + JSON.stringify(formParams));
        const {current, pageSize} = pagination;
        const combineParam:any = {}
        combineParam.username = formParams.username;
        combineParam.types = formParams.types;
        combineParam.states = formParams.states;
        const createTime = formParams.createTime;
        if(createTime && Array.isArray(createTime)){
            combineParam.createStartTime = createTime[0];
            combineParam.createEndTime = createTime[1];
        }
        console.log("combineParam:" + JSON.stringify(combineParam));
        const fetchApproveList:Promise<{list:Array<Project>,total:number}> = new Promise<{list:Array<Project>,total:number}>((resolve) => {
            const proc = async () => {
                const result = await requestApproveList({
                        queryParams:combineParam,
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
            <Table
                rowKey="id"
                style={{ marginTop:12}}
                columns={columns} data={listData} />
            {showProcessPanel && <OrderProcessPanel orderInfo={currentOrder} onClose={() => setShowProcessPanel(false)}/>}
        </Card>
    );
}