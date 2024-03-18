import React, {useEffect, useRef, useState} from 'react';
import SearchForm from "./search_form";
import {Card, Grid, Notification, Space, Spin, Typography} from "@arco-design/web-react";
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
import {getRandomString} from "@/utils/util";
import {
    getStatBasicLineOption,
    handlerFetchLimitData,
    handlerFetchStatData,
    translateResponseDataToLineChartData,
} from "@/pages/stat/preview/common";
import * as echarts from "echarts";
import TimeLineBarPanel from "@/pages/stat/preview/timeline_bar_chart";
import StatBasicLineChartV2 from "@/pages/stat/preview/line_chart_v2";
import StatBasicLineChart from "@/pages/stat/preview/line_chart_v1";
const { Row, Col } = Grid;
import './style/index.module.less';

export default function StatPreviewPanel({specifyTitle = null,size = 'default',id}) {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const [statInfo,setStatInfo] = useState<Stat>(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [statChartLoading,setStatChartLoading] = useState<boolean>(false);
    const [searchForm,setSearchForm] = useState(null);
    const [showFilterConfigModal,setShowFilterConfigModal] = useState<boolean>(false);
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const [showLimitedRecord,setShowLimitedRecord] = useState<boolean>(false);
    const [showUpdateModal,setShowUpdateModal] = useState<boolean>(false);
    const [statChartData,setStatChartData] = useState<Array<StatData>>(null);
    const [statChartErrorMessage,setStatChartErrorMessage] = useState<string>(null);
    const [limitChartData,setLimitChartData] = useState<Array<LimitData>>(null);
    const [limitChartLoading,setLimitChartLoading] = useState<boolean>(false);
    const [option,setOption] = useState({});
    const refs = useRef<any[]>([]);
    const refFetchId = useRef<any>(null);

    const tableCallback = async (type,data) => {
        if(type == 'showFilterConfigModal'){
            setShowFilterConfigModal(true);
        }else if(type == 'showLimitedRecord'){
            setShowLimitedRecord(true);
        }else if(type == 'showUpdateModal'){
            setShowUpdateModal(true);
        }
    }

    const fetchStatInfo = async () => {
        setLoading(true);
        await requestQueryById({id:id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                if (refFetchId.current === id) {
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

    const fetchStatData = async () => {
        setStatChartLoading(true);
        if(statInfo && refFetchId.current === id){
            const statChartData = await handlerFetchStatData(statInfo,searchForm);
            if(statChartData.code == '0'){
                setStatChartLoading(false);
                setStatChartData(statChartData.data);
                setStatChartErrorMessage(null);
            }else{
                setStatChartData(null);
                setStatChartErrorMessage(statChartData.message);
            }
        }
        setStatChartLoading(false);
    }

    const fetchLimitData = async () => {
        setLimitChartLoading(true);
        if(statInfo && refFetchId.current === id){
            const limitChartData = await handlerFetchLimitData();
            setLimitChartData(limitChartData.data);
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
                        <StatBasicLineChart size={'small'} data={statChartData} errorMessage={statChartErrorMessage} loading={statChartLoading} group={'sameGroup'}/>
                    </Card>
                </Col>
        )
    }

    const getStateCharts = () => {
        if(!statInfo){
            return ;
        }
        const stateList = statInfo.templateEntity.statStateList;
        return stateList.map((z,index) => {
            return (
                <Col span={24/statInfo.templateEntity.statStateList.length} key={getRandomString()}>
                    <Card title={z.stateBody}>
                        <StatBasicLineChart size={'mini'} data={statChartData} stateIndex={z.functionIndex} errorMessage={statChartErrorMessage} loading={statChartLoading} group={'sameGroup'}/>
                    </Card>
                </Col>
            )
        })
    }
    const [currentIndex, setCurrentIndex] = useState(0);

    const getLimitChart = () => {
        return (
            <Col span={24} key={getRandomString(32)}>
                <Card title={'sss'}>
                    <TimeLineBarPanel loading={limitChartLoading} size={'default'} data={limitChartData} />
                </Card>
            </Col>
        );
    }

    useEffect(() => {
        fetchStatData().then();
        fetchLimitData().then();
    },[statInfo,JSON.stringify(searchForm)])

    function handleSearch(params) {
        setSearchForm({...params,t:Date.now()});
    }

    useEffect(() => {
        refFetchId.current = id;
        setTimeout(() => {
            echarts.connect('sameGroup');
            setSearchForm(null);
            fetchStatInfo().then();
        },10)
    },[id,reloadTime])


    return(
        <>
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
                            {specifyTitle?specifyTitle:statInfo?.title}
                            <span style={{color:"red",fontSize:'15px',marginLeft:'10px'}}>{'['}{getStatStateDescription(t,statInfo?.state)}{']'}</span>
                        </Typography.Title>
                        {<SearchForm size={size} statInfo={statInfo} onSearch={handleSearch}/>}
                        {getStatChart()}
                    </Card>
                    {statInfo.templateEntity.statStateList.length > 1 &&
                        <Row gutter={16}>
                            {getStateCharts()}
                        </Row>}
                    <Row gutter={16}>
                        {/*{getLimitChart()}*/}
                    </Row>
                    <Card>
                        <Typography.Title
                            heading={6}
                        >
                            {t['statDisplay.label.statistic.information']}{'：'}
                        </Typography.Title>
                        <BasicInfo statInfo={statInfo} callback={tableCallback}/>
                    </Card>
                        </>
                    }
                </Space>
                {showFilterConfigModal && <StatFilterConfigModal statInfo={statInfo}
                                                                 onClose={() => setShowFilterConfigModal(false)}
                                                                 onSuccess={() => setReloadTime(Date.now)}
                />}
                {/*{showLimitedRecord && <LimitedRecordModal resourceId={statInfo?.id} recordTypes={[RecordTypeEnum.STAT_RESULT_LIMITING]} resourceType={ResourceTypeEnum.Stat} onClose={() => setShowLimitedRecord(false)}/>}*/}
                {showUpdateModal && <StatUpdateModal statInfo={statInfo} onClose={() => setShowUpdateModal(false)} listCallback={(r1,r2) => setStatInfo(r1)}/>}
            </Spin>
        </>
    );
}