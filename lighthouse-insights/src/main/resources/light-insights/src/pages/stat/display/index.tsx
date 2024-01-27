import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import SearchForm from "./search_form";
import {Breadcrumb, Button, Card, Divider, Grid, Notification, Space, Spin, Typography} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import {useSelector} from "react-redux";
import {Department, Stat} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import ChartPanel from "@/pages/stat/display/chart_panel";
import {IconDashboard, IconHome, IconTag, IconTags} from "@arco-design/web-react/icon";
import BasicInfo from "@/pages/stat/display/basic";
import {requestQueryById} from "@/api/stat";

const {Row, Col} = Grid;
import {RiGlobalLine} from "react-icons/ri";
import {AiOutlineBarChart} from "react-icons/ai";
import {getStatStateDescription, getStatStateDescriptionWithBadge} from "@/pages/common/desc/base";
import StatDetailModal from "@/pages/stat/list/detail";
import StatFilterConfigModal from "@/pages/stat/filter/filter_set";
import StatPreviewPanel from "@/pages/stat/display/preview";

export default function StatPreviewPage() {

    const {id} = useParams();
    const t = useLocale(locale);

    return (
        <>
            <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                <Breadcrumb.Item>
                    <IconHome/>
                </Breadcrumb.Item>
                <Breadcrumb.Item style={{fontWeight: 20}}>{t['statDisplay.breadcrumbItem']}</Breadcrumb.Item>
            </Breadcrumb>
            <StatPreviewPanel id={id}/>
        </>
    );
}