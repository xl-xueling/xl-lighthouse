import React from 'react';
import {Card, Typography, Grid, Space} from '@arco-design/web-react';
import MetricManagePanel from "./metricset_manage";
import MetricNewDetail from "@/pages/metricset/manage/new_detail";


const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});

export default function Index() {

    return (
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            <Card>
                <MetricNewDetail/>
            </Card>
            <Card>
                <MetricManagePanel groupId={0}/>
            </Card>
        </Space>
    );
}