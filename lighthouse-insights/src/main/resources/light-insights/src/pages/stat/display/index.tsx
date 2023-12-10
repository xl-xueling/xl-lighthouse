import React, {useEffect} from 'react';
import {useParams} from "react-router-dom";
import SearchForm from "./search_form";
import {Card, Divider, Grid, Space, Typography} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import ChartPanel from "@/pages/stat/display/chart_panel";
import {IconDashboard} from "@arco-design/web-react/icon";
import BasicInfo from "@/pages/stat/display/basic";
const { Row, Col } = Grid;

export default function StatDisplay({statId = 0}) {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const { id } = useParams();

    useEffect(() => {
        console.log("stat display")
    },[])

    return(
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
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
    );
}