import React, {useContext, useEffect, useState} from 'react';
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
    Menu, Empty
} from '@arco-design/web-react';
import MetricSetPreviewHeader from "@/pages/metricset/preview/header";
import {
    IconDashboard,
    IconFolder,
    IconHome,
    IconMindMapping,
    IconTag,
    IconThunderbolt
} from "@arco-design/web-react/icon";
import BindedList from "@/pages/metricset/binded/list";
import GroupBasicPanel from "@/pages/group/basic";
import ProjectPreviewPage from "@/pages/project/preview";
import useLocale from "@/utils/useLocale";
import locale from "../locale";
import {requestQueryById} from "@/api/metricset";
import {MetricSet} from "@/types/insights-web";
import styles from "@/pages/project/preview/style/index.module.less";
import ProjectMenu from "@/pages/project/preview/menu";
import StatPreviewPanel from "@/pages/stat/preview/preview";
import {CiViewTable} from "react-icons/ci";
import get = Reflect.get;
import {PiDiamondsFour} from "react-icons/pi";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import { BiExtension } from "react-icons/bi";
import { BiWalletAlt } from "react-icons/bi";
import { RxCube } from "react-icons/rx";
import {MetricSetPreviewContext} from "@/pages/common/context";
import {getTreeResourceIcon, getTreeResourceIconWithColor} from "@/pages/common/desc/base";

export default function MetricSetDataViewMenu({callback}) {

    const { metricSetInfo, setMetricSetInfo } = useContext(MetricSetPreviewContext);

    const renderMenuItems = (items,level) =>
        items?.map((item) => {
            if (Array.isArray(item.children) && item.children.length > 0) {
                return (
                    <Menu.SubMenu key={item.key}
                                  onClick={(e)=>
                    {e.stopPropagation();
                        if(item.type == 'stat'){
                            callback("clickStatMenu",Number(item.value),item.label);
                        }else if(item.type == 'view'){
                            callback("clickViewMenu",Number(item.value),item.label);
                        }
                    }}

                                  title={
                        <span style={{display:"inline-flex",alignItems:"center"}}>{getTreeResourceIconWithColor(item.type,level,'var(--color-neutral-8)')}{item.label}</span>
                    }>
                        {renderMenuItems(item.children,level + 1)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item onClick={(e)=>
            {e.stopPropagation();
                if(item.type == 'stat'){
                    callback("clickStatMenu",Number(item.value),item.label);
                }else if(item.type == 'view'){
                    callback("clickViewMenu",Number(item.value),item.label);
                }
            }} key={item.key}>{getTreeResourceIconWithColor(item.type,level,'var(--color-neutral-8)')}{item.label}</Menu.Item>;
        });

    return (
        <>
            <Menu
                className={'disable-select'}
                style={{height: 'calc(100% - 28px)' ,minHeight:'500px',overflow: "auto"}}>
                {
                    (metricSetInfo && metricSetInfo?.structure?.children) ? renderMenuItems(metricSetInfo?.structure?.children,1)
                        : <Empty style={{marginTop:'50px'}}/>
                }
            </Menu>
        </>
    );
}