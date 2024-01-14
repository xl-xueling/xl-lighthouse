import React from 'react';
import {Card, Typography, Grid, Space, Tabs, Divider} from '@arco-design/web-react';
import MetricNewDetail from "@/pages/metricset/preview/new_detail";
import {IconDashboard, IconTag, IconThunderbolt} from "@arco-design/web-react/icon";
import BindedList from "@/pages/metricset/binded/list/binded";
import GroupBasicPanel from "@/pages/group/basic";
import ProjectPreview from "@/pages/project/preview";


const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});
const TabPane = Tabs.TabPane;

export default function MetricSetPreview() {

    return (
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            <Card>
                <MetricNewDetail/>
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
                    <ProjectPreview/>
                </TabPane>
                <TabPane
                    key='2'
                    title={
                        <span>
                            <IconThunderbolt style={{ marginRight: 6 }} />
                            Binded Items
                        </span>
                    }>
                    <BindedList metricId={0}/>
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
    );
}