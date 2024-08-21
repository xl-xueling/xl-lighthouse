import React, {useContext, useState} from 'react';
import {Grid, Space, Spin, Tabs, Typography} from '@arco-design/web-react';
import styles from "@/pages/project/preview/style/index.module.less";
import StatPreviewPanel from "@/pages/stat/preview/preview";
import MetricSetDataViewMenu from "@/pages/metricset/preview/dataview/MetricSetDataViewMenu";
import {MetricSetPreviewContext} from "@/pages/common/context";
import {ResourceTypeEnum} from "@/types/insights-common";

const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

interface Item {
    resourceId:number;
    resourceType:ResourceTypeEnum;
    specifyTitle:string;
}

export default function MetricSetDataViewPanel({parentLoading}) {

    const [selectedItem,setSelectedItem] = useState<Item>(null);

    const {PRO_ViewPreview} = useContext(MetricSetPreviewContext);

    const handlerCallback = async (type,p1,p2) => {
        if(type == 'clickStatMenu'){
            const item = {
                resourceId:Number(p1),
                resourceType:ResourceTypeEnum.Stat,
                specifyTitle:p2
            }
            setSelectedItem(item);
        }else if(type == 'clickViewMenu'){
            const item = {
                resourceId:Number(p1),
                resourceType:ResourceTypeEnum.View,
                specifyTitle:p2
            }
            setSelectedItem(item);
        }
    }

    const render = () => {
        if(selectedItem.resourceType == ResourceTypeEnum.Stat){
            return <StatPreviewPanel size={'small'} id={selectedItem.resourceId} specifyTitle={selectedItem.specifyTitle}/>
        }else{
            return PRO_ViewPreview(selectedItem.resourceId,selectedItem.specifyTitle);
        }
    }

    return (
        <Spin loading={parentLoading} style={{display:'block'}}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <div className={styles.wrapper}>
                    <Space size={16} direction="vertical" className={styles.left}>
                        <Row>
                            <MetricSetDataViewMenu callback={handlerCallback}/>
                        </Row>
                    </Space>
                    <Space className={styles.right} size={16} direction="vertical">
                        {selectedItem && render()}
                    </Space>
                </div>
            </Space>
        </Spin>
    );
}