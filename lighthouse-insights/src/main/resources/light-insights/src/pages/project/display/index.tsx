import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Button, Card, DatePicker, Divider, Grid, Space, Typography} from "@arco-design/web-react";
import Overview from "@/pages/dashboard/workplace/overview";
import PopularContents from "@/pages/dashboard/workplace/popular-contents";
import ContentPercentage from "@/pages/dashboard/workplace/content-percentage";
import Shortcuts from "@/pages/dashboard/workplace/shortcuts";
import Carousel from "@/pages/dashboard/workplace/carousel";
import Announcement from "@/pages/dashboard/workplace/announcement";
import Docs from "@/pages/dashboard/workplace/docs";
import ProjectMenu from "@/pages/project/display/menu";
import SearchForm from "@/pages/stat/display/search_form";
import ChartPanel from "@/pages/stat/display/chart_panel";
import BasicInfo from "@/pages/stat/display/basic";
const { Row, Col } = Grid;
import { RiAppsLine } from "react-icons/ri";
import MetricNewDetail from "@/pages/metricset/manage/new_detail";
import DisplayHeader from "@/pages/project/display/head";
import {ArcoTreeNode, MetricSet, PrivilegeEnum, Project} from "@/types/insights-web";
import {requestList, requestQueryByIds} from "@/api/project";
import {requestPrivilegeCheck} from "@/api/privilege";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";

export default function ProjectDisplay() {

    const { id } = useParams();
    const [loading,setLoading] = useState<boolean>(true);
    const [projectInfo,setProjectInfo] = useState<Project>(null);

    const fetchProjectInfo:Promise<Project> = new Promise<Project>((resolve,reject) => {
        const proc = async () => {
            const result = await requestQueryByIds([id]);
            resolve(result.data[id]);
        }
        proc().then();
    })

    const fetchPrivilegeInfo = async(ids) => {
        return new Promise<Record<number,PrivilegeEnum[]>>((resolve,reject) => {
            requestPrivilegeCheck({type:"project",ids:ids}).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const result = await Promise.all([fetchProjectInfo]);
        const projectInfo = result[0];
        const adminIds = projectInfo.adminIds;
        Promise.all([fetchPrivilegeInfo([id])])
            .then(([r1]) => {
                const combinedItem = { ...projectInfo, ...{"permissions":r1[projectInfo.id]}};
                setProjectInfo(combinedItem);
                setLoading(false);
            }).catch((error) => {
                console.log(error);
            })
    }


    useEffect(() => {
        fetchData().then();
    },[])

    return (
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
        <Card>
            <DisplayHeader/>
        </Card>
        <div className={styles.wrapper}>
            <Space size={16} direction="vertical" className={styles.left}>
                <Row>
                    <ProjectMenu structure={projectInfo.structure} />
                </Row>
            </Space>
            <Space className={styles.right} size={16} direction="vertical">
                <Card>
                    <Row>
                        <Col span={12}>
                            <Typography.Title
                                heading={6}
                            >
                                {'每分钟uv数据统计'}
                            </Typography.Title>
                        </Col>
                    </Row>
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
        </Space>
    );
}