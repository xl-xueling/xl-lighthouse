import React, {useEffect} from 'react';
import {useParams} from "react-router-dom";
import FiltersForm from "./filters_form";
import {Card, Divider, Grid, Typography} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import ChartPanel from "@/pages/stat/display/chart_panel";
import {IconDashboard} from "@arco-design/web-react/icon";
const { Row, Col } = Grid;

export default function StatDisplay({statId = 0}) {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const { id } = useParams();

    useEffect(() => {
        console.log("stat display")
    },[])

    return(
        <Card style={{width:'90%'}}>
            <Typography.Title
                style={{  fontSize:15}}
            >
                <IconDashboard style={{fontSize:17}}/> {'每分钟uv数据统计'}
            </Typography.Title>
            <Divider/>
            <div className={styles['search-form-wrapper']}>
                <FiltersForm />
            </div>
            <ChartPanel />
        </Card>
    );
}