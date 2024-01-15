import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {Card, Typography, Grid, Space, Tabs, Divider, Notification, Breadcrumb} from '@arco-design/web-react';
import PreviewHeader from "@/pages/metricset/preview/header";
import {IconDashboard, IconHome, IconTag, IconThunderbolt} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {requestQueryById} from "@/api/metricset";
import {MetricSet} from "@/types/insights-web";
import MetricSetPreviewPanel from "@/pages/metricset/preview/panel_dashboard/dashboard";
import MetricBindedList from "@/pages/metricset/binded/list";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

export default function MetricSetPreview() {

    const { id } = useParams();
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const [metricSetInfo,setMetricSetInfo] = useState<MetricSet>(null);

    const fetchMetricSetInfo = async (): Promise<void> => {
        setLoading(true);
        console.log("fetchMetricInfo,id:" + id);
        await requestQueryById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setMetricSetInfo(data);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        console.log("fetch metric..")
        fetchMetricSetInfo().then();
    },[])

    return (
        <>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
            <Breadcrumb.Item>
                <IconHome />
            </Breadcrumb.Item>
            <Breadcrumb.Item style={{fontWeight:20}}>{t['metricSetPreview.breadcrumb']}</Breadcrumb.Item>
        </Breadcrumb>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            <Card>
                <PreviewHeader metricSetInfo={metricSetInfo}/>
            </Card>
            <Tabs
                type="line">
                <TabPane
                    key='1'
                    title={
                        <span>
                            <IconDashboard style={{ marginRight: 6 }} />
                            Data View
                        </span>
                    }>
                    <MetricSetPreviewPanel metricSetInfo={metricSetInfo}/>
                </TabPane>
                <TabPane
                    key='2'
                    title={
                        <span>
                            <IconThunderbolt style={{ marginRight: 6 }} />
                            Binded Items
                        </span>
                    }>
                    <MetricBindedList metricId={metricSetInfo?.id}/>
                </TabPane>
                <TabPane key='3' title={
                    <span>
                        <IconTag style={{ marginRight: 6 }} />
                        Authority Info
                  </span>}>
                    {/*<GroupBasicPanel groupId={0}/>*/}
                </TabPane>
            </Tabs>
        </Space>
        </>
    );
}