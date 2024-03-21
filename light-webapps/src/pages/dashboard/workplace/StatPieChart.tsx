import React, {useContext, useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {Stat, StatData, EChartChartValue, StatValue} from "@/types/insights-web";
import {Link, Notification, Space, Typography} from "@arco-design/web-react";
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
        series:  [
            {
                name: t['workplace.department.statistic.size'],
                type: 'pie',
                radius: '65%',
                data:chartData?.length ? chartData : [{name:'No Data',value:0}],
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
        <div style={{height:'240px'}}>
            {chartData && <ReactECharts option={option} style={chartStyle } />}
        </div>
    );
}