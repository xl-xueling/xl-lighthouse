import React, {useEffect, useState} from 'react';
import styles from './style/index.module.less';
import {Card, Grid, Space, Tabs, Typography} from "@arco-design/web-react";
import GroupAddPanel from "@/pages/group/add/group_add";
import GroupManagePanel from "@/pages/group/manage";
import {useParams} from "react-router-dom";
import StatDisplayPanel from "@/pages/display/stat/stat_display";
import StatDisplay from "@/pages/stat/display";
import SearchForm from "@/pages/stat/display/search_form";
import ChartPanel from "@/pages/stat/display/chart_panel";
import BasicInfo from "@/pages/stat/display/basic";
import ProjectMenu from "@/pages/project/display/menu";
const { Row, Col } = Grid;

export default function MetricDisplay() {

    return (
        // <div style={{ minHeight:500 }}>
        //     <div className={styles.layout}>
        //         <div className={styles['layout-left-side']}>
        //             <Space size={24} direction="vertical" className={styles.left}>
        //                 <ProjectTree projectId={0} filterTypes={[1,2]} handlerProcess={null} />
        //             </Space>
        //             <StatDisplay />
        //         </div>
        //     </div>
        // </div>

        // <div style={{ minHeight:500 }}>
        //     <div className={styles.layout}>
        //         <div className={styles['layout-left-side']} style={{ border:'1px solid var(--color-neutral-3)'}}>
        //             <Space size={24} direction="vertical" className={styles.left}>
        //                 <ProjectTree projectId={0} filterTypes={[1,2]} handlerProcess={null} />
        //             </Space>
        //         </div>
        //         <StatDisplay />
        //     </div>
        // </div>

        <div className={styles.wrapper}>
            <Space size={16} direction="vertical" className={styles.left}>
                <Row gutter={16}>
                    <ProjectMenu />
                </Row>
            </Space>
            <Space className={styles.right} size={16} direction="vertical">
                <Card>
                    <Typography.Title
                        heading={6}
                    >
                        {'每分钟uv数据统计'}
                    </Typography.Title>
                    <SearchForm />
                    <ChartPanel />
                </Card>
                <Card>
                    <Typography.Title
                        heading={6}
                    >
                        {'Metric Information'}
                    </Typography.Title>
                    <BasicInfo />
                </Card>
            </Space>
        </div>
    );
}