import React, {useContext, useEffect, useState} from 'react';
import {
    Typography,
    Grid,
    Tabs,
    Menu, Empty
} from '@arco-design/web-react';
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import {getTreeResourceIcon} from "@/desc/base";
import {MetricSetPreviewContext} from "@/pages/common/context";

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
                    }
                    }}

                                  title={
                        <span style={{display:"inline-flex",alignItems:"center"}}>{getTreeResourceIcon(item.type,level)}{item.label}</span>
                    }>
                        {renderMenuItems(item.children,level + 1)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item onClick={(e)=>
            {e.stopPropagation();
                if(item.type == 'stat'){
                    callback("clickStatMenu",Number(item.value),item.label);
                }
            }} key={item.key}>{getTreeResourceIcon(item.type,level)}{item.label}</Menu.Item>;
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