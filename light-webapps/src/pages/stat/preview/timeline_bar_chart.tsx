import React, {useEffect, useState} from 'react';
import {Space} from "@arco-design/web-react";
import ReactECharts from 'echarts-for-react';
import {loadingOption} from "@/pages/stat/preview/common";
import {stringifyObj} from "@/utils/util";

export default function TimeLineBarPanel({data = null,size="default", loading = false,group=null}) {

    const [timeIndex,setTimeIndex] = useState<number>(1);
    const [seriesArray,setSeriesArray] = useState([]);
    const [batchList,setBatchList] = useState([]);
    const [dimensList,setDimensList] = useState([]);

    const handleTimelineClick = (e) => {
        console.log("e is:" + e);
    };

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
            timeline: {
                axisType: 'category',
                autoPlay: false,
                currentIndex:timeIndex,
                playInterval: 1000,
                data: batchList,
                label: {
                    formatter: '{value}'
                }
            },
            xAxis: {
                type: 'category',
                data: dimensList,
            },
            yAxis: {
                type: 'value',
            },
            series: [{
                type: 'bar',
            }]
        },
        options: seriesArray,
    };

    const onclick={
        // click:(params)=>{
        //     console.log("params 1 is:" + stringifyObj(params));
        //     // setTimeIndex(params.dataIndex);
        // },
    }

    useEffect(() => {
        if(data){
            const seriesArray = getSeries(data);
            setSeriesArray(seriesArray);
            const batchList = data.map(z => z.displayBatchTime);
            setBatchList(batchList);
        }
    },[data])

    const getReactChart = () => {
        if(size == 'default'){
            return <ReactECharts option={defaultOption} style={{ height: '300px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'small'){
            return <ReactECharts option={defaultOption} style={{ height: '230px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
        }else if(size == 'mini'){
            return <ReactECharts option={defaultOption} style={{ height: '150px' ,width:'100%',marginLeft:'0px'}} showLoading={loading} loadingOption={loadingOption}/>
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