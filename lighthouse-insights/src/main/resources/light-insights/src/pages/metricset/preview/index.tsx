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
import {PiLinkSimple, PiTreeStructure} from "react-icons/pi";
import MetricSetStructure from "@/pages/metricset/structure";
import MetricSetPermissions from "@/pages/metricset/permissions";
import {ResourceTypeEnum} from "@/types/insights-common";
import {VscGistSecret} from "react-icons/vsc";
import {GlobalErrorCodes} from "@/utils/constants";
import ErrorPage from "@/pages/common/error";
import { FaRegChartBar } from "react-icons/fa";
import { AiOutlineDashboard } from "react-icons/ai";


export default function MetricSetPreview() {

    const { id } = useParams();
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const [metricSetInfo,setMetricSetInfo] = useState<MetricSet>(null);
    const [errorCode,setErrorCode] = useState<string>(null);

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        await requestQueryById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setMetricSetInfo(data);
            }else if(GlobalErrorCodes.includes(String(code))){
                setErrorCode(code);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        fetchData().then();
    },[])

    return (
        <>
            {
                errorCode ? <ErrorPage errorCode={errorCode}/>
                    :
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
                <span style={{display:"inline-flex",alignItems:"center"}}><IconDashboard style={{ marginRight: 6}} />{t['metricSetPreview.tab.title.dataView']}</span>
                </span>
            }>
                    <MetricSetPreviewPanel metricSetInfo={metricSetInfo}/>
                </TabPane>
                <TabPane
                key='2'
                title={
                <span>
                <span style={{display:"inline-flex",alignItems:"center"}}><PiLinkSimple style={{ marginRight: 6}} />{t['metricSetPreview.tab.title.bindItems']}</span>
                </span>
            }>
            {metricSetInfo && <MetricBindedList metricSetInfo={metricSetInfo}/>}
                </TabPane>
                <TabPane key='3' title={
                <span>
                <span style={{display:"inline-flex",alignItems:"center"}}><VscGistSecret style={{ marginRight: 6}} />{t['metricSetPreview.tab.title.permissions']}</span>
                </span>}>
                    <MetricSetPermissions resourceType={ResourceTypeEnum.Metric} resourceId={metricSetInfo?.id}/>
                </TabPane>
                <TabPane key='4' title={
                <span>
                <span style={{display:"inline-flex",alignItems:"center"}}><PiTreeStructure style={{ marginRight: 6}} />{t['metricSetPreview.tab.title.structure']}</span>
                </span>}>
            {metricSetInfo && <MetricSetStructure metricSetInfo={metricSetInfo}/>}
                </TabPane>
                </Tabs>
                </Space>
                </>
            }
        </>
    );
}