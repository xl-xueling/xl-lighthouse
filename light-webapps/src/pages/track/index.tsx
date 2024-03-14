import {
    Breadcrumb,
    Card, Checkbox, Grid, Input, Space, Switch, Table, TableColumnProps,
} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "./locale";
import SearchForm from "@/pages/stat/list/form";
import {IconCheck, IconClose, IconHome, IconPlus} from "@arco-design/web-react/icon";
import styles from "./style/index.module.less";
import Overview from "@/pages/dashboard/workplace/overview";
import Shortcuts from "@/pages/dashboard/workplace/shortcuts";
import StatPieChart from "@/pages/dashboard/workplace/StatPieChart";
import {Stat} from "@/types/insights-web";
import {requestQueryById} from "@/api/stat";
import {useParams} from "react-router-dom";
import {getDataWithLocalCache} from "@/utils/localCache";
import ChartPanel from "@/pages/stat/preview/chart_panel";
const BreadcrumbItem = Breadcrumb.Item;

export default function TrackStatPage() {

    const t = useLocale(locale);
    const [formParams, setFormParams] = useState({});
    const TextArea = Input.TextArea;
    const [statInfo,setStatInfo] = useState(null);
    const [searchForm,setSearchForm] = useState({"date":["2024-03-14 14:31:35","2024-03-14 14:31:35"],"captcha":["0","1","2"],"groupId":"100288","t":1710398201616});
    const {id} = useParams();

    async function actualFetchStatInfo():Promise<Stat> {
        return new Promise<Stat>((resolve,reject) => {
            requestQueryById({id:1013}).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    async function fetchStatInfo() {
        const statInfo = await getDataWithLocalCache('cache_cluster_monitor_1013',300,actualFetchStatInfo);
        console.log("statInfo:" + JSON.stringify(statInfo));
        setStatInfo(statInfo);
    }

    useEffect(() => {
        console.log("id:" + id);
        fetchStatInfo().then()
    },[id])

    function handleSearch(params) {
        setFormParams(params);
    }

    const columns: TableColumnProps[] = [
        {
            title: 'Name',
            dataIndex: 'name',
        },
        {
            title: 'Salary',
            dataIndex: 'salary',
        },
        {
            title: 'Address',
            dataIndex: 'address',
        },
        {
            title: 'Email',
            dataIndex: 'email',
        },
    ];
    const data = [
        {
            key: '1',
            name: 'Jane Doe',
            salary: 23000,
            address: '32 Park Road, London',
            email: 'jane.doe@example.com',
        },
        {
            key: '2',
            name: 'Alisa Ross',
            salary: 25000,
            address: '35 Park Road, London',
            email: 'alisa.ross@example.com',
        },
        {
            key: '3',
            name: 'Kevin Sandra',
            salary: 22000,
            address: '31 Park Road, London',
            email: 'kevin.sandra@example.com',
        },
        {
            key: '4',
            name: 'Ed Hellen',
            salary: 17000,
            address: '42 Park Road, London',
            email: 'ed.hellen@example.com',
        },
        {
            key: '5',
            name: 'William Smith',
            salary: 27000,
            address: '62 Park Road, London',
            email: 'william.smith@example.com',
        },
    ];


    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <BreadcrumbItem>
                    <IconHome />
                </BreadcrumbItem>
                <BreadcrumbItem style={{fontWeight:20}}>{t['statTrack.breadcrumb.title']}</BreadcrumbItem>
            </Breadcrumb>
            <Space size={16} style={{width:'100%'}} direction="vertical">

                <div className={styles.wrapper}>
                    <Space size={16} direction="vertical" className={styles.left}>
                        <Card style={{height:'340px'}}>
                            {statInfo && <ChartPanel parentLoading={false} statInfo={statInfo} size={'default'} searchForm={searchForm}/>}
                        </Card>
                    </Space>
                    <Space size={16} direction="vertical" className={styles.right}>
                        <TextArea
                            placeholder='Please enter ...'
                            readOnly={true}
                            style={{ width: '100%',height:'340px',backgroundColor:'#373434',color:'white' }}
                            defaultValue='This is the contents of the textarea. This is the contents of the textarea. This is the contents of the textarea. '
                        />
                    </Space>
                </div>

                <Card style={{height:'50px'}}>
                    <Grid.Row gutter={8}>
                        <Grid.Col span={14}>
                            调试模式： <Switch checkedIcon={<IconCheck />} uncheckedIcon={<IconClose />} defaultChecked size={"small"} />
                            <span style={{marginLeft:'30px'}}>
                            当前统计组：behavior_stat，统计项：每分钟_uv数据统计
                            </span>
                        </Grid.Col>
                        <Grid.Col span={10} style={{textAlign:"right"}}>
                            <Grid.Row gutter={8}>
                                <Grid.Col span={18}>

                                </Grid.Col>
                                <Grid.Col span={6}>
                                    <Checkbox/>自动刷新
                                </Grid.Col>
                            </Grid.Row>
                        </Grid.Col>
                    </Grid.Row>
                </Card>

                <Card>
                    <Table columns={columns} data={data} />
                </Card>
            </Space>


        </>
    );
}