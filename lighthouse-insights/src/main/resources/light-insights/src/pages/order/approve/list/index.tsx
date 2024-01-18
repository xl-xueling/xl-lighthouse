import React, {useEffect, useMemo, useState} from 'react';
import {
    Card,
    PaginationProps,
    Table,
    Message, Breadcrumb, Notification,
} from '@arco-design/web-react';
import {Order, Project} from "@/types/insights-web";
import {requestApproveList} from "@/api/order";
import {getColumns} from "@/pages/order/approve/list/constants";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/order/approve/list/locale";
import SearchForm from "@/pages/order/approve/list/form";
import OrderDetailModal from "@/pages/order/common/detail_modal";
import OrderProcessModal from "@/pages/order/approve/list/process_modal";
import {IconHome} from "@arco-design/web-react/icon";
import {convertDateToTimestamp, DateFormat, getDayEndTimestamp, getDayStartTimestamp} from "@/utils/date";

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
            combineParam.createStartTime = getDayStartTimestamp(convertDateToTimestamp(createTime[0],DateFormat));
            combineParam.createEndTime = getDayEndTimestamp(convertDateToTimestamp(createTime[1],DateFormat));
        }
        await requestApproveList({
            queryParams:combineParam,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setListData(data.list);
                setPagination({
                    ...pagination,
                    current,
                    pageSize,
                    total: data.total});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })

    }

    useEffect(() => {
        fetchData().then();
    }, [reloadTime,pagination.current, pagination.pageSize, JSON.stringify(formParams)]);


    return (
        <>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
            <Breadcrumb.Item>
                <IconHome />
            </Breadcrumb.Item>
            <Breadcrumb.Item style={{fontWeight:20}}>{t['approveList.breadcrumb.title']}</Breadcrumb.Item>
        </Breadcrumb>
        <Card>

            <SearchForm onSearch={handleSearch} />
            <Table
                rowKey="id"
                style={{ marginTop:12}}
                size={"small"}
                loading={loading}
                columns={columns} data={listData} />
            {showProcessPanel && <OrderProcessModal orderId={currentOrder.id} onClose={() => {setShowProcessPanel(false);}} onReload={() => {setReloadTime(Date.now)}}/>}
            {showDetailPanel && <OrderDetailModal orderId={currentOrder.id} onClose={() => setShowDetailPanel(false)}/>}
        </Card>
        </>
    );
}