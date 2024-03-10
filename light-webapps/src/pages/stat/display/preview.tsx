import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import SearchForm from "./search_form";
import {Breadcrumb, Button, Card, Divider, Grid, Notification, Space, Spin, Typography} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import {useSelector} from "react-redux";
import {Department, Stat, TreeNode} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import ChartPanel from "@/pages/stat/display/chart_panel";
import {IconDashboard, IconHome, IconTag, IconTags} from "@arco-design/web-react/icon";
import BasicInfo from "@/pages/stat/display/basic";
import {requestQueryById} from "@/api/stat";
const { Row, Col } = Grid;
import { RiGlobalLine } from "react-icons/ri";
import { AiOutlineBarChart } from "react-icons/ai";
import {getStatStateDescription, getStatStateDescriptionWithBadge} from "@/pages/common/desc/base";
import StatDetailModal from "@/pages/stat/list/detail";
import StatFilterConfigModal from "@/pages/stat/filter/filter_set";
import {LimitedRecordModal} from "@/pages/record/limited_records";
import {RecordTypeEnum, ResourceTypeEnum} from "@/types/insights-common";
import StatUpdateModal from "@/pages/stat/update";
import ReverseBindedPanel from "@/pages/metricset/binded/reverse-binded";


export default function StatPreviewPanel({size = 'default',id}) {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const [statInfo,setStatInfo] = useState<Stat>(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [searchForm,setSearchForm] = useState(null);
    const [showFilterConfigModal,setShowFilterConfigModal] = useState<boolean>(false);
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const [showLimitedRecord,setShowLimitedRecord] = useState<boolean>(false);
    const [showUpdateModal,setShowUpdateModal] = useState<boolean>(false);

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

    function handleSearch(params) {
        console.log("--Params is:" + JSON.stringify(params));
        setSearchForm({...params,t:Date.now()});
    }

    useEffect(() => {
        fetchData().then();
    },[id,reloadTime])

    return(
        <>
            <Spin loading={loading} size={20} style={{ display: 'block' }}>
                <Space size={16} direction="vertical" style={{ width: '100%' }}>
                    {statInfo &&
                        <Card>
                            <Typography.Title
                                heading={6}
                                style={{marginBottom:'25px'}}
                            >
                                <IconTag style={{marginRight:'10px'}}/>
                                {statInfo?.title}
                                <span style={{color:"red",fontSize:'15px',marginLeft:'10px'}}>{'['}{getStatStateDescription(t,statInfo?.state)}{']'}</span>
                            </Typography.Title>
                            {<SearchForm size={size} statInfo={statInfo} onSearch={handleSearch}/>}
                            {<ChartPanel size={size} statInfo={statInfo} searchForm={searchForm}/>}
                        </Card>
                    }
                    <Card>
                        <Typography.Title
                            heading={6}
                        >
                            {t['statDisplay.label.statistic.information']}{'：'}
                        </Typography.Title>
                        <BasicInfo statInfo={statInfo} callback={tableCallback}/>
                    </Card>
                </Space>
                {showFilterConfigModal && <StatFilterConfigModal statInfo={statInfo}
                                                                 onClose={() => setShowFilterConfigModal(false)}
                                                                 onSuccess={() => setReloadTime(Date.now)}
                />}
                {showLimitedRecord && <LimitedRecordModal resourceId={statInfo?.id} recordTypes={[RecordTypeEnum.STAT_RESULT_LIMITED]} resourceType={ResourceTypeEnum.Stat} onClose={() => setShowLimitedRecord(false)}/>}
                {showUpdateModal && <StatUpdateModal statInfo={statInfo} onClose={() => setShowUpdateModal(false)} listCallback={(r1,r2) => setStatInfo(r1)}/>}
            </Spin>
        </>
    );
}