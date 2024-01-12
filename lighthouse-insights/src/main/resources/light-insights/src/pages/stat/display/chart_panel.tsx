import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {AntChartValue, ArcoTreeNode, Department, Stat, StatData} from "@/types/insights-web";
import { Line } from '@ant-design/plots';
import { Chart, Line as Line2, Point, Tooltip,getTheme } from "bizcharts";
import { LineAdvance} from 'bizcharts';
import {IconEdit, IconList, IconPublic, IconPushpin} from "@arco-design/web-react/icon";
import {requestQueryById} from "@/api/stat";
import {Notification} from "@arco-design/web-react";
import {requestData, requestTestData} from "@/api/data";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {formatTimeStampBackUp} from "@/utils/util";
import {
    convertDateToTimestamp,
    formatTimeStamp, DateTimeFormat,
    getDailyEndTimestamp,
    getDailyStartTimestamp,
    getSystemTimeZone, DateFormat, getDayBefore, getDayStartTimestamp, getDayEndTimestamp
} from "@/utils/date";


export default function ChartPanel({searchForm={},statInfo}:{searchForm:any,statInfo:Stat}) {
    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const [chartData,setChartData] = useState<Array<AntChartValue>>([]);

    const translateData = (data:Array<StatData>) => {
        const valuesArray:Array<AntChartValue> = [];
        data?.forEach(z => {
            const dimens = z.dimens;
            const dimensValue = z.dimensValue;
            const displayDimensValue = z.displayDimensValue;
            const statId = z.statId;
            z.valuesList?.forEach(v => {
                const chartValue:AntChartValue = {
                    Date: v.displayBatchTime,
                    Value: v.value,
                    Dimens: displayDimensValue?displayDimensValue:' ',
                }
                valuesArray.push(chartValue);
            })
            console.log("valuesArray is:" + JSON.stringify(valuesArray));
        })
        return valuesArray;
    }

    const config = {
        data: chartData,
        xField: 'Date',
        yField: 'Value',
        // seriesField: 'Dimens',
        point: {
            size: 5,
        },
        theme:'light',
    };

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
                const chartData = translateData(data);
                setChartData(chartData);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        fetchData().then();
    },[JSON.stringify(searchForm)])

    return (<>
        <Line style={{ height:'300px'}} {...config} />
    </>);
}