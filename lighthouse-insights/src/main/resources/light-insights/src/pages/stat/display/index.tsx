import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import SearchForm from "./search_form";
import {Breadcrumb, Button, Card, Divider, Grid, Notification, Space, Typography} from "@arco-design/web-react";
import styles from "./style/index.module.less";
import {useSelector} from "react-redux";
import {Department, Stat} from "@/types/insights-web";
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

export default function StatDisplay() {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const t = useLocale(locale);
    const [statInfo,setStatInfo] = useState<Stat>(null);
    const [loading,setLoading] = useState<boolean>(true);
    const { id } = useParams();

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

    useEffect(() => {
        fetchData().then();
    },[])

    return(
        <>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
            <Breadcrumb.Item>
                <IconHome />
            </Breadcrumb.Item>
            <Breadcrumb.Item style={{fontWeight:20}}>{t['statDisplay.breadcrumbItem']}</Breadcrumb.Item>
        </Breadcrumb>
        <Space size={16} direction="vertical" style={{ width: '100%' }}>
            <Card>
                <Typography.Title
                    heading={6}
                    style={{marginBottom:'25px'}}
                >
                    <IconTag style={{marginRight:'10px'}}/>
                    {statInfo?.title}
                    <span style={{color:"red",fontSize:'15px'}}>{'['}{getStatStateDescription(t,statInfo?.state)}{']'}</span>
                </Typography.Title>
                <SearchForm statInfo={statInfo} />
                {/*<ChartPanel statInfo={null}/>*/}
            </Card>
            <Card>
                <Typography.Title
                    heading={6}
                >
                    {'Metric Information'}
                </Typography.Title>
                {/*<BasicInfo statInfo={null}/>*/}
            </Card>
        </Space>
        </>
    );
}