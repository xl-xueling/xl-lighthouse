import React, {useEffect, useMemo, useState} from "react";
import {Caller} from "@/types/caller";
import {
    Breadcrumb,
    Button,
    Card,
    Grid,
    Input,
    Notification,
    PaginationProps,
    Radio,
    Space,
    Spin,
    Table
} from "@arco-design/web-react";
import {getColumns} from "@/pages/caller/list/Constants";
import {requestChangeState, requestDeleteById, requestList} from "@/api/caller";
import CallerCreateModal from "@/pages/caller/create";
import locale from "./locale/index"
import useLocale from "@/utils/useLocale";
import {useHistory} from 'react-router-dom';
import {useUpdateEffect} from "ahooks";
import SearchForm from "./forms";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useSelector} from "react-redux";
import {TreeNode} from "@/types/insights-web";
import {GlobalState} from "@/store";
import {CallerStateEnum} from "@/types/insights-common";

const BreadcrumbItem = Breadcrumb.Item;
const InputSearch = Input.Search;

export default function CallerListPanel(){

    const t = useLocale(locale);
    const [form] = useForm();
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const [formParams, setFormParams] = useState<any>({});
    const [loading, setLoading] = useState(true);
    const [showCreateModal,setShowCreateModal] = useState<boolean>(false);
    const [showUpdateModal,setShowUpdateModal] = useState<boolean>(false);
    const [listData,setListData] = useState<Array<Caller>>([]);
    const [current,setCurrent] = useState<Caller>(null);
    const [refreshTime,setRefreshTime] = useState<number>(Date.now);
    const history = useHistory();
    const [owner,setOwner] = useState(1);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);

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
        } else if(type == 'frozen'){
            await handlerChangeState(record.id,CallerStateEnum.Frozen).then();
        } else if(type == 'restart'){
            await handlerChangeState(record.id,CallerStateEnum.Normal).then();
        }
    }

    const columns = useMemo(() => getColumns(t,tableCallback,userInfo), [t]);

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

    const handlerChangeState = async (callerId: number,state:CallerStateEnum) => {
        await requestChangeState({"id":callerId,"state":state}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                let message;
                if(state == CallerStateEnum.Frozen){
                    message = t['callerList.columns.operations.frozen.success'];
                }else if(state == CallerStateEnum.Normal){
                    message = t['callerList.columns.operations.restart.success'];
                }
                Notification.info({style: { width: 420 }, title: 'Notification', content: message});
                setRefreshTime(Date.now());
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    };

    const fetchData = async () => {
        setLoading(true);
        const {current, pageSize} = pagination;
        const combineParams:any = {}
        combineParams.search = formParams.search;
        combineParams.departmentIds = formParams.departmentIds && formParams.departmentIds.length > 0 ? formParams.departmentIds: null;
        combineParams.ownerId = owner == 1?userInfo?.id:null;
        await requestList({
            queryParams:combineParams,
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

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    function handleChangeOwnerType(p){
        setOwner(p);
    }

    useEffect(() => {
        fetchData().then();
    },[refreshTime])

    useUpdateEffect(() => {
        setRefreshTime(Date.now());
    },[pagination.current, pagination.pageSize])

    useUpdateEffect(() => {
        setPagination({
            ...pagination,
            current : 1,
        });
        setRefreshTime(Date.now());
    },[JSON.stringify(formParams)])

    useUpdateEffect(() => {
        setPagination({
            ...pagination,
            current : 1
        });
        setTimeout(() => {
            setRefreshTime(Date.now());
        },0)
    },[owner])

    function handleSearch(params) {
        setFormParams({...params,t:Date.now()});
    }

    function handleReset(){
        form.resetFields();
        handleSearch({})
    }

    return (<>
        <Spin loading={loading} style={{ display: 'block' }}>
            <Card>
                <SearchForm onSearch={handleSearch} onClear={handleReset} allDepartInfo={allDepartInfo} form={form}/>
                <Grid.Row justify="space-between" align="center" style={{marginBottom:'15px'}}>
                    <Grid.Col span={16} style={{ textAlign: 'left' }}>
                        <Space>
                            <Radio.Group defaultValue={"1"} name='button-radio-group' onChange={handleChangeOwnerType}>
                                {[{value:"1",label:t['callerList.operations.my.callers']},{value:"0",label:t['callerList.operations.all.callers']}].map((item) => {
                                    return (
                                        <Radio key={item.value} value={item.value}>
                                            {({ checked }) => {
                                                return (
                                                    <Button size={"small"} tabIndex={-1} key={item.value} shape='round' style={checked ? {color:'rgb(var(--primary-6)',fontWeight:500}:{fontWeight:500}}>
                                                        {item.label}
                                                    </Button>
                                                );
                                            }
                                            }
                                        </Radio>
                                    );
                                })}
                            </Radio.Group>
                        </Space>
                    </Grid.Col>
                    <Grid.Col span={8} style={{ textAlign: 'right' }}>
                        <Button size={"small"} type="primary" onClick={() => setShowCreateModal(true)}>{t['callerList.button.create']}</Button>
                    </Grid.Col>
                </Grid.Row>
                <Table rowKey={'id'} size={"small"} columns={columns} data={listData} pagination={pagination} onChange={onChangeTable} />
            </Card>
        </Spin>
        {showCreateModal && <CallerCreateModal onClose={() => setShowCreateModal(false)} onSuccess={() => setRefreshTime(Date.now())}/>}
    </>)
}