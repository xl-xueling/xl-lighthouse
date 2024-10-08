import React, {useEffect, useMemo, useState} from "react";
import {Caller} from "@/types/caller";
import {
    Breadcrumb,
    Button,
    Card,
    Divider,
    Grid, Input,
    Notification,
    PaginationProps,
    Spin,
    Table
} from "@arco-design/web-react";
import {getColumns} from "@/pages/caller/list/Constants";
import {requestDeleteById, requestList} from "@/api/caller";
import {useActivate, useUnactivate} from "react-activation";
import {IconHome} from "@arco-design/web-react/icon";
import CallerCreateModal from "@/pages/caller/create";
const BreadcrumbItem = Breadcrumb.Item;
import locale from "./locale/index"
import CallerUpdateModal from "@/pages/caller/update";
import useLocale from "@/utils/useLocale";
const InputSearch = Input.Search;
import { useLocation, useHistory } from 'react-router-dom';
import {getRandomString} from "@/utils/util";

export default function CallerListPanel(){

    const t = useLocale(locale);
    const [formParams, setFormParams] = useState({});
    const [loading, setLoading] = useState(true);
    const [showCreateModal,setShowCreateModal] = useState<boolean>(false);
    const [showUpdateModal,setShowUpdateModal] = useState<boolean>(false);
    const [listData,setListData] = useState<Array<Caller>>([]);
    const [current,setCurrent] = useState<Caller>(null);
    const [refreshTime,setRefreshTime] = useState<number>(Date.now);
    const history = useHistory();

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
        } else if(type == 'manage'){
            history.push(`/caller/manage/${record.id}`);
        }
    }

    const columns = useMemo(() => getColumns(t,tableCallback), [t]);

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
        await requestList({
            queryParams:formParams,
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
                setLoading(false);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                setLoading(false);
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerSearch = (value) => {
        setPagination({ ...pagination, current: 1 });
        setFormParams({search:value});
    }

    useEffect(() => {
        fetchData().then();
    },[refreshTime,pagination.current, pagination.pageSize, JSON.stringify(formParams)])

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    return (<>
        <Spin loading={loading} style={{ display: 'block' }}>
            <Card>
                <Grid.Row justify="space-between" align="center" style={{marginBottom:'15px'}}>
                    <Grid.Col span={16} style={{ textAlign: 'left' }}>
                        <InputSearch allowClear placeholder='Search Title' style={{width: 380}} onSearch={handlerSearch}/>
                    </Grid.Col>
                    <Grid.Col span={8} style={{ textAlign: 'right' }}>
                        <Button size={"small"} type="primary" onClick={() => setShowCreateModal(true)}>{t['callerList.button.create']}</Button>
                    </Grid.Col>
                </Grid.Row>
                <Divider/>
                <Table rowKey={'id'} size={"small"} columns={columns} data={listData} pagination={pagination} onChange={onChangeTable} />
            </Card>
        </Spin>
        {showCreateModal && <CallerCreateModal onClose={() => setShowCreateModal(false)} onSuccess={() => setRefreshTime(Date.now())}/>}
    </>)
}