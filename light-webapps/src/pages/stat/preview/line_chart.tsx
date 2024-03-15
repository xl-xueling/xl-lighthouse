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

export default function BasicLinePanel({option = null,size="default", loading = false,cref = null}) {

    // const chartRef = cref == null ? useRef(null) : cref;

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

    const getReactChart = () => {
        if(size == 'default'){
            return <ReactECharts ref={cref} option={option} style={{ height: '300px' ,width:'100%',marginLeft:'0px'}} showLoading={loading}
                                 loadingOption={loadingOption}/>
        }else if(size == 'small'){
            return <ReactECharts ref={cref} option={option} style={{ height: '230px' ,width:'100%',marginLeft:'0px'}} showLoading={loading}
                                 loadingOption={loadingOption}/>
        }else if(size == 'mini'){
            return <ReactECharts ref={cref} option={option} style={{ height: '150px' ,width:'100%',marginLeft:'0px'}} showLoading={loading}
                                 loadingOption={loadingOption}/>
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