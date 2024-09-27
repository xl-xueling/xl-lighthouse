import React, {useEffect, useMemo, useState} from 'react';
import {
    Button,
    Card,
    Grid,
    PaginationProps,
    Space,
    Table,
    Form,
    Input,
    Breadcrumb, Notification, Divider,
} from '@arco-design/web-react';

import {IconHome, IconPlus} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Component} from "@/types/insights-common";
import {requestList} from "@/api/component";
import {getColumns} from "./constants";
import ComponentUpdateModal from "@/pages/component/update/ComponentUpdateModal";
import {requestDeleteById} from "@/api/component";
import styles from "@/pages/component/list/style/index.module.less";
import ComponentCreateModal from "@/pages/component/create/ComponentCreateModal";
const { Row, Col } = Grid;
const { useForm } = Form;
export default function ComponentList() {

    const [formParams, setFormParams] = useState({});
    const t = useLocale(locale);
    const [listData,setListData] = useState<Component[]>(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [currentComponent,setCurrentComponent] = useState<Component>(null);
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const [form] = useForm();
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showUpdatePanel, setShowsUpdatePanel] = useState(false);

    function handleSearch(params) {
        setPagination({ ...pagination, current: 1 });
        setFormParams(params);
    }

    const tableCallback = async (record, type) => {
        if(type == 'update'){
            setCurrentComponent(record);
            setShowsUpdatePanel(true);
        }else if(type == 'delete'){
            await handlerDeleteComponent(record.id);
        }
    };

    const columns = useMemo(() => getColumns(t, tableCallback), [t]);

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const combineParam:any = {}
        const {current, pageSize} = pagination;
        await requestList({
            queryParams:formParams,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((response) => {
            const {code,data,message} = response;
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

    const handlerDeleteComponent = async (id: number) => {
        await requestDeleteById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['componentList.operations.delete.submit.success']});
                setReloadTime(Date.now);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    };

    useEffect(() => {
        fetchData().then();
    },[reloadTime,pagination.current, pagination.pageSize, JSON.stringify(formParams)])

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <Breadcrumb.Item>
                    <IconHome />
                </Breadcrumb.Item>
                <Breadcrumb.Item style={{fontWeight:20}}>{t['componentList.breadcrumb.title']}</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
                <Grid.Row justify="space-between" align="center" style={{marginBottom:'15px'}}>
                    <Grid.Col span={16} style={{ textAlign: 'left' }}>
                        <Input.Search style={{width: 380}} placeholder={t['componentList.label.title']} allowClear onSearch={(v) => {handleSearch({search:v})}} />
                    </Grid.Col>
                    <Grid.Col span={8} style={{ textAlign: 'right' }}>
                        <Button size={"small"} type="primary" onClick={() => setShowCreateModal(true)}>{t['componentList.button.create']}</Button>
                    </Grid.Col>
                </Grid.Row>
                <Divider/>
                <Table
                    loading={loading}
                    rowKey={'id'}
                    size={"small"}
                    style={{ marginTop:12}}
                    columns={columns} data={listData} />
                {showCreateModal && <ComponentCreateModal onClose={() => setShowCreateModal(false)} onSuccess={() => {setReloadTime(Date.now)}} />}
                {showUpdatePanel && <ComponentUpdateModal componentInfo={currentComponent} onClose={() => setShowsUpdatePanel(false)} onSuccess={() => {setReloadTime(Date.now)}}/>}
            </Card>
        </>
    );
}