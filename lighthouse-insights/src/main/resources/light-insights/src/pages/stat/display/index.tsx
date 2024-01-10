import React, {useEffect} from 'react';
import {useParams} from "react-router-dom";
import SearchForm from "./search_form";
import {Breadcrumb, Card, Divider, Grid, Space, Typography} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import ChartPanel from "@/pages/stat/display/chart_panel";
import {IconDashboard, IconHome} from "@arco-design/web-react/icon";
import BasicInfo from "@/pages/stat/display/basic";
const { Row, Col } = Grid;

export default function StatDisplay({statId = 0}) {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const { id } = useParams();

    useEffect(() => {
        console.log("stat display:" + statId);

    },[])

    return(
        <>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
            <Breadcrumb.Item>
                <IconHome />
            </Breadcrumb.Item>
            <Breadcrumb.Item style={{fontWeight:20}}>{t['statDisplay.breadcrumbItem']}</Breadcrumb.Item>
        </Breadcrumb>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            <Card>
                <Typography.Title
                    heading={6}
                >
                    {'每分钟uv数据统计'}
                </Typography.Title>
                <SearchForm statInfo={null} />
                {/*<ChartPanel statInfo={null}/>*/}
            </Card>
            <Card>
                <Typography.Title
                    heading={6}
                >
                    {'Metric Information'}
                </Typography.Title>
                {/*<BasicInfo statInfo={null}/>*/}
            </Card>
        </Space>
        </>
    );
}