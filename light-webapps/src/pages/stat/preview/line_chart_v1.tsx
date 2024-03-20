import React, {useEffect, useRef, useState} from 'react';
import {Space} from "@arco-design/web-react";
import ReactECharts from 'echarts-for-react';
import {getEmptyOption, getErrorOption, getLineErrorOption, getLoadingOption} from "@/pages/stat/preview/common";
import {getRandomString, stringifyObj} from "@/utils/util";
import * as echarts from "echarts";
import Decimal from 'decimal.js';
import useStorage from "@/utils/useStorage";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/stat/preview/locale";


export default function StatBasicLineChart({data = null,errorMessage = null,stateIndex = -1,size="default", loading = false,group=null}) {

    const [timeIndex,setTimeIndex] = useState<number>(0);
    const [seriesArray,setSeriesArray] = useState([]);
    const [batchList,setBatchList] = useState([]);
    const [dimensList,setDimensList] = useState([]);
    const [option,setOption] = useState({});
    const [loadingOption, setLoadingOption] = useState({});
    const [emptyOption,setEmptyOption] = useState({});
    const [theme, setTheme] = useStorage('arco-theme', 'light');
    const chartRef = useRef(null);
    const t = useLocale(locale);

    const defaultOption = !data ? {} : {
        tooltip: {
            show:data && !loading,
            trigger: 'axis',
            formatter: function (params) {
                if(!params){
                    return;
                }
                const newParams = [];
                const paramData = params.sort(function (a, b) {
                    return b.value - a.value;
                });
                for (let i = 0, len = paramData.length; i < len; i++) {
                    const v = paramData[i];
                    const dimens = v.seriesName.startsWith('series')?"value":v.seriesName;
                    const s = v.marker + ' ' + dimens + ' : ' + v.value;
                    newParams.push(s)
                }
                return params[0].axisValue + "<br>" + newParams.join('<br>');
            },
            confine: true
        },
        dataZoom: [
            {
                type: 'inside',
                start: 0,
                end: 100
            }
        ],
        legend: {
            data: dimensList,
            icon:'circle',
            itemHeight:'10',
        },
        grid: {
            top: dimensList.length> 0 ? '40px':'25px',
            left: '10px',
            right: '10px',
            bottom: '0px',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: batchList,
                axisLabel: {
                    animation: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    animation: true,
                    formatter: function (value, index) {
                        if (value >= 1000 && value < 1000000) {
                            value = value / 1000 + "K";
                        } else if (value >= 1000000 && value < 1000000000) {
                            value = value / 1000000 + "M";
                        } else if (value >= 1000000000 && value < 1000000000000) {
                            value = value / 1000000000 + "B";
                        } else if (value >= 1000000000000) {
                            value = value / 1000000000000 + "T";
                        }
                        return value;
                    }
                },
            }
        ],
        series: seriesArray,
    };

    const getSeries = (chartData) => {
        const seriesArray = new Array<any>();
        for(let i=0;i<chartData.length;i++){
            const dimensValue = chartData[i].dimensValue;
            let values;
            if(stateIndex == -1){
                values = chartData[i].valuesList.map(z => z.value);
            }else{
                values = chartData[i].valuesList.map(z => z.statesValue[stateIndex]);
            }
            const seriesObj =  {
                name: dimensValue,
                type: 'line',
                data: values,
                label: {
                    show: false,
                },
                symbolSize: 3,
                animation: true,
                animationEasing: 'quadraticInOut',
                animationDurationUpdate:1000,
            };
            seriesArray.push(seriesObj);
        }
        return seriesArray;
    }

    useEffect(() => {
       if(data){
            const chart = chartRef.current.getEchartsInstance();
            chart.group = group;
            const dimensList = data.map(z => z.displayDimensValue);
            setDimensList(dimensList.length == 1 && dimensList[0] == null ? [] : dimensList);
            const batchList = data[0].valuesList.map(z => z.displayBatchTime);
            setBatchList(batchList);
            errorMessage = null;
            const seriesArray = getSeries(data);
            setSeriesArray(seriesArray);
            chart.clear();
        }else{
            const chart = chartRef.current.getEchartsInstance();
            chart.clear();
            const errorOption = getErrorOption(theme,errorMessage);
            chart.setOption(errorOption);
        }
    },[JSON.stringify(data),errorMessage])

    useEffect(() => {
        setLoadingOption(getLoadingOption(theme));
        setEmptyOption(getEmptyOption(t,theme));
    },[])


    const getReactChart = () => {
        if(size == 'default'){
            return <ReactECharts ref={chartRef} option={!data? {}:defaultOption} style={{ height: '300px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'small'){
            return <ReactECharts ref={chartRef} option={!data? {}:defaultOption} style={{ height: '230px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'mini'){
            return <ReactECharts ref={chartRef} option={!data? {}:defaultOption} style={{ height: '150px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }
    }

    return (<>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            {getReactChart()}
        </Space>
    </>);
}