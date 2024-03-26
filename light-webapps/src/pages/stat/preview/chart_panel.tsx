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
import {requestStatData} from "@/api/data";
import {getEmptyOption, getLoadingOption} from "@/pages/stat/preview/common";
import useStorage from "@/utils/useStorage";

export default function ChartPanel({size = 'default',searchForm = null,statInfo,parentLoading = false,ref=null}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const [batchTimeList,setBatchTimeList] = useState<string[]>([]);
    const [eChartData,setEChartData] = useState<Array<EChartChartValue>>([]);
    const [errorMessage,setErrorMessage] = useState(null);
    const [loadingOption, setLoadingOption] = useState({});
    const chartRef = ref == null ? useRef(null) : ref;
    const [emptyOption,setEmptyOption] = useState({});
    const [theme, setTheme] = useStorage('arco-theme', 'light');

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
            const seriesObject = {
                name:displayDimensValue,
                type:'line',
                data:z.valuesList?.map(z => z.value),
                animation: true,
                animationEasing: 'quadraticInOut',
                animationDurationUpdate:30,
            }
            eChartChartValues.push(seriesObject);
        })
        setEChartData(eChartChartValues);
        setErrorMessage(null);
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
            const dimensParams = {};
            for (const [key, value] of Object.entries(searchForm)) {
                if(key == 'date' || key == 't'){
                    continue;
                }
                dimensParams[key] = value;
            }
            combineParam.dimensParams = dimensParams;
        }else{
            const timeParam = statInfo?.timeparam;
            if(timeParam.endsWith('minute') || timeParam.endsWith('second') || timeParam.endsWith('hour')){
                combineParam.startTime = getDailyStartTimestamp();
                combineParam.endTime = getDailyEndTimestamp();
            }else if(timeParam.endsWith('day')){
                combineParam.startTime = getDayBefore(getDailyStartTimestamp(),13);
                combineParam.endTime = getDailyEndTimestamp();
            }else if(timeParam.endsWith('month')){
                combineParam.startTime = getDayBefore(getDailyStartTimestamp(),365);
                combineParam.endTime = getDailyEndTimestamp();
            }
        }
        await requestStatData(combineParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setErrorMessage(null);
                loadData(data);
            }else{
                setErrorMessage(message);
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const getOption = () => {
        return {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'line',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside',
                    start: 0,
                    end: 100
                }
            ],
            legend: {
                data: eChartData.map(z => z.name?z.name:""),
                icon:'circle',
                itemHeight:'10',
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: errorMessage ? [] : batchTimeList,
                    axisLabel: {
                        animation: false
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLabel: {
                        animation: false
                    },
                }
            ],

            series: errorMessage ? [] : eChartData,
            graphic: errorMessage && [{
                type: 'text',
                left: 'center',
                top: 'middle',
                style: {
                    fill: '#000',
                    text: errorMessage,
                    fontSize: 12,
                }
            }]
        };
    }


    useEffect(() => {
        if(statInfo?.templateEntity?.dimensArray?.length == 0){
            fetchData().then();
        }else if(searchForm == null){
            setErrorMessage(t['statDisplay.filterConfig.warning']);
        }else{
            const dateValue = searchForm.date;
            if(!searchForm.date){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: t['statDisplay.filterConfig.warning.dateParam']});
                return;
            }
            for (let i = 0; i < statInfo?.templateEntity?.dimensArray?.length; i++) {
                const dimens = statInfo?.templateEntity?.dimensArray[i];
                const dimensParam = searchForm[dimens];
                if(!dimensParam || dimensParam.length == 0){
                    setErrorMessage(t['statDisplay.filterConfig.warning']);
                    return;
                }
            }
            fetchData().then();
        }
    },[JSON.stringify(searchForm),statInfo?.id])

    useEffect(() => {
        setLoadingOption(getLoadingOption(theme));
        setEmptyOption(getEmptyOption(t,theme));
    },[])

    const getReactChart = () => {
        if(size == 'default'){
            return <ReactECharts ref={chartRef} option={getOption()} style={{ height: '300px' ,width:'100%',marginLeft:'0px'}} showLoading={parentLoading?false:loading}
                                 loadingOption={loadingOption}/>
        }else if(size == 'small'){
            return <ReactECharts ref={chartRef} option={getOption()} style={{ height: '230px' ,width:'100%',marginLeft:'0px'}} showLoading={parentLoading?false:loading}
                                 loadingOption={loadingOption}/>
        }else if(size == 'mini'){
            return <ReactECharts ref={chartRef} option={getOption()} style={{ height: '150px' ,width:'100%',marginLeft:'0px'}} showLoading={parentLoading?false:loading}
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