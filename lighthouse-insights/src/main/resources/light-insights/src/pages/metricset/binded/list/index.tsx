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
import React, {useEffect, useState} from 'react';
import locale from './locale';
import styles from './style/index.module.less';
import useLocale from "@/utils/useLocale";
import {Project, Relation} from "@/types/insights-web";
import {requestBindList} from "@/api/metricset";
import {CiLock} from "react-icons/ci";
import {OwnerTypeEnum, ResourceTypeEnum} from "@/types/insights-common";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import DepartmentLabel from "@/pages/department/common/depart";
import {getRandomString} from "@/utils/util";
const { Row, Col } = Grid;
const { Text } = Typography;

export default function MetricBindedList({metricId}) {

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

    const columns: TableColumnProps[] = [
        {
            title: t['bindedList.list.column.label.id'],
            dataIndex: 'id',
        },
        {
            title: t['bindedList.list.column.label.elementType'],
            dataIndex: 'relationType',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        t['bindedList.list.elementType.project']
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        t['bindedList.list.elementType.stat']
                    )
                }

            }
        },
        {
            title:t['bindedList.list.column.label.title'],
            dataIndex: 'title',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        record.extend?.title
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        record.extend?.title
                    )
                }
            }
        },
        {
            title: t['bindedList.list.column.label.department'],
            dataIndex: 'department',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        <DepartmentLabel departmentId={record.extend?.departmentId} />
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        <DepartmentLabel departmentId={record.extend?.project?.departmentId} />
                    )
                }
            }
        },
        {
            title: t['bindedList.list.column.label.admins'],
            dataIndex: 'admins',
            render: (value,record) =>
            {
                return (<UserGroup users={record.extend?.admins}/>)
            }
        },
        {
            title: t['bindedList.list.column.label.bindTime'],
            dataIndex: 'createTime',
            render: (value,record) =>
            {
                return (formatTimeStamp(value,DateTimeFormat))
            }
        },
        {
            title: t['bindedList.list.column.label.operations'],
            dataIndex: 'operations',
            headerCellStyle: {width:'250px' },
            render: (_, record) => {
                return (
                <Space size={0} direction="horizontal">
                <Button key={getRandomString()}
                               type="text"
                               size="mini">
                    {t['bindedList.list.column.label.operations.remove']}
                </Button>
                    <Button key={getRandomString()}
                            type="text"
                            size="mini">
                        {t['bindedList.list.column.label.operations.apply']}
                    </Button>
                </Space>
                );
            }
        }
    ];

    const fetchData = async () => {
        setLoading(true);
        const requestParam = {
            id:metricId,
            search:searchForms.search,
        }
        await requestBindList(requestParam)
            .then((response) => {
                const {code, data ,message} = response;
                if(code == '0'){
                    setListData(data);
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