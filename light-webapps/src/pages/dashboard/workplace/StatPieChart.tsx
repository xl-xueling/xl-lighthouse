import React, {useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {Stat, StatData, EChartChartValue, StatValue} from "@/types/insights-web";
import {Notification, Space} from "@arco-design/web-react";
import {requestData, requestTestData} from "@/api/data";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import ReactECharts from 'echarts-for-react';
import {
    convertDateToTimestamp,
    getDailyEndTimestamp,
    getDailyStartTimestamp,
    DateFormat, getDayBefore, getDayStartTimestamp, getDayEndTimestamp
} from "@/utils/date";
import {formatString, getRandomString} from "@/utils/util";
// import 'default-passive-events'

export default function StatPieChart() {

    const option = {
        tooltip: {
            trigger: 'item'
        },

        series: [
            {
                name: 'Access From',
                type: 'pie',
                radius: '50%',
                data: [
                    { value: 1048, name: 'Search Engine' },
                    { value: 735, name: 'Direct' },
                    { value: 580, name: 'Email' },
                    { value: 484, name: 'Union Ads' },
                    { value: 300, name: 'Video Ads' }
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    const chartStyle = {
        height: '240px' ,width:'100%',marginLeft:'0px'
    };

    return (
        <ReactECharts option={option} style={chartStyle } />
    );
}