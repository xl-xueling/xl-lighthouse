import React, {useEffect, useRef, useState} from 'react';
import {Space} from "@arco-design/web-react";
import ReactECharts from 'echarts-for-react';
import {getEmptyOption, getLoadingOption, loadingOption} from "@/pages/stat/preview/common";
import {stringifyObj} from "@/utils/util";
import * as echarts from "echarts";
import useStorage from "@/utils/useStorage";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/stat/preview/locale";

export default function TimeLineBarPanel({data = null,size="default", loading = false,group=null}) {

    const [timeIndex,setTimeIndex] = useState<number>(-1);
    const [seriesArray,setSeriesArray] = useState([]);
    const [batchList,setBatchList] = useState([]);
    const [currentBatch,setCurrentBatch] = useState(null);
    const [dimensList,setDimensList] = useState([]);
    const chartRef = useRef(null);
    const [loadingOption, setLoadingOption] = useState({});
    const [emptyOption,setEmptyOption] = useState({});
    const [theme, setTheme] = useStorage('arco-theme', 'light');
    const t = useLocale(locale);

    const getSeries = (chartData) => {
        const seriesArray = new Array<any>();
        for(let i=0;i<chartData.length;i++){
            const dimensList = chartData[i].values;
            const seriesData = [];
            for(let k=0;k < dimensList.length;k++){
                seriesData[k] = dimensList[k].score;
            }
            const seriesObj = {
                series: [
                    {
                        name: 'Series',
                        type: 'bar',
                        data: seriesData,
                        animation: true,
                        animationDuration: 1000,
                    }
                ]
            };
            seriesArray.push(seriesObj);
        }
        return seriesArray;
    }

    const defaultOption = {
        baseOption: {
            grid: {
              top:'25px',
              left:'50px',
              right:'50px',
              bottom:'90px',
            },
            tooltip: {
                show:data && !loading,
                trigger: 'axis',
                formatter: function (params) {
                    if(!params || !Array.isArray(params)){
                        return;
                    }
                    const newParams = [];
                    const paramData = params.sort(function (a, b) {
                        return b.value - a.value;
                    });
                    for (let i = 0, len = paramData.length; i < len; i++) {
                        const v = paramData[i];
                        const s = v.marker + ' value : ' + v.value;
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
            timeline: {
                axisType: 'category',
                autoPlay: false,
                playInterval: 1000,
                bottom:'0px',
                left: '100px',
                right:'100px',
                currentIndex:timeIndex,
                data: batchList,
                label: {
                    formatter: '{value}'
                },
                controlStyle:{
                    showPlayBtn:false,
                    showPrevBtn:true,
                    showNextBtn:true,
                    itemGap:20,
                    prevBtnSize:10,
                    nextBtnSize:10,
                },
                checkpointStyle: {
                    symbol: 'circle',
                    symbolSize: 15,
                    normal: {
                        color: 'lightgray',
                        show: true,
                    },
                },
                symbolSize: 8,
                symbol:'emptyCircle',
                lineStyle: {
                    width: 2,
                },
            },
            xAxis: {
                type: 'category',
                data: dimensList,
            },
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
            series: [{
                type: 'bar',
            }]
        },
        options: seriesArray,
    };


    useEffect(() => {
        if(data){
            const values = data[timeIndex]?.values;
            setDimensList(values.map(z => z.dimensValue));
            if(values.length == 0){
                const chart = chartRef.current.getEchartsInstance();
                chart.clear();
                chart.setOption(emptyOption);
            }else{
                const chart = chartRef.current.getEchartsInstance();
                chart.clear();
            }
        }
    },[timeIndex])

    useEffect(() => {
        if(data){
            const seriesArray = getSeries(data);
            setSeriesArray(seriesArray);
            const batchList = data.map(z => z.displayBatchTime);
            setBatchList(batchList);
            const index = timeIndex == -1 || currentBatch == null || batchList.indexOf(currentBatch) == -1 ? seriesArray.length - 1 : batchList.indexOf(currentBatch);
            setTimeIndex(index);
            setCurrentBatch(currentBatch);
        }
    },[data])

    useEffect(() => {
        setLoadingOption(getLoadingOption(theme));
        setEmptyOption(getEmptyOption(t,theme));
    },[])

    useEffect(() => {
        const chart = chartRef.current.getEchartsInstance();
        chart.on('timelinechanged', (e:any) => {
            const index = e.currentIndex;
            setTimeIndex(index);
            setCurrentBatch(batchList[index]);
        });
    },[batchList])

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
            <div onWheel={(e) => {e.stopPropagation();}}>
                {getReactChart()}
            </div>
        </Space>
    </>);
}