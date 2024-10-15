import React, {useContext, useEffect, useState} from "react";
import {CallerManageContext} from "@/pages/common/context";
import {Card,Notification} from "@arco-design/web-react";
import {requestQueryByIds} from "@/api/stat";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/caller/manage/locale";
import {handlerFetchStatData} from "@/pages/stat/preview/common";
import {useUpdateEffect} from "ahooks";
import {stringifyMap} from "@/utils/util";

export default function CallerPreviewPanel({}){

    const t = useLocale(locale);

    const {callerInfo} = useContext(CallerManageContext);

    const [statsInfo,setStatsInfo] = useState<Map<string,any>>(new Map());

    const [loading,setLoading] = useState<boolean>(false);

    const [statsData,setStatsData] = useState(new Map());

    const statIds = [1031];

    const [formParams,setFormParams] = useState<any>({"callerName":["caller:lighthouse_test_call"],"function":["dataQuery"]});

    const fetchStatsInfo = async () => {
        setLoading(true);
        await requestQueryByIds({ids:statIds}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                const statsInfoMap = data.reduce((acc, obj) => {
                    acc.set(String(obj.id), obj);
                    return acc;
                }, new Map());
                setStatsInfo(statsInfoMap);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const fetchStatsData = async () => {
        setLoading(true);
        for (const statId of statIds) {
            const statInfo = statsInfo.get(String(statId));
            const statChartData = await handlerFetchStatData(statInfo,formParams);
            if(statChartData.code == '0'){

            }else{
                // setStatChartData(null);
                // setStatChartErrorMessage(statChartData.message);
            }
        }
        setLoading(false);
    }

    useUpdateEffect(() => {
        if(statsInfo && statsInfo.size > 0){
            fetchStatsData().then();
        }
    },[statsInfo])

    useEffect(() => {
        fetchStatsInfo().then();
    },[])

    return (
        <Card>

        </Card>
    )
}