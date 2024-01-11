import React, {useEffect, useMemo, useState} from 'react';
import {
    Radio,
    Button,
    Card,
    Grid,
    PaginationProps,
    Space,
    Table,
    Tabs,
    Typography,
    Modal,
    Divider,
    Steps,
    AutoComplete,
    Select,
    Cascader,
    Form,
    Input,
    InputNumber,
    TreeSelect,
    Switch,
    Message,
    TableColumnProps, Breadcrumb, Notification,
} from '@arco-design/web-react';

import SearchForm from "@/pages/filter/list/form";
import FilterAddPanel from "@/pages/filter/add/filter_add";
import {IconHome, IconPlus} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Component} from "@/types/insights-common";
import {Project} from "@/types/insights-web";
import {requestList} from "@/api/component";
import {getColumns} from "./constants";
import FilterUpdatePanel from "@/pages/filter/update/filter_update";
import {requestDeleteById} from "@/api/component";
import styles from "@/pages/filter/list/style/index.module.less";
const { Row, Col } = Grid;
const { useForm } = Form;
export default function ComponentList() {

    const [formParams, setFormParams] = useState({});
    const t = useLocale(locale);
    const [listData,setListData] = useState<Component[]>(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [currentComponent,setCurrentComponent] = useState<Component>(null);
    const [reloadSwitch,setReloadSwitch] = useState<number>(Date.now);
    const [form] = useForm();
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    function handlerReloadList(){
        setReloadSwitch(Date.now);
    }

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const [showAddPanel, setShowsAddPanel] = useState(false);
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
        console.log("FormParams is:" + JSON.stringify(formParams));
        const {current, pageSize} = pagination;
        const fetchComponentsInfo:Promise<{list:Array<Component>,total:number}> = new Promise<{list:Array<Component>,total:number}>((resolve) => {
            const proc = async () => {
                const result = await requestList({
                        queryParams:formParams,
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
        const result = await Promise.all([fetchComponentsInfo]);
        const {list,total}:{list:Array<Project>,total:number} = result[0];
        setListData(list);
        setPagination({
            ...pagination,
            current,
            pageSize,
            total: total});
        setLoading(false);
    }

    const handlerDeleteComponent = async (id: number) => {
        await requestDeleteById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['componentList.operations.delete.submit.success']});
                handlerReloadList();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    };

    useEffect(() => {
        fetchData().then();
    },[reloadSwitch,pagination.current, pagination.pageSize, JSON.stringify(formParams)])

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <Breadcrumb.Item>
                    <IconHome />
                </Breadcrumb.Item>
                <Breadcrumb.Item style={{fontWeight:20}}>{t['componentList.breadcrumb.title']}</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
                <div className={styles['search-form-wrapper']}>
                <Form
                    form={form}
                    className={styles['search-form']}
                    labelAlign="left"
                    wrapperCol={{ span: 24 }}
                >
                    <Row gutter={24}>
                        <Col span={9}>
                            <Form.Item field="Title">
                                <Input.Search  placeholder={t['componentList.label.title']} allowClear onSearch={(v) => {handleSearch({title:v})}} />
                            </Form.Item>
                        </Col>
                        <Grid.Col span={15} style={{ textAlign: 'right' }}>
                            <Space>
                                <Button size={"small"} type="primary" onClick={() => setShowsAddPanel(true)}>{t['componentList.button.create']}</Button>
                            </Space>
                        </Grid.Col>
                    </Row>
                </Form>
                </div>
                <Table
                    loading={loading}
                    rowKey={'id'}
                    style={{ marginTop:12}}
                    columns={columns} data={listData} />
                {showAddPanel && <FilterAddPanel onClose={() => setShowsAddPanel(false)}/>}
                {showUpdatePanel && <FilterUpdatePanel componentInfo={currentComponent} onClose={() => setShowsUpdatePanel(false)} onReload={handlerReloadList}/>}
            </Card>
        </>
    );
}