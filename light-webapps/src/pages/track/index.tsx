import {
    Breadcrumb,
    Card, Checkbox, Grid, Input, Notification, Space, Switch, Table, TableColumnProps,
} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from "./locale";
import {IconCheck, IconClose, IconHome, IconPlus} from "@arco-design/web-react/icon";
import styles from "./style/index.module.less";
import {Stat} from "@/types/insights-web";
import {requestChangeState, requestQueryById} from "@/api/stat";
import {requestEnableDebugMode, requestDisableDebugMode} from "@/api/track";
import {useParams} from "react-router-dom";
import {getDataWithLocalCache} from "@/utils/localCache";
import ChartPanel from "@/pages/stat/preview/chart_panel";
import {DebugModeEnum} from "@/types/insights-common";
const BreadcrumbItem = Breadcrumb.Item;

export default function TrackStatPage() {

    const t = useLocale(locale);
    const [formParams, setFormParams] = useState({});
    const [loading,setLoading] = useState<boolean>(false);
    const TextArea = Input.TextArea;
    const [monitorStatInfo,setMonitorStatInfo] = useState(null);
    const [statInfo,setStatInfo] = useState(null);
    const [groupId,setGroupId] = useState<number>(null);
    const [searchForm,setSearchForm] = useState({"date":["2024-03-14 14:31:35","2024-03-14 14:31:35"],"captcha":["0","1","2"],"groupId":"100288","t":1710398201616});
    const {id} = useParams();

    async function actualFetchMonitorStatInfo():Promise<Stat> {
        return new Promise<Stat>((resolve,reject) => {
            requestQueryById({id:1013}).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    async function fetchMonitorStatInfo() {
        const monitorStatInfo = await getDataWithLocalCache('cache_cluster_monitor_1013',300,actualFetchMonitorStatInfo);
        setMonitorStatInfo(monitorStatInfo);
    }

    const fetchData = async () => {
        setLoading(true);
        await requestQueryById({id:id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                console.log("data:" + JSON.stringify(data));
                setStatInfo(data)
                setGroupId(data.groupId);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }



    useEffect(() => {
        console.log("groupId is:" + groupId);
        if(groupId){
            enableDebugMode(groupId).then();
        }
    },[groupId])


    const enableDebugMode = async (groupId:number) => {
        console.log("start enable group,groupId:" + groupId);
        const changeParam = {
            id:groupId,
        }
        await requestEnableDebugMode(changeParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){

                Notification.info({style: { width: 420 }, title: 'Notification', content: ""});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error)=>{
            console.log(error);
        })
    }

    const disableDebugMode = async (groupId:number) => {
        const changeParam = {
            id:groupId,
        }
        await requestDisableDebugMode(changeParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: ""});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error)=>{
            console.log(error);
        })
    }


    useEffect(() => {
        console.log("id:" + id);
        fetchMonitorStatInfo().then()
        fetchData().then();
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
                            {monitorStatInfo && <ChartPanel parentLoading={false} statInfo={monitorStatInfo} size={'default'} searchForm={searchForm}/>}
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