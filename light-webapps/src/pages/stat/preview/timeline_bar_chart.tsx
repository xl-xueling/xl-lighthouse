import React, {useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {Stat, StatData, EChartChartValue, StatValue} from "@/types/insights-web";
import {Notification, Space} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import ReactECharts from 'echarts-for-react';
import {
    convertDateToTimestamp,
    getDailyEndTimestamp,
    getDailyStartTimestamp,
    DateFormat, getDayBefore, getDayStartTimestamp, getDayEndTimestamp
} from "@/utils/date";
import {formatString, getRandomString, stringifyObj} from "@/utils/util";
import {getTimeLineBarOption} from "@/pages/stat/preview/common";

export default function TimeLineBarPanel({data=null,size="default", loading = false,group=null}) {

    const [timeIndex,setTimeIndex] = useState<number>(1);
    const [option,setOption] = useState({});

    const loadingOption = {
        animation: false,
        icon: 'none',
        text: 'Loading...',
        color: '#c23531',
        showSpinner: true,
        spinnerRadius: 7,
        textColor: '#000',
        fontWeight: 'normal',
        lineWidth: 2,
        fontSize: 13,
        maskColor: 'rgba(255, 255, 255, 1)',
    };

    const handleTimelineClick = (e) => {
        console.log("e is:" + e);
    };


    useEffect(() => {
        console.log("timeIndex is:" + timeIndex);
        if(data){
            const option = getTimeLineBarOption(data,null,timeIndex);
            setOption(option);
        }
    },[timeIndex])

    const onclick={
        click:(params)=>{
            setTimeIndex(params.dataIndex);
        }
    }

    const getReactChart = () => {
        if(size == 'default'){
            return <ReactECharts option={option} onEvents={onclick} />
        }else if(size == 'small'){
            return <ReactECharts option={option} onEvents={onclick} />
        }else if(size == 'mini'){
            return <ReactECharts option={option} onEvents={onclick} />
        }
    }

    return (<>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            <div onWheel={(e) => {e.stopPropagation();}}>
                {getReactChart()}
            </div>
        </Space>
    </>);
}