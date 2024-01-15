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
import {ResourceTypeEnum} from "@/types/insights-common";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import DepartmentLabel from "@/pages/department/common/depart";
import {getRandomString} from "@/utils/util";
const { Row, Col } = Grid;
const { Text } = Typography;

export default function MetricBindedList({metricId}) {

    const t = useLocale(locale);
    const [listData, setListData] = useState<Relation[]>([]);

    const columns: TableColumnProps[] = [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'ElementType',
            dataIndex: 'relationType',
            render: (value,record) =>
            {
                if(record.resourceType == ResourceTypeEnum.Project){
                    return (
                        '统计工程'
                    )
                }else if(record.resourceType == ResourceTypeEnum.Stat){
                    return (
                        '统计项'
                    )
                }

            }
        },
        {
            title: 'Title',
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
            title: 'Department',
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
            title: 'Admins',
            dataIndex: 'admins',
            render: (value,record) =>
            {
                return (<UserGroup users={record.extend?.admins}/>)
            }
        },
        {
            title: 'BindTime',
            dataIndex: 'createTime',
            render: (value,record) =>
            {
                return (formatTimeStamp(value,DateTimeFormat))
            }
        },
        {
            title: 'Operations',
            dataIndex: 'operations',
            headerCellStyle: {width:'250px' },
            render: (_, record) => {
                return (
                <Space size={0} direction="horizontal">
                <Button key={getRandomString()}
                               type="text"
                               size="mini">
                    {'移除'}
                </Button>
                    <Button key={getRandomString()}
                            type="text"
                            size="mini">
                        {'申请权限'}
                    </Button>
                </Space>
                );
            }
        }
    ];

    const fetchData = async () => {
        await requestBindList({id:metricId})
            .then((response) => {
                const {code, data ,message} = response;
                if(code == '0'){
                    setListData(data);
                }else{
                    Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                }
        }).catch((error) => {
            console.log(error);
        })
    }


    useEffect(() => {
        console.log("----metricId is:" + metricId)
    },[metricId])

    useEffect(() => {
        console.log("metricId is:" + metricId)
        fetchData().then();
    },[])

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
                            <Input.Search  placeholder={'Search Title'} allowClear />
                        </Form.Item>
                    </Col>
                    <Grid.Col span={16} style={{ textAlign: 'right' }}>
                        <Space>
                            <Button size={"small"} type="primary" icon={<IconPlus />}>绑定</Button>
                        </Space>
                    </Grid.Col>
                </Row>
            </Form>
        <Table rowKey={'id'} size={"small"} columns={columns} data={listData}/>
        {/*<AddBindedPanel metricId={0} />*/}
        </Card>
    );
}