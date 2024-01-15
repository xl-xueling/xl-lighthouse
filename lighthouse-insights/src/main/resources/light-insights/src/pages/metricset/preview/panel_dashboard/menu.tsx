import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {
    Card,
    Typography,
    Grid,
    Space,
    Tabs,
    Divider,
    Notification,
    Breadcrumb,
    Spin,
    Menu
} from '@arco-design/web-react';
import PreviewHeader from "@/pages/metricset/preview/header";
import {IconDashboard, IconHome, IconTag, IconThunderbolt} from "@arco-design/web-react/icon";
import BindedList from "@/pages/metricset/binded/list/binded";
import GroupBasicPanel from "@/pages/group/basic";
import ProjectPreview from "@/pages/project/preview";
import useLocale from "@/utils/useLocale";
import locale from "../locale";
import {requestQueryById} from "@/api/metricset";
import {MetricSet} from "@/types/insights-web";
import styles from "@/pages/project/preview/style/index.module.less";
import ProjectMenu from "@/pages/project/preview/menu";
import StatPreviewPanel from "@/pages/stat/display/preview";
import {CiViewTable} from "react-icons/ci";
import get = Reflect.get;
import {PiDiamondsFour} from "react-icons/pi";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

export default function DashboardMenu({metricSetInfo,callback}) {

    const getIcon = (item) => {
        if(item.type == 'stat'){
            return <IconTag/>
        }else if(item.type == 'group'){
            return <CiViewTable style={{marginRight:'10px'}}/>
        }else if(item.type == 'project'){
            return <PiDiamondsFour style={{marginRight:'10px'}}/>
        }
    }

    const renderMenuItems = (items) =>
        items?.map((item) => {
            if (Array.isArray(item.children) && item.children.length > 0) {
                return (
                    <Menu.SubMenu key={item.key} title={
                        <span style={{display:"inline-flex",alignItems:"center"}}>{getIcon(item)}{item.label}</span>
                    }>
                        {renderMenuItems(item.children)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item key={item.key}>{getIcon(item)}{item.label}</Menu.Item>;
        });

    return (
        <>
            <Menu
                className={'disable-select'}
                style={{height: 'calc(100% - 28px)' ,minHeight:'500px',overflow: "auto"}}
                onClickMenuItem = {(key, event, keyPath) => {
                    const type = key.split("_")[0];
                    const id = key.split("_")[1];
                    if(type == 'stat'){
                        callback("clickStatMenu",Number(id));
                    }
                }}>
                {
                    renderMenuItems(metricSetInfo?.structure[0]?.children)
                }
            </Menu>
        </>
    );
}