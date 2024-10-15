import React, {useContext, useEffect, useState} from "react";
import {CallerManageContext} from "@/pages/common/context";
import {Card, Grid, Notification,Typography} from "@arco-design/web-react";
import {requestQueryByIds} from "@/api/stat";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";
import {handlerFetchStatData} from "@/pages/stat/preview/common";
import {useUpdateEffect} from "ahooks";
import {stringifyMap} from "@/utils/util";
import StatBasicLineChart from "@/pages/stat/preview/line_chart_v1";
import {GlobalContext} from "@/context";
import Exception100 from "@/pages/exception/100";
import SearchForm from "@/pages/caller/preview/search_form";

const { Row, Col } = Grid;

export default function CallerPreviewPanel({}){

    const t = useLocale(locale);

    const {callerInfo} = useContext(CallerManageContext);

    const [statsInfo,setStatsInfo] = useState<Map<string,any>>(new Map());

    const [loading,setLoading] = useState<boolean>(false);

    const [statsData,setStatsData] = useState(new Map());

    const statIds = [1031,1032,1033,1034,1035,1036];

    const { setLang, lang, theme, setTheme } = useContext(GlobalContext);

    const [formParams,setFormParams] = useState<any>({callerId:[String(callerInfo?.id)],function:['dataQuery']});

    const [errorInfo,setErrorInfo] = useState<string>(null);

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
        const statsDataMap = new Map();
        for (const statId of statIds) {
            const statInfo = statsInfo.get(String(statId));
            let queryParams = formParams;
            if(statId == 1035){
                queryParams = {...formParams,"from":['0' + ',' + t['callerPreview.from.0'] , '1'  + ',' + t['callerPreview.from.1']]}
            }else if(statId == 1036){
                queryParams = {...formParams,"status":['0'+ ',' + t['api.result.0'] , '1'+ ',' + t['api.result.1']]}
            }
            const statChartData = await handlerFetchStatData(statInfo,queryParams);
            if(statChartData.code == '0'){
                statsDataMap.set(String(statId),statChartData.data);
            }else{
                setErrorInfo(statChartData.message);
            }
        }
        setStatsData(statsDataMap);
        setLoading(false);
    }

    const getTitle = (statId,indicatorIndex) => {
        let title;
        if(statId == '1031'){
            title = t['callerPreview.stat.title.1031'];
        }else if(statId == '1032'){
            title = t['callerPreview.stat.title.1032'];
        }else if(statId == '1033'){
            title = t['callerPreview.stat.title.1033'];
        }else if(statId == '1034'){
            title =  t['callerPreview.stat.title.1034'];
        }else if(statId == '1035'){
            title = t['callerPreview.stat.title.1035'];
        }else if(statId == '1036'){
            title = t['callerPreview.stat.title.1036'];
        }
        return title;
    }

    const getIndicatorCharts = (statId,indicatorIndex) => {
        return <Col span={12} style={{marginTop:'15px'}}>
            <Card title={
                <Grid.Row gutter={8}>
                    <Grid.Col span={20}>
                        <Typography.Title style={{ fontSize: 14 }}>
                            {getTitle(statId,indicatorIndex)}
                        </Typography.Title>
                    </Grid.Col>
                </Grid.Row>
            }>
                <StatBasicLineChart theme={theme} size={'mini'} data={statsData.get(String(statId))} stateIndex={indicatorIndex - 1} errorMessage={null} loading={loading} group={'sameGroup'}/>
            </Card>
        </Col>
    }

    useUpdateEffect(() => {
        if(statsInfo && statsInfo.size > 0){
            fetchStatsData().then();
        }
    },[statsInfo,JSON.stringify(formParams)])

    useEffect(() => {
        if(callerInfo == null){
            setErrorInfo(t['system.error.403']);
        }else{
            fetchStatsInfo().then();
        }
    },[])

    const onSearch = (v) => {
        setFormParams({...v,"callerId":[String(callerInfo?.id)]});
    }

    return (
        <>
            <Card style={{paddingTop:'20px'}}>
                {statsInfo.get(String(1031)) && <SearchForm size={'small'} onSearch={onSearch} statInfo={statsInfo.get(String(1031))} initValues={{function:['dataQuery,dataQuery']}}/>}
            </Card>
            {
                errorInfo ? <Exception100 errorMessage={errorInfo}/>:
                    <Row gutter={16}>
                        {getIndicatorCharts(1031,0)}
                        {getIndicatorCharts(1032,0)}
                        {getIndicatorCharts(1033,0)}
                        {getIndicatorCharts(1034,0)}
                        {getIndicatorCharts(1035,0)}
                        {getIndicatorCharts(1036,0)}
                    </Row>
            }
        </>
    )
}