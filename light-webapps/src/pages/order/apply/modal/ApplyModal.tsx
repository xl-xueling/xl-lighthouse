import {
    Typography,
    Grid,
} from '@arco-design/web-react';
import React, {useContext, useEffect, useMemo, useState} from 'react';

import {ResourceTypeEnum} from "@/types/insights-common";
import {MetricSetPreviewContext} from "@/pages/common/context";
const { Row, Col } = Grid;
const { Text } = Typography;


export default function ApplyModal({itemInfo,resourceType,onClose}) {

    const { metricSetInfo, setMetricSetInfo,PRO_ViewBindTab,PRO_ProjectApplyModal = null,PRO_StatApplyModal = null} = useContext(MetricSetPreviewContext);

    const getRenderModal = () => {
        if(resourceType == ResourceTypeEnum.Project && PRO_ProjectApplyModal){
            return <PRO_ProjectApplyModal projectInfo={itemInfo} onClose={onClose}/>
        }else if(resourceType == ResourceTypeEnum.Stat && PRO_StatApplyModal){
            return <PRO_StatApplyModal statInfo={itemInfo} onClose={onClose}/>
        }
        return null;
    }

    return (
        <>
            {getRenderModal()};
        </>
    );
}