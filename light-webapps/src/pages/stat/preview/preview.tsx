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
    getLineOption, handlerFetchLimitData,
    handlerFetchStatData,
    translateResponseDataToLineChartData,
} from "@/pages/stat/preview/common";
import BasicLinePanel from "@/pages/stat/preview/line_chart";
import * as echarts from "echarts";
import TimeLineBarPanel from "@/pages/stat/preview/timeline_bar_chart";
const { Row, Col } = Grid;

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
    const [statChartErrorMessage,setStatChartErrorMessage] = useState(null);
    const [limitChartData,setLimitChartData] = useState<Array<LimitData>>(null);
    const [limitChartLoading,setLimitChartLoading] = useState<boolean>(false);
    const [option,setOption] = useState({});
    const refs = useRef<any[]>([]);

    const tableCallback = async (type,data) => {
        if(type == 'showFilterConfigModal'){
            setShowFilterConfigModal(true);
        }else if(type == 'showLimitedRecord'){
            setShowLimitedRecord(true);
        }else if(type == 'showUpdateModal'){
            setShowUpdateModal(true);
        }
    }

    const fetchData = async () => {
        setLoading(true);
        await requestQueryById({id:id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setStatInfo(data)
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
        if(statInfo){
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
        if(statInfo){
            const limitChartData = await handlerFetchLimitData();
            setLimitChartData(limitChartData.data);
        }
        setLimitChartLoading(false);
    }

    const getStatChart = () => {
        const lineData = translateResponseDataToLineChartData(statChartData,-1);
        const option = getLineOption(lineData, null);
        return (
                <Col span={24}>
                    <Card>
                        <BasicLinePanel loading={statChartLoading} size={size} option={option} group={'sameGroup'}/>
                    </Card>
                </Col>
        )
    }

    const getStateCharts = () => {
        const stateList = statInfo.templateEntity.statStateList;
        return stateList.map((z,index) => {
            const lineData = translateResponseDataToLineChartData(statChartData,z.functionIndex);
            const option = getLineOption(lineData, null);
            return (
                <Col span={12} key={getRandomString(32)}>
                    <Card title={z.stateBody}>
                        <BasicLinePanel loading={statChartLoading} size={'mini'} option={option} group={'sameGroup'}/>
                    </Card>
                </Col>
            );

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
        echarts.connect('sameGroup');
        setSearchForm(null);
        fetchData().then();
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
                        {/*{getStatChart()}*/}
                    </Card>
                    <Row gutter={16}>
                        {/*{getStateCharts()}*/}
                    </Row>
                    <Row gutter={16}>
                        {getLimitChart()}
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