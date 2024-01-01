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
import SearchForm from "@/pages/order/approve/list/form";
import OrderDetailModal from "@/pages/order/common/detail_modal";
import OrderProcessModal from "@/pages/order/approve/list/process_modal";

export default function ApproveList() {
    const t = useLocale(locale);
    const [formParams, setFormParams] = useState<any>({});
    const [listData, setListData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentOrder,setCurrentOrder] = useState<Order>();
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const [showProcessPanel, setShowProcessPanel] = useState(false);
    const [showDetailPanel, setShowDetailPanel] = useState(false);

    const tableCallback = async (record, type) => {
        if(type == 'process'){
            setShowProcessPanel(true);
            setCurrentOrder(record);
        }else if(type == 'detail'){
            setShowDetailPanel(true);
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
        const fetchApproveList:Promise<{list:Array<Order>,total:number}> = new Promise<{list:Array<Order>,total:number}>((resolve) => {
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
        const {list,total}:{list:Array<Order>,total:number} = result[0];
        setListData(list);
        setPagination({
            ...pagination,
            current,
            pageSize,
            total: total});
        setLoading(false);
    }

    useEffect(() => {
        console.log("-----Load page...")
        fetchData().then().catch(error => {
            console.log(error);
            Message.error(t['system.error']);
        })
    }, [reloadTime,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


    return (
        <Card>
            <SearchForm onSearch={handleSearch} />
            <Table
                rowKey="id"
                style={{ marginTop:12}}
                size={"small"}
                loading={loading}
                columns={columns} data={listData} />
            {showProcessPanel && <OrderProcessModal orderInfo={currentOrder} onClose={() => {setShowProcessPanel(false);}} onReload={() => {setReloadTime(Date.now)}}/>}
            {showDetailPanel && <OrderDetailModal orderInfo={currentOrder} onClose={() => setShowDetailPanel(false)}/>}
        </Card>
    );
}