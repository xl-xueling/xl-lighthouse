import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {RenderTypeEnum, ResultData} from "@/types/insights-common";
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


export default function BasicInfo({searchForm={}}) {

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

    return (
        <>
            <Descriptions
                // title='Basic Information'
                labelStyle={{ width:'10%' }}
                valueStyle={{ width:'40%' }}
                size={"small"} border data={descriptionData} column={2}/>
        </>
    );
}