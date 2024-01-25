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
import React, {useEffect, useMemo, useState} from 'react';
import locale from './locale';
import styles from './style/index.module.less';
import useLocale from "@/utils/useLocale";
import {Project, Relation} from "@/types/insights-web";
import {requestBindList, requestBindRemove} from "@/api/metricset";
import {CiLock} from "react-icons/ci";
import {OwnerTypeEnum, ResourceTypeEnum} from "@/types/insights-common";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import DepartmentLabel from "@/pages/department/common/depart";
import {getRandomString} from "@/utils/util";
import {getColumns} from "./constants";
import {requestDeleteById} from "@/api/project";
const { Row, Col } = Grid;
const { Text } = Typography;

export default function MetricBindedList({metricSetInfo}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState<Relation[]>([]);
    const [loading,setLoading] = useState<boolean>(true);
    const [searchForms,setSearchForms] = useState<any>({});

    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [10,20,30,50],
        sizeCanChange: false,
        showTotal: true,
        pageSize: 5,
        current: 1,
        pageSizeChangeResetCurrent: false,
    });

    const tableCallback = async (record, type) => {
        if(type == 'remove'){
            await handleRemove(record.id);
        }
    }

    const handleRemove = async (relationId) => {
        const removeParam = {
            id:metricSetInfo?.id,
            relationId:relationId,
        }
        console.log("removeParam is:" + JSON.stringify(removeParam));
        await requestBindRemove(removeParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.delete.submit.success']});
                const updatedList = listData.filter(x => x.id != relationId);
                setListData(updatedList);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
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
    },[pagination.current, pagination.pageSize,JSON.stringify(searchForms)])

    return (
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
                        <Space>
                            <Button size={"small"} type="primary" icon={<IconPlus />}>绑定</Button>
                        </Space>
                    </Grid.Col>
                </Row>
            </Form>
        <Table rowKey={'id'} size={"small"} onChange={onChangeTable} loading={loading} columns={columns} data={listData}/>
        {/*<AddBindedPanel metricId={0} />*/}
        </Card>
    );
}