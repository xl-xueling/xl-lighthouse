import React, {useContext, useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {Card, Typography, Grid, Space, Tabs, Divider, Notification, Breadcrumb, Spin} from '@arco-design/web-react';
import styles from "@/pages/project/preview/style/index.module.less";
import StatPreviewPanel from "@/pages/stat/display/preview";
import MetricSetDataViewMenu from "@/pages/metricset/preview/dataview/MetricSetDataViewMenu";
import {MetricSetPreviewContext} from "@/pages/metricset/preview";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

export default function MetricSetDataViewPanel({parentLoading}) {
    const { metricSetInfo, setMetricSetInfo } = useContext(MetricSetPreviewContext);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);

    const handlerCallback = async (type,record) => {
        if(type == 'clickStatMenu'){
            console.log("record is:" + record)
            setSelectedStatId(Number(record));
        }
    }

    return (
        <Spin loading={parentLoading} style={{display:'block'}}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <div className={styles.wrapper}>
                    <Space size={16} direction="vertical" className={styles.left}>
                        <Row>
                            <MetricSetDataViewMenu metricSetInfo={metricSetInfo} callback={handlerCallback}/>
                        </Row>
                    </Space>
                    <Space className={styles.right} size={16} direction="vertical">
                        {selectedStatId && <StatPreviewPanel size={'small'} id={selectedStatId}/>}
                    </Space>
                </div>
            </Space>
        </Spin>
    );
}