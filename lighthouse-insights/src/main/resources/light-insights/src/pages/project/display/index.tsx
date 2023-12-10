import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Card, Grid, Space, Typography} from "@arco-design/web-react";
import Overview from "@/pages/dashboard/workplace/overview";
import PopularContents from "@/pages/dashboard/workplace/popular-contents";
import ContentPercentage from "@/pages/dashboard/workplace/content-percentage";
import Shortcuts from "@/pages/dashboard/workplace/shortcuts";
import Carousel from "@/pages/dashboard/workplace/carousel";
import Announcement from "@/pages/dashboard/workplace/announcement";
import Docs from "@/pages/dashboard/workplace/docs";
import ProjectTree from "@/pages/project/display/tree";
import SearchForm from "@/pages/stat/display/search_form";
import ChartPanel from "@/pages/stat/display/chart_panel";
import BasicInfo from "@/pages/stat/display/basic";
const { Row, Col } = Grid;

export default function ProjectDisplay() {

    const { id } = useParams();

    useEffect(() => {
        console.log("project display..")
    },[])

    return (
        <div className={styles.wrapper}>
            <Space size={16} direction="vertical" className={styles.left}>
                <Row gutter={16}>
                    <ProjectTree />
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