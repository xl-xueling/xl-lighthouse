import React, {useContext, useEffect, useRef, useState} from 'react';
import SearchForm from "./search_form";
import {Button, Card, Grid, Notification, Space, Spin, Typography} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import {LimitData, Stat, StatData, TreeNode} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {IconTag} from "@arco-design/web-react/icon";
import BasicInfo from "@/pages/stat/preview/basic";
import {requestQueryById} from "@/api/stat";
import {getStatStateDescription} from "@/pages/common/desc/base";
import StatFilterConfigModal from "@/pages/stat/filter/filter_set";
import StatUpdateModal from "@/pages/stat/update";
import {handlerFetchLimitData, handlerFetchStatData,} from "@/pages/stat/preview/common";
import * as echarts from "echarts";
import TimeLineBarPanel from "@/pages/stat/preview/timeline_bar_chart";
import StatBasicLineChart from "@/pages/stat/preview/line_chart_v1";
import './style/index.module.less';
import {PermissionEnum, StatStateEnum} from "@/types/insights-common";
import {IoMdRefresh} from "react-icons/io";
import StatLimitingModal from "@/pages/stat/limiting/StatLimitingModal";
import ErrorPage from "@/pages/common/error";
import {deepCopyObject} from "@/utils/util";
import StatPreviewSettingsModal from "@/pages/stat/preview/settings/StatPreviewSettingsModal";
import {GlobalContext} from "@/context";
import {CiSettings} from "react-icons/ci";
import {StatInfoPreviewContext} from "@/pages/common/context";

const { Row, Col } = Grid;

