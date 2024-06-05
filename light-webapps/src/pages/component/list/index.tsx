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
    Breadcrumb, Notification, TreeSelect, Popconfirm,
} from '@arco-design/web-react';

import {IconHome, IconPlus} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Component} from "@/types/insights-common";
import {requestList} from "@/api/component";
import ComponentUpdateModal from "@/pages/component/update/ComponentUpdateModal";
import {requestDeleteById} from "@/api/component";
import styles from "@/pages/component/list/style/index.module.less";
import ComponentCreateModal from "@/pages/component/create/ComponentCreateModal";
import {CiLock} from "react-icons/ci";
import {translateResponse} from "@/pages/department/base";
import UserGroup from "@/pages/user/common/groups";
import {getRandomString} from "@/utils/util";
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


    const getColumns = (t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) => {
        return [
            {
                title: t['componentList.label.id'],
                dataIndex: 'id',
            },
            {
                title: t['componentList.label.title'],
                dataIndex: 'title',
                render: (value,record) =>
                {
                    return (
                        <span style={{display:"inline-flex",alignItems:"center"}}>{value}{record.privateType == 0 ?<CiLock style={{marginLeft:'5px'}}/>:null}</span>
                    )
                }
            },
            {
                title: t['componentList.label.display'],
                dataIndex: 'configuration',
                headerCellStyle: {width: '500px'},
                render: (value, record) => {
                    return <TreeSelect
                        placeholder={"Please Select"}
                        multiple={true}
                        treeCheckable={true}
                        allowClear={true}
                        treeData={translateResponse(value)}
                    />;
                }
            },
            {
                title: t['componentList.label.admins'],
                dataIndex: 'user',
                render: (value) => {
                    return (<UserGroup users={[value]}/>);
                },
            },
            {
                title: t['componentList.label.operations'],
                dataIndex: 'operations',
                headerCellStyle: {width: '250px'},
                render: (value, record) => {
                    let updateButton;
                    let deleteButton;
                    if(record.permissions.includes('ManageAble')){
                        updateButton = <Button key={getRandomString()}
                                               onClick={() => callback(record, 'update')}
                                               type="text"
                                               size="mini">
                            {t['componentList.columns.button.update']}
                        </Button>;
                        deleteButton =
                            <Popconfirm key={getRandomString()}
                                        focusLock
                                        position={"tr"}
                                        title='Confirm'
                                        content={t['componentList.form.delete.confirm']}
                                        onOk={() => callback(record, 'delete')}
                            >
                                <Button
                                    type="text"
                                    size="mini">
                                    {t['componentList.columns.button.delete']}
                                </Button>
                            </Popconfirm>
                    }
                    return  <Space size={0} direction="horizontal">{[updateButton,deleteButton]}</Space>;
                }
            }
        ];
    }
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
                <div className={styles['search-form-wrapper']}>
                <Form
                    form={form}
                    className={styles['search-form']}
                    labelAlign="left"
                    autoComplete={'off'}
                    wrapperCol={{ span: 24 }}
                >
                    <Row gutter={24}>
                        <Col span={7}>
                            <Form.Item field="Title">
                                <Input.Search  placeholder={t['componentList.label.title']} allowClear onSearch={(v) => {handleSearch({search:v})}} />
                            </Form.Item>
                        </Col>
                        <Grid.Col span={17} style={{ textAlign: 'right' }}>
                            <Space>
                                <Button size={"small"} type="primary" onClick={() => setShowCreateModal(true)}>{t['componentList.button.create']}</Button>
                            </Space>
                        </Grid.Col>
                    </Row>
                </Form>
                </div>
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