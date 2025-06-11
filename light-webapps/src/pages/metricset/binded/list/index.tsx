import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card, Table, TableColumnProps, Space, Divider, PaginationProps, Notification
} from '@arco-design/web-react';
import {
    IconDownCircle, IconPlus, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useContext, useEffect, useMemo, useState} from 'react';
import locale from './locale';
import styles from './style/index.module.less';
import useLocale from "@/utils/useLocale";
import {Project, Relation} from "@/types/insights-web";
import {requestBindList, requestBindRemove} from "@/api/metricset";
import {CiLock} from "react-icons/ci";
import {OwnerTypeEnum, PermissionEnum, ResourceTypeEnum} from "@/types/insights-common";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import DepartmentLabel from "@/pages/department/common/depart";
import {getRandomString} from "@/utils/util";
import {getColumns} from "./constants";
import {requestDeleteById} from "@/api/project";
import ApplyModal from "@/pages/order/apply/modal/ApplyModal";
import NewMetricBindedModal from "@/pages/metricset/binded/binded";
import {MetricSetBindListContext, MetricSetPreviewContext} from "@/pages/common/context";
const { Row, Col } = Grid;
const { Text } = Typography;


export default function MetricSetBindListPanel() {

    const t = useLocale(locale);
    const { metricSetInfo, setMetricSetInfo,PRO_ViewBindTab,PRO_ProjectApplyModal,PRO_StatApplyModal} = useContext(MetricSetPreviewContext);
    const { reloadTime, setReloadTime } = useContext(MetricSetPreviewContext);
    const [listData, setListData] = useState<Relation[]>([]);
    const [loading,setLoading] = useState<boolean>(true);
    const [searchForms,setSearchForms] = useState<any>({});
    const [showApplyModal,setShowApplyModal] = useState<boolean>(false);
    const [showBindModal,setShowBindModal] = useState<boolean>(false);
    const [currentRecord,setCurrentRecord] = useState<Relation>(null);
    const [needReload,setNeedReload] = useState<boolean>(false);


    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,30],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const handleMetricBindListReloadCallback = () => {
        // setReloadTime(Date.now());
        setNeedReload(true);
    }

    const tableCallback = async (record, type) => {
        if(type == 'remove'){
            await handleRemove(record.id);
        }else if(type == 'apply'){
            setCurrentRecord(record);
            setShowApplyModal(true);
        }
    }


    const handleRemove = async (relationId) => {
        const removeParam = {
            id:metricSetInfo?.id,
            relationId:relationId,
        }
        await requestBindRemove(removeParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['bindedList.list.column.label.operations.remove.submit.success']});
                const updatedList = listData.filter(x => x.id != relationId);
                setListData(updatedList);
                setReloadTime(Date.now());
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerShowBindModal = () => {
        setShowBindModal(true);
    }

    const columns = useMemo(() => getColumns(t, metricSetInfo,tableCallback), [t,listData]);

    const fetchData = async () => {
        setLoading(true);
        const {current, pageSize} = pagination;
        const requestParam = {
            id:metricSetInfo?.id,
            search:searchForms.search
        }
        await requestBindList({
            queryParams:requestParam,
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
        }).catch((error) => {
            console.log(error);
        }).finally(() => {
            setLoading(false);
        })
    }

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const handlerSearch = (search) => {
        setPagination({ ...pagination, current: 1 });
        setSearchForms({search});
    }

    useEffect(() => {
        fetchData().then();
    },[reloadTime,pagination.current, pagination.pageSize,JSON.stringify(searchForms)])

    return (
        <MetricSetBindListContext.Provider value={{needReload,setNeedReload,handleMetricBindListReloadCallback}}>
        <Card>
            <Form
                className={styles['search-form']}
                labelAlign="left"
                style={{marginTop:8}}
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 19 }}
            >
                <Row gutter={24}>
                    <Col span={8}>
                        <Form.Item field="Title">
                            <Input.Search autoComplete={'off'} size={"small"} placeholder={'Search Title'} allowClear onSearch={handlerSearch}/>
                        </Form.Item>
                    </Col>
                    <Grid.Col span={16} style={{ textAlign: 'right' }}>
                        {
                            metricSetInfo?.permissions.includes(PermissionEnum.ManageAble)?
                                <Space>
                                    <Button size={"small"} type="primary" onClick={handlerShowBindModal}>{t['bindedList.list.button.bind']}</Button>
                                </Space>
                                :null
                        }
                    </Grid.Col>
                </Row>
            </Form>
        <Table rowKey={'id'} size={"small"} onChange={onChangeTable} loading={loading} pagination={pagination} columns={columns} data={listData}/>
        {showApplyModal && <ApplyModal itemInfo={currentRecord?.extend} resourceType={currentRecord.resourceType} onClose={() => setShowApplyModal(false)}/>}
        {showBindModal && <NewMetricBindedModal metricSetInfo={metricSetInfo} PRO_ViewBindTab={PRO_ViewBindTab} onClose={() =>
        {
            setShowBindModal(false);
            if(needReload){
                setReloadTime(Date.now());
                setNeedReload(false);
            }
        }
        }/>}
        </Card>
        </MetricSetBindListContext.Provider>
    );
}