export default function StatPreviewPanel({specifyTitle = null,size = 'default',id}) {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const [statInfo,setStatInfo] = useState<Stat>(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [statChartLoading,setStatChartLoading] = useState<boolean>(false);
    const [searchForm,setSearchForm] = useState(null);
    const [showFilterConfigModal,setShowFilterConfigModal] = useState<boolean>(false);
    const [refreshTime,setRefreshTime] = useState<number>(Date.now());
    const [showLimitedRecord,setShowLimitedRecord] = useState<boolean>(false);
    const [statChartData,setStatChartData] = useState<Array<StatData>>(null);
    const [statChartErrorMessage,setStatChartErrorMessage] = useState<string>(null);
    const [limitChartData,setLimitChartData] = useState<Array<LimitData>>(null);
    const [showSettingsModal,setShowSettingsModal] = useState<boolean>(false);
    const [limitChartErrorMessage,setLimitChartErrorMessage] = useState<string>(null);
    const [limitChartLoading,setLimitChartLoading] = useState<boolean>(false);
    const [pageTitle,setPageTitle] = useState<string>(null)
    const [errorCode,setErrorCode] = useState<string>(null);
    const [option,setOption] = useState({});
    const refs = useRef<any[]>([]);
    const refFetchId = useRef<any>(null);
    const formRef = useRef(null);

    const tableCallback = async (type,data) => {
        if(type == 'showFilterConfigModal'){
            setShowFilterConfigModal(true);
        }else if(type == 'showLimitedRecord'){
            setShowLimitedRecord(true);
        }else if(type == 'showSettingsModal'){
            setShowSettingsModal(true);
        }
    }


    const fetchStatInfo = async () => {
        setLoading(true);
        await requestQueryById({id:id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                if (refFetchId.current == id) {
                    if(!data.permissions.includes(PermissionEnum.AccessAble)){
                        setErrorCode("403");
                    }
                    setStatInfo(data)
                }
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const getPageTitle = () => {
        return statInfo?.state == StatStateEnum.RUNNING ?
            specifyTitle?specifyTitle:statInfo?.title
            :
            <span style={{color:"red",fontSize:'15px',marginLeft:'10px'}}>{specifyTitle?specifyTitle:statInfo?.title}</span>
    }


    const fetchStatData = async () => {
        if(!statInfo){
            return;
        }
        if(!formRef.current){
            return;
        }
        const cloneStatInfo = deepCopyObject(statInfo);
        const formParams = formRef.current.getData();
        let validateDimensParam = {};
        if(formParams != null){
            validateDimensParam = Object.keys(formParams).reduce((acc, key) => {
                if (key != 't' && key != 'date' && formParams[key] !== null && formParams[key] !== undefined && formParams[key].length > 0) {
                    acc[key] = formParams[key];
                }
                return acc;
            }, {});
        }
        const numDimensParam = Object.keys(validateDimensParam).length;
        if(cloneStatInfo.templateEntity.dimensArray.length > 0 && numDimensParam == 0){
            if (refFetchId.current == cloneStatInfo.id) {
                setStatChartData(null);
                setStatChartErrorMessage(t['statDisplay.filterConfig.warning']);
            }
        }else{
            setStatChartLoading(true);
            const statChartData = await handlerFetchStatData(cloneStatInfo,formParams);
            if (refFetchId.current == cloneStatInfo.id) {
                if(statChartData.code == '0'){
                    setStatChartData(statChartData.data);
                    setStatChartErrorMessage(null);
                }else{
                    setStatChartData(null);
                    setStatChartErrorMessage(statChartData.message);
                }
            }
            setStatChartLoading(false);
        }
    }

    const fetchLimitData = async () => {
        if(!statInfo){
            return;
        }
        const cloneStatInfo = deepCopyObject(statInfo);
        setLimitChartLoading(true);
        const limitChartData = await handlerFetchLimitData(cloneStatInfo.id);
        if (refFetchId.current == cloneStatInfo.id) {
            if(limitChartData.code == '0'){
                setLimitChartData(limitChartData.data);
                setLimitChartErrorMessage(null);
            }else{
                setLimitChartData(null);
                setLimitChartErrorMessage(limitChartData.message);
            }
        }
        setLimitChartLoading(false);
    }

    const getStatChart = () => {
        if(!statInfo){
            return ;
        }
         return (
                <Col span={24}>
                    <Card>
                        <StatBasicLineChart size={'small'} data={statChartData} errorMessage={statChartErrorMessage} loading={loading?false:statChartLoading} group={'sameGroup'}/>
                    </Card>
                </Col>
        )
    }

    const getStateChartTitle = (functionIndex) => {
        const chartsConfigs = statInfo?.renderConfig?.charts;
        if(chartsConfigs){
            const chartsConfig = chartsConfigs?.filter(item => item.functionIndex === functionIndex);
            return chartsConfig[0].title;
        }else{
            return statInfo.templateEntity.statStateList[functionIndex].stateBody;
        }
    }

    const getStateCharts = () => {
        if(!statInfo){
            return ;
        }
        const stateList = statInfo.templateEntity.statStateList;
        return stateList.map((z,index) => {
            return (
                <Col span={24/statInfo.templateEntity.statStateList.length} key={'state-chart-' + z.functionIndex}>
                    <Card title={
                        <Grid.Row gutter={8}>
                            <Grid.Col span={20}>
                                {getStateChartTitle(z.functionIndex)}
                            </Grid.Col>
                            {statInfo?.permissions.includes(PermissionEnum.ManageAble)?
                                <Grid.Col span={4} style={{ textAlign:"right" }}>
                                    <CiSettings onClick={() => tableCallback('showSettingsModal',z.functionIndex)} style={{cursor:'pointer'}}/>
                                </Grid.Col>:null
                            }
                        </Grid.Row>
                    }>
                        <StatBasicLineChart size={'mini'} data={statChartData} stateIndex={z.functionIndex} errorMessage={statChartErrorMessage} loading={loading?false:statChartLoading} group={'sameGroup'}/>
                    </Card>
                </Col>
            )
        })
    }
    const refresh = () => {
        setRefreshTime(Date.now());
    }
    const getLimitChart = () => {
        return (
            <Col span={24}>
                <Card title={t['statDisplay.limit.title']}>
                    <TimeLineBarPanel compId={statInfo?.id} size={'small'} data={limitChartData} errorMessage={limitChartErrorMessage} loading={loading?false:limitChartLoading} />
                </Card>
            </Col>
        );
    }

    function handleSearch(params) {
        setSearchForm({...params,t:Date.now()});
    }

    useEffect(() => {
        const pageTitle = getPageTitle();
        setPageTitle(pageTitle);
        fetchStatData().then();
        if(statInfo?.templateEntity.limitFlag){
            fetchLimitData().then();
        }
    },[statInfo])

    useEffect(() => {
        fetchStatData().then();
    },[JSON.stringify(searchForm)])

    useEffect(() => {
         fetchStatInfo().then();
    },[refreshTime])

    useEffect(() => {
        setErrorCode(null);
        refFetchId.current = id;
        setTimeout(() => {
            echarts.connect('sameGroup');
            setSearchForm(null);
            fetchStatInfo().then();
        },3)
    },[id])


    return(
        errorCode ? <ErrorPage errorCode={403}/> :
        <>
            <StatInfoPreviewContext.Provider value={{statInfo,setStatInfo}}>
                <Spin loading={loading} size={20} style={{ display: 'block' }}>
                <Space size={16} direction="vertical" style={{ width: '100%',minHeight:'500px' }}>
                    {statInfo &&
                        <>
                    <Card>
                        <Typography.Title
                            heading={6}
                            style={{marginBottom:'25px'}}
                        >
                            <IconTag style={{marginRight:'10px'}}/>
                            {
                                pageTitle
                            }
                            <span style={{color:"red",fontSize:'15px',marginLeft:'10px'}}>{'['}{getStatStateDescription(t,statInfo?.state)}{']'}</span>
                            <Button style={{marginLeft:'15px'}} icon={<IoMdRefresh/>} size={"mini"} shape={"round"} onClick={() => {refresh()}} />
                        </Typography.Title>
                        {<SearchForm size={size} onSearch={handleSearch} ref={formRef}/>}
                        {getStatChart()}
                    </Card>
                    {statInfo.templateEntity.statStateList.length > 1 &&
                        <Row gutter={16}>
                            {getStateCharts()}
                        </Row>}
                    {statInfo.templateEntity.limitFlag &&
                        <Row gutter={16}>
                            {getLimitChart()}
                        </Row>
                    }
                    <Card>
                        <Typography.Title
                            heading={6}
                            style={{marginBottom:'15px',marginTop:'0px'}}
                        >
                            {t['statDisplay.label.statistic.information']}{'：'}
                        </Typography.Title>
                        <BasicInfo callback={tableCallback}/>
                    </Card>
                        </>
                    }
                </Space>
                {showFilterConfigModal && <StatFilterConfigModal
                                                                 onClose={() => setShowFilterConfigModal(false)}
                                                                 onSuccess={() => refresh()}
                />}
                {showLimitedRecord && <StatLimitingModal onClose={() => setShowLimitedRecord(false)}/>}
                {showSettingsModal && <StatPreviewSettingsModal onClose={() => setShowSettingsModal(false)}/>}
            </Spin>
            </StatInfoPreviewContext.Provider>
        </>
    );
}