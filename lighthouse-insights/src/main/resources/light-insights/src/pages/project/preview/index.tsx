import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {
    Button,
    Card,
    DatePicker,
    Divider,
    Grid,
    Space,
    Typography,
    Spin,
    Notification,
    Breadcrumb
} from "@arco-design/web-react";
import Overview from "@/pages/dashboard/workplace/overview";
import PopularContents from "@/pages/dashboard/workplace/popular-contents";
import ContentPercentage from "@/pages/dashboard/workplace/content-percentage";
import Shortcuts from "@/pages/dashboard/workplace/shortcuts";
import Carousel from "@/pages/dashboard/workplace/carousel";
import Announcement from "@/pages/dashboard/workplace/announcement";
import Docs from "@/pages/dashboard/workplace/docs";
import ProjectMenu from "@/pages/project/preview/menu";
import SearchForm from "@/pages/stat/display/search_form";
import ChartPanel from "@/pages/stat/display/chart_panel";
import BasicInfo from "@/pages/stat/display/basic";
const { Row, Col } = Grid;
import { RiAppsLine } from "react-icons/ri";
import PreviewHeader from "@/pages/project/preview/head";
import {ArcoTreeNode, MetricSet, Project} from "@/types/insights-web";
import {requestList, requestQueryById} from "@/api/project";
import {requestPrivilegeCheck} from "@/api/privilege";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {IconHome, IconTag} from "@arco-design/web-react/icon";
import { LoadingOutlined } from '@ant-design/icons';
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import StatPreviewPanel from "@/pages/stat/display/preview";


export default function ProjectPreview() {

    const { id } = useParams();
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(true);
    const [projectInfo,setProjectInfo] = useState<Project>(null);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);

    const handlerCallback = async (type,record) => {
        if(type == 'clickStatMenu'){
            console.log("type is:" + type + ",record is:" + JSON.stringify(record))
            setSelectedStatId(Number(record));
        }

    }

    const fetchProjectInfo = async (): Promise<void> => {
        setLoading(true);
        await requestQueryById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                console.log("projectInfo is:" + JSON.stringify(data));
                setProjectInfo(data);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }


    useEffect(() => {
        fetchProjectInfo().then();
    },[])

    return (
        <>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
            <Breadcrumb.Item>
                <IconHome />
            </Breadcrumb.Item>
            <Breadcrumb.Item style={{fontWeight:20}}>{t['projectPreview.breadcrumb']}</Breadcrumb.Item>
        </Breadcrumb>
        <Spin loading={loading} style={{display:'block'}}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <Card>
                    <PreviewHeader projectInfo={projectInfo}/>
                </Card>
                <div className={styles.wrapper}>
                <Space size={16} direction="vertical" className={styles.left}>
                    <Row>
                        <ProjectMenu projectInfo={projectInfo} callback={handlerCallback} />
                    </Row>
                </Space>
                <Space className={styles.right} size={16} direction="vertical">
                    {selectedStatId && <StatPreviewPanel id={selectedStatId}/>}
                </Space>
            </div>
            </Space>
        </Spin>
        </>
    );
}