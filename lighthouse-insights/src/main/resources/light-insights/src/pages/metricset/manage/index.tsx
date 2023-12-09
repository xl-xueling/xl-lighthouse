import React from 'react';
import {Card, Typography, Grid} from '@arco-design/web-react';
import MetricManagePanel from "./metricset_manage";
import MetricNewDetail from "@/pages/metricset/manage/new_detail";


const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});

export default function Index() {

    return (
        <div style={{ minHeight:500}}>
            {/*<Card>*/}
            {/*    <MetricNewDetail/>*/}
            {/*    <div style={{marginBottom:'15px'}}></div>*/}
            {/*    <MetricManagePanel groupId={0}/>*/}
            {/*</Card>*/}

            <Card>
                <MetricNewDetail/>
                <MetricManagePanel groupId={0}/>
            </Card>
        </div>
    );
}