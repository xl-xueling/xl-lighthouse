import React, { useState, useEffect } from 'react';
import LineChartWrapper from "@/pages/render/wrapper/LineChartWrapper";
import {requestQueryByIds} from "@/api/stat";
import {Stat} from "@/types/insights-web";

export default function TempIndex(){

    const [statInfo,setStatInfo] = useState<Stat>(null);

    const id = 1100577;

    const fetchStatInfo = async () => {
        await requestQueryByIds({ids:[id]}).then((response) => {
            const data = response.data;
            setStatInfo(data[0]);
        });
    }

    useEffect(() => {
        fetchStatInfo().then();
    },[])

    return (
        <>
            {statInfo && <LineChartWrapper statInfo={statInfo} functionIndex={-1} startTime={'2024-06-27 00:00:00'} endTime={'2024-06-27 23:59:59'}/>}
            </>
    );
}