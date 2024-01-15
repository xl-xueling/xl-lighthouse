import React, {useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {Stat, StatData, EChartChartValue} from "@/types/insights-web";
import {Notification, Space} from "@arco-design/web-react";
import {requestData, requestTestData} from "@/api/data";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import ReactECharts from 'echarts-for-react';
import echarts from "echarts";
import {
    convertDateToTimestamp,
    getDailyEndTimestamp,
    getDailyStartTimestamp,
 DateFormat, getDayBefore, getDayStartTimestamp, getDayEndTimestamp
} from "@/utils/date";

export default function ChartPanel({searchForm={},statInfo}:{searchForm:any,statInfo:Stat}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const [batchTimeList,setBatchTimeList] = useState<string[]>([]);
    const [eChartData,setEChartData] = useState<Array<EChartChartValue>>([]);

    const loadData = (data:Array<StatData>) => {
        const eChartChartValues:Array<EChartChartValue> = [];
        if(data && data.length > 0){
            setBatchTimeList(data[0].valuesList?.map(z => z.displayBatchTime))
        }
        data?.forEach(z => {
            const dimens = z.dimens;
            const dimensValue = z.dimensValue;
            const displayDimensValue = z.displayDimensValue;
            const statId = z.statId;
            const seriesObject:EChartChartValue = {
                name:dimensValue,
                type:'line',
                data:z.valuesList?.map(z => z.value),
            }
            eChartChartValues.push(seriesObject);
            setEChartData(eChartChartValues);
        })
    }


    const fetchData = async () => {
        setLoading(true);
        const combineParam:any = {}
        combineParam.statId = statInfo?.id;
        if(searchForm != null){
            const date = searchForm.date;
            if(date && Array.isArray(date)){
                combineParam.startTime = getDayStartTimestamp(convertDateToTimestamp(date[0],DateFormat));
                combineParam.endTime = getDayEndTimestamp(convertDateToTimestamp(date[1],DateFormat));
            }
        }else{
            const timeParam = statInfo?.timeparam;
            if(timeParam.endsWith('minute') || timeParam.endsWith('second') || timeParam.endsWith('hour')){
                combineParam.startTime = getDailyStartTimestamp();
                combineParam.endTime = getDailyEndTimestamp();
            }else if(timeParam.endsWith('day')){
                combineParam.startTime = getDayBefore(getDailyStartTimestamp(),14);
                combineParam.endTime = getDailyEndTimestamp();
            }else if(timeParam.endsWith('month')){
                combineParam.startTime = getDayBefore(getDailyStartTimestamp(),365);
                combineParam.endTime = getDailyEndTimestamp();
            }
        }
        await requestTestData(combineParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                loadData(data);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const option = {
        xAxis: {
            type: 'category',
            data: batchTimeList,
        },
        yAxis: {
            type: 'value',
        },
        dataZoom: [
            {
                type: 'inside',
                start: 0,
                end: 100
            }
        ],
        grid: {
            left: '50px;',
            right: '5px;',
            bottom: '25px;',
            top: '10px;'
        },
        series: eChartData,
    };

    useEffect(() => {
        fetchData().then();
    },[JSON.stringify(searchForm)])

    return (<>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            <ReactECharts option={option} style={{ height: '240px' ,width:'100%',marginLeft:'0px'}} />
        </Space>
    </>);
}