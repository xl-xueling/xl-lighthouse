import React, {useEffect, useRef, useState} from 'react';
import {Space} from "@arco-design/web-react";
import ReactECharts from 'echarts-for-react';
import {loadingOption} from "@/pages/stat/preview/common";
import {getRandomString, stringifyObj} from "@/utils/util";
import * as echarts from "echarts";

export default function LineChartV2({data = null,errorMessage = null,size="default", loading = false,group=null}) {

    const [timeIndex,setTimeIndex] = useState<number>(0);
    const [seriesArray,setSeriesArray] = useState([]);
    const [batchList,setBatchList] = useState([]);
    const [dimensList,setDimensList] = useState([]);
    const [option,setOption] = useState({});
    const chartRef = useRef(null);

    const defaultOption = {
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
            data: dimensList,
            icon:'circle',
            itemHeight:'10',
        },
        grid: {
            top:'25px',
            left: '3%',
            right: '4%',
            bottom: '3%',
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
                    animation: true
                },
            }
        ],
        series: seriesArray,
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

    const getSeries = (chartData) => {
        const seriesArray = new Array<any>();
        for(let i=0;i<chartData.length;i++){
            const dimensValue = chartData[i].dimensValue;
            const values = chartData[i].valuesList.map(z => z.value);
            const seriesObj =  {
                name: dimensValue,
                type: 'line',
                data: values,
                animation: true,
                animationEasing: 'quadraticInOut',
                animationDurationUpdate:3000,
            };
            seriesArray.push(seriesObj);
        }
        return seriesArray;
    }

    useEffect(() => {
        if(data){
            const chart = chartRef.current.getEchartsInstance();
            chart.clear();
            const dimensList = data.map(z => z.displayDimensValue);
            setDimensList(dimensList.length == 1 && dimensList[0] == null ? [] : dimensList);
            const batchList = data[0].valuesList.map(z => z.displayBatchTime);
            setBatchList(batchList);
            const seriesArray = getSeries(data);
            setSeriesArray(seriesArray);
            chart.setOption(defaultOption);
        }
    },[data])

    const getReactChart = () => {
        if(size == 'default'){
            return <ReactECharts ref={chartRef} option={defaultOption} style={{ height: '300px' ,width:'100%',marginLeft:'0px'}}  showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'small'){
            return <ReactECharts ref={chartRef} option={defaultOption} style={{ height: '230px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'mini'){
            return <ReactECharts ref={chartRef} option={defaultOption} style={{ height: '150px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }
    }

    return (<>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            {getReactChart()}
        </Space>
    </>);
}