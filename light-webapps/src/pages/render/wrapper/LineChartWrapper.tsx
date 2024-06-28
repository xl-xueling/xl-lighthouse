import React, { useState, useEffect } from 'react';
import {handlerFetchStatData, translateStatDataToSequenceData} from "@/pages/stat/preview/common";
import {RenderSequenceData, Stat, StatData} from "@/types/insights-web";
import EChartLineChartRender from "@/pages/render/render/echarts/line/LineChartRender";

interface Props {
    from?:string;
    statInfo:Stat;
    functionIndex:number;
    filters?:[];
    startTime?:String;
    endTime?:String;
}

export default function LineChartWrapper (props:Props){

    const {from,statInfo,functionIndex,filters,startTime,endTime} = props;

    const [renderData,setRenderData] = useState<Array<RenderSequenceData>>();

    const [errorMessage,setErrorMessage] = useState<string>();

    const functionData = async () => {
        const formParams = {
            filters:filters,
            startTime:startTime,
            endTime:endTime,
        }
        const response = await handlerFetchStatData(statInfo,formParams);
        if(response.code == "0"){
            const renderData = translateStatDataToSequenceData(response.data,functionIndex);
            setRenderData(renderData);
        }else{
            setRenderData(null);
            setErrorMessage(response.message);
        }
    }

    useEffect(() => {
        functionData().then();
    },[])

    return (
        <>
            {renderData && <EChartLineChartRender from={from} renderData={renderData} errorMessage={errorMessage} />}
        </>
    );
}