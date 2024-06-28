import React, { useState, useEffect } from 'react';
import ReactECharts from 'echarts-for-react';
import {getOptions} from "@/pages/render/render/echarts/line/Constants";
import {RenderValue, StatData} from "@/types/insights-web";

interface RenderProps {
    from?:string;
    renderData?:Array<RenderValue>;
    loading?:boolean;
}

export default function LineChartRender (props:RenderProps){

    const {from,renderData,errorMessage} = props;

    const [option,setOption] = useState(null);

    useEffect(() => {
        const option = getOptions(renderData);
        setOption(option);
    },[renderData])

    return (
        <>
            {option && <ReactECharts option={option} style={{ height: '300px' ,width:'100%',marginLeft:'0px'}} />}
        </>
    );
}