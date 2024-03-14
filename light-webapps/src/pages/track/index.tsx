import {
    Breadcrumb,
    Card, Input, Space,
} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "./locale";
import SearchForm from "@/pages/stat/list/form";
import {IconHome} from "@arco-design/web-react/icon";
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
    const [searchForm,setSearchForm] = useState({"date":["2024-03-14 14:31:35","2024-03-14 14:31:35"],"captcha":["0"],"groupId":"100288","t":1710398201616});
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

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <BreadcrumbItem>
                    <IconHome />
                </BreadcrumbItem>
                <BreadcrumbItem style={{fontWeight:20}}>{t['statTrack.breadcrumb.title']}</BreadcrumbItem>
            </Breadcrumb>
            <div className={styles.wrapper}>
                <Space size={16} direction="vertical" className={styles.left}>
                    <Card>
                        {statInfo && <ChartPanel parentLoading={false} statInfo={statInfo} size={'default'} searchForm={{date:['2024-03-14','2024-03-14'],groupId:23,captcha:0}}/>}
                    </Card>
                </Space>

                <Space size={16} direction="vertical" className={styles.right}>
                    <TextArea
                        placeholder='Please enter ...'
                        readOnly={true}
                        style={{ width: '100%',height:'300px',backgroundColor:'#373434',color:'white' }}
                        defaultValue='This is the contents of the textarea. This is the contents of the textarea. This is the contents of the textarea. '
                    />
                </Space>
            </div>
        </>
    );
}