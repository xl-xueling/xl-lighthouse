import {
    Typography,
    Grid,
} from '@arco-design/web-react';
import React, {useEffect, useMemo, useState} from 'react';

import ProjectApplyModal from "@/pages/project/apply";
import {ResourceTypeEnum} from "@/types/insights-common";
import StatApplyModal from "@/pages/stat/apply";
const { Row, Col } = Grid;
const { Text } = Typography;


export default function ApplyModal({itemInfo,resourceType,onClose}) {

    const getRenderModal = () => {
        if(resourceType == ResourceTypeEnum.Project){
            return <ProjectApplyModal projectInfo={itemInfo} onClose={onClose}/>
        }else if(resourceType == ResourceTypeEnum.Stat){
            return <StatApplyModal statInfo={itemInfo} onClose={onClose}/>
        }
        return null;
    }

    return (
        <>
            {getRenderModal()};
        </>
    );
}