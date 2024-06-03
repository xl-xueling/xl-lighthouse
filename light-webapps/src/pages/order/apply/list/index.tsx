import React, {useEffect, useMemo, useState} from 'react';
import {
    Breadcrumb,
    Button,
    Card,
    Grid, Notification,
    PaginationProps,
    Space,
    Table,
} from '@arco-design/web-react';
import SearchForm from "./form";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Order, Project} from "@/types/insights-web";
import {IconHome} from "@arco-design/web-react/icon";
import {requestApplyList, requestProcess} from "@/api/order";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {convertDateToTimestamp, DateFormat, getDayEndTimestamp, getDayStartTimestamp} from "@/utils/date";
import OrderDetailModal from "@/pages/order/common/detail_modal";
import getColumns from "@/pages/order/apply/list/constants";

export default function ApplyListPage() {

    const t = useLocale(locale);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [listData, setListData] = useState([]);
    const [loading,setLoading] = useState<boolean>(true);
    const [formParams, setFormParams] = useState<any>({});
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const [currentOrder,setCurrentOrder] = useState<Order>();
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const [showDetailPanel, setShowDetailPanel] = useState(false);

    const tableCallback = async (record, type) => {
        if(type == 'retract'){
            setCurrentOrder(record);
            retractOrder(record.id);
        }else if(type == 'detail'){
            setCurrentOrder(record);
            setShowDetailPanel(true);

        }
    };

    const retractOrder = async (id) => {
        const retractParam = {
            id:id,
            state:3,
        }
        await requestProcess(retractParam)
            .then((result) => {
            if(result.code === '0'){
                Notification.info({
                    style: { width: 420 },
                    title: 'Notification',
                    content: t['applyList.form.submit.retractSuccess'],
                })
                setReloadTime(Date.now);
            }else{
                Notification.warning({
                    style: { width: 420 },
                    title: 'Warning',
                    content: result.message || t['system.error'],
                })
            }
        }).catch((error) => {
            console.log(error);
            Notification.error({
                style: { width: 420 },
                title: 'Error',
                content: t['system.error'],
            })
        })
    }

    const columns = useMemo(() => getColumns(t, tableCallback), [t]);

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const queryParams:any = {};
        queryParams.userId = userInfo?.id;
        queryParams.types = formParams.types;
        queryParams.states = formParams.states;
        const createTime = formParams.createTime;
        if(createTime && Array.isArray(createTime)){
            queryParams.createStartTime = getDayStartTimestamp(convertDateToTimestamp(createTime[0],DateFormat));
            queryParams.createEndTime = getDayEndTimestamp(convertDateToTimestamp(createTime[1],DateFormat));
        }
        const {current, pageSize} = pagination;
        await requestApplyList({
            queryParams:queryParams,
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

    const [showAddPanel, setShowsAddPanel] = useState(false);

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

    return (
        <>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
            <Breadcrumb.Item>
                <IconHome />
            </Breadcrumb.Item>
            <Breadcrumb.Item style={{fontWeight:20}}>{t['applyList.breadcrumb.title']}</Breadcrumb.Item>
        </Breadcrumb>
        <Card>
            <SearchForm onSearch={handleSearch} />
            <Table
                loading={loading}
                rowKey={'id'}
                size={"small"}
                onChange={onChangeTable}
                style={{ marginTop:12}}
                pagination={pagination}
                columns={columns} data={listData} />
            {/*{showAddPanel && <FilterAddPanel onClose={() => setShowsAddPanel(false)}/>}*/}
            {showDetailPanel && <OrderDetailModal orderId={currentOrder.id} onClose={() => setShowDetailPanel(false)}/>}
        </Card>
        </>
    );
}