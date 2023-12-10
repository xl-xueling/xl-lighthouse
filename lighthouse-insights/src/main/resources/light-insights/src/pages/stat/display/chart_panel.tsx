import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {RenderConfig, RenderTypeEnum, ResultData} from "@/types/insights-common";
import {requestQueryDimensValue} from "@/api/group";
import {Descriptions, Divider, Form, Grid, Select, TreeSelect, Typography} from "@arco-design/web-react";
import {Col} from "antd";
import {translate} from "@/pages/department/common";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "@/pages/stat/display/style/index.module.less";
import { Line } from '@ant-design/plots';
import { Chart, Line as Line2, Point, Tooltip,getTheme } from "bizcharts";
import { LineAdvance} from 'bizcharts';
import {IconEdit, IconList, IconPublic, IconPushpin} from "@arco-design/web-react/icon";


export default function ChartPanel({searchForm={}}) {

    const data = [
        { year: '1991', value: 3 },
        { year: '1992', value: 4 },
        { year: '1993', value: 3.5 },
        { year: '1994', value: 5 },
        { year: '1995', value: 4.9 },
        { year: '1996', value: 6 },
        { year: '1997', value: 7 },
        { year: '1998', value: 9 },
        { year: '1999', value: 13 },
    ];

    const config = {
        data,
        xField: 'year',
        yField: 'value',
        point: {
            size: 5,
        },

        theme:'light',
    };

    // const config = {
    //     data,
    //     padding: 'auto',
    //     xField: 'Date',
    //     yField: 'value',
    //     xAxis: {
    //         // type: 'timeCat',
    //         tickCount: 5,
    //     },
    // };

    // 数据源
    const data2 = [
        {
            year: "1991",
            value: 3,
        },
        {
            year: "1992",
            value: 4,
        },
        {
            year: "1993",
            value: 3.5,
        },
        {
            year: "1994",
            value: 5,
        },
        {
            year: "1995",
            value: 4.9,
        },
        {
            year: "1996",
            value: 6,
        },
        {
            year: "1997",
            value: 7,
        },
        {
            year: "1998",
            value: 9,
        },
        {
            year: "1999",
            value: 13,
        },
    ];


    const descriptionData = [
        {
            label: 'Title',
            value: '每分钟_uv数据统计',
        },
        {
            label: 'CreatedTime',
            value: '2023-12-01 10:00:19',
        },
        {
            label: 'Group',
            value: '[首页广告模块数据统计]homepage_behavior_stat',
        },
        {
            label: 'Admins',
            value: 'zhangsan;lisi;wangwu',
        },

        {
            label: 'TimeParam',
            value: '1-minute',
        },
        {
            label: 'Operation',
            value: <div><IconList style={{marginRight:'10px'}} /> <IconEdit style={{marginRight:'10px'}} />
            <IconPushpin style={{marginRight:'10px'}} /></div>,
        },
        {
            label: 'Template',
            value: '<stat-item title="每分钟uv数据统计" stat="bitcount(userId)" dimens="province" />',
            span:2,
        },

    ];
    useEffect(() => {
        console.log("chart panel..")
    },[])

    return (<div>
        <Line {...config} />
        <Divider style={{ marginTop:'30px',marginBottom:'25px'}}/>
        <Typography.Title
            heading={6}
            style={{ marginTop: 0, marginBottom: 15,fontSize:14 }}
        >
            {'Metric Information'}
        </Typography.Title>
        <Descriptions
            // title='Basic Information'
            labelStyle={{ width:'10%' }}
            valueStyle={{ width:'40%' }}
            size={"small"} border data={descriptionData} column={2}/>
    </div>);
}