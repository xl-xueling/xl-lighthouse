import React, {useContext, useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {Stat, StatData, EChartChartValue, StatValue} from "@/types/insights-web";
import {Link, Notification, Space, Typography} from "@arco-design/web-react";
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
import {HomePageContext} from "@/pages/dashboard/workplace/index";
import styles from "@/pages/dashboard/workplace/style/overview.module.less";
// import 'default-passive-events'

export default function StatPieChart() {

    const { homeData, statInfo } = useContext(HomePageContext);
    const [chartData,setChartData] = useState(null);
    const t = useLocale(locale);

    const option = {
        tooltip: {
            trigger: 'item'
        },

        series: [
            {
                name: t['workplace.department.statistic.size'],
                type: 'pie',
                radius: '50%',
                data:chartData,
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

    useEffect(() => {
        console.log(JSON.stringify(homeData.departmentStatCount));
        if(homeData?.departmentStatCount){
            const chartData = homeData?.departmentStatCount.map(z => {
                return { value: z.value, name: z.dimensValue };
            })
            setChartData(chartData);
        }
    },[JSON.stringify(homeData)])

    const chartStyle = {
        height: '240px' ,width:'100%',marginLeft:'0px'
    };

    return (
        <>
            <ReactECharts option={option} style={chartStyle } />
        </>
    );
}