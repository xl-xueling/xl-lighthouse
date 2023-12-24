import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Button, Card, DatePicker, Divider, Grid, Space, Typography,Spin} from "@arco-design/web-react";
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
import {ArcoTreeNode, MetricSet, PermissionsEnum, Project} from "@/types/insights-web";
import {requestList, requestQueryByIds} from "@/api/project";
import {requestPrivilegeCheck} from "@/api/privilege";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {IconTag} from "@arco-design/web-react/icon";
import StatDisplayMode1 from "@/pages/stat/display/display_mode1";
import { LoadingOutlined } from '@ant-design/icons';


export default function ProjectDisplay() {

    const { id } = useParams();
    const [loading,setLoading] = useState<boolean>(true);
    const [projectInfo,setProjectInfo] = useState<Project>(null);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);

    const fetchProjectInfo:Promise<Project> = new Promise<Project>((resolve,reject) => {
        const proc = async () => {
            const result = await requestQueryByIds({ids:[id]});
            resolve(result?.data?.[id]);
        }
        proc().then();
    })

    const fetchPrivilegeInfo = async(ids) => {
        return new Promise<Record<number,PermissionsEnum[]>>((resolve,reject) => {
            requestPrivilegeCheck({type:"project",ids:ids}).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    const menuCallback = async (id) => {
        setSelectedStatId(Number(id));
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const result = await Promise.all([fetchProjectInfo]);
        const projectInfo = result[0];
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
        <Spin loading={loading} style={{display:'block'}}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <Card>
                    <DisplayHeader projectInfo={projectInfo}/>
                </Card>
                <div className={styles.wrapper}>
                <Space size={16} direction="vertical" className={styles.left}>
                    <Row>
                        <ProjectMenu structure={projectInfo?.structure} callback={menuCallback} />
                    </Row>
                </Space>
                <Space className={styles.right} size={16} direction="vertical">
                    <StatDisplayMode1 statId={selectedStatId}/>
                </Space>
            </div>
            </Space>
        </Spin>

            // <Space size={16} direction="vertical" style={{ width: '100%'}}>
            //     <Card>
            //         <DisplayHeader projectInfo={projectInfo}/>
            //     </Card>
            //     <div className={styles.wrapper}>
            //     <Space size={16} direction="vertical" className={styles.left}>
            //         <Row>
            //             <ProjectMenu structure={projectInfo?.structure} callback={menuCallback} />
            //         </Row>
            //     </Space>
            //     <Space className={styles.right} size={16} direction="vertical">
            //         <StatDisplayMode1 statId={selectedStatId}/>
            //     </Space>
            // </div>
            // </Space>
    );
}