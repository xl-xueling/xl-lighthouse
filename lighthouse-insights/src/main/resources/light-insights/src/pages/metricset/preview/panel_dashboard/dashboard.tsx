import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {Card, Typography, Grid, Space, Tabs, Divider, Notification, Breadcrumb, Spin} from '@arco-design/web-react';
import PreviewHeader from "@/pages/metricset/preview/header";
import {IconDashboard, IconHome, IconTag, IconThunderbolt} from "@arco-design/web-react/icon";
import BindedList from "@/pages/metricset/binded/list/binded";
import GroupBasicPanel from "@/pages/group/basic";
import ProjectPreview from "@/pages/project/preview";
import useLocale from "@/utils/useLocale";
import locale from "../locale";
import {requestQueryById} from "@/api/metricset";
import {MetricSet} from "@/types/insights-web";
import styles from "@/pages/project/preview/style/index.module.less";
import ProjectMenu from "@/pages/project/preview/menu";
import StatPreviewPanel from "@/pages/stat/display/preview";
import DashboardMenu from "@/pages/metricset/preview/panel_dashboard/menu";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

export default function MetricSetPreviewPanel({metricSetInfo}) {
    const [loading,setLoading] = useState<boolean>(false);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);
    const handlerCallback = async (type,record) => {
        if(type == 'clickStatMenu'){
            setSelectedStatId(Number(record));
        }

    }

    return (
        <Spin loading={loading} style={{display:'block'}}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <div className={styles.wrapper}>
                    <Space size={16} direction="vertical" className={styles.left}>
                        <Row>
                            <DashboardMenu metricSetInfo={metricSetInfo} callback={handlerCallback}/>
                        </Row>
                    </Space>
                    <Space className={styles.right} size={16} direction="vertical">
                        {selectedStatId && <StatPreviewPanel id={selectedStatId}/>}
                    </Space>
                </div>
            </Space>
        </Spin>
    );
}