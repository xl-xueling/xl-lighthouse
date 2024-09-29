import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Breadcrumb,
    Card,
    Input,
    Table,
    Grid,
    Notification,
    TableColumnProps,
    Button,
    PaginationProps,
    Spin, Divider
} from "@arco-design/web-react";
import {IconHome} from "@arco-design/web-react/icon";
const BreadcrumbItem = Breadcrumb.Item;
import locale from "./locale/index"
import CallerCreateModal from "@/pages/caller/create";
import {requestDeleteById, requestList} from "@/api/caller";
import {Caller} from "@/types/caller";
import {getColumns} from "./Constants";
import CallerUpdateModal from "@/pages/caller/update";
import useLocale from "@/utils/useLocale";
const InputSearch = Input.Search;

export default function CallerListPage(){

    const t = useLocale(locale);

    const [loading, setLoading] = useState(true);

    const [showCreateModal,setShowCreateModal] = useState<boolean>(false);
    const [showUpdateModal,setShowUpdateModal] = useState<boolean>(false);
    const [listData,setListData] = useState<Array<Caller>>([]);
    const [current,setCurrent] = useState<Caller>(null);
    const [refreshTime,setRefreshTime] = useState<number>(Date.now);

    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,30],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 11,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const tableCallback = async (record, type) => {
        if (type == 'update') {
            setCurrent(record);
            setShowUpdateModal(true);
        } else if(type == 'delete'){
            await handlerDeleteCaller(record.id).then();
        }
    }

    const handlerDeleteCaller = async (id: number) => {
        setLoading(true);
        await requestDeleteById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['callerList.operations.delete.submit.success']});
                setRefreshTime(Date.now());
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    };

    const fetchData = async () => {
        setLoading(true);
        const {current, pageSize} = pagination;
        // const combineParams = {
        //     search:formParams.search,
        //     ownerFlag:activeKey == '1'?1:0,
        // }
        const combineParams = {

        };
        await requestList({
            queryParams:combineParams,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            },
        }).then((response) => {
            console.log("response is:" + JSON.stringify(response));
            const {code, data ,message} = response;
            if(code == '0'){
                setListData(data.list);
                setPagination({
                    ...pagination,
                    current,
                    pageSize,
                    total: data.total});
                setLoading(false);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                setLoading(false);
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        fetchData().then();
    },[refreshTime])

    const columns = useMemo(() => getColumns(t,tableCallback), [t]);

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <BreadcrumbItem>
                    <IconHome />
                </BreadcrumbItem>
                <BreadcrumbItem style={{fontWeight:20}}>{t['callList.breadcrumb.title']}</BreadcrumbItem>
            </Breadcrumb>
            <Spin loading={loading} style={{ display: 'block' }}>
                <Card>
                    <Grid.Row justify="space-between" align="center" style={{marginBottom:'15px'}}>
                        <Grid.Col span={16} style={{ textAlign: 'left' }}>
                            <InputSearch allowClear placeholder='Enter keyword to search' style={{width: 380}} />
                        </Grid.Col>
                        <Grid.Col span={8} style={{ textAlign: 'right' }}>
                            <Button size={"small"} type="primary" onClick={() => setShowCreateModal(true)}>{'创建'}</Button>
                        </Grid.Col>
                    </Grid.Row>
                    <Divider/>
                    <Table rowKey={'id'} size={"small"} columns={columns} data={listData} />
                </Card>
            </Spin>
            {showCreateModal && <CallerCreateModal onClose={() => setShowCreateModal(false)} onSuccess={() => setRefreshTime(Date.now)}/>}
        </>
    );
}