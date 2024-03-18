import React, {useEffect, useRef, useState} from 'react';
import {Space} from "@arco-design/web-react";
import ReactECharts from 'echarts-for-react';
import {loadingOption} from "@/pages/stat/preview/common";
import {getRandomString, stringifyObj} from "@/utils/util";
import * as echarts from "echarts";

export default function StatBasicLineChartV2({option={},size="default", loading = false,group=null}) {

    const chartRef = useRef(null);

    useEffect(() => {
        const chart = chartRef.current.getEchartsInstance();
        chart.clear();
        chart.setOption(option);
    },[option])

    const getReactChart = () => {
        if(size == 'default'){
            return <ReactECharts ref={chartRef} option={option} style={{ height: '300px' ,width:'100%',marginLeft:'0px'}}  showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'small'){
            return <ReactECharts ref={chartRef} option={option} style={{ height: '230px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'mini'){
            return <ReactECharts ref={chartRef} option={option} style={{ height: '150px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }
    }

    return (<>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            {getReactChart()}
        </Space>
    </>);
}