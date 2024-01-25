import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card, Table, TableColumnProps, Space, Divider, PaginationProps, Notification
} from '@arco-design/web-react';
import {
    IconDownCircle, IconPlus, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useMemo, useState} from 'react';

import ProjectApplyModal from "@/pages/project/apply";
import {ResourceTypeEnum} from "@/types/insights-common";
const { Row, Col } = Grid;
const { Text } = Typography;


export default function ApplyModal({itemInfo,resourceType,onClose}) {

    const getRenderModal = () => {
        if(resourceType == ResourceTypeEnum.Project){
            return <ProjectApplyModal projectInfo={itemInfo} onClose={onClose}/>
        }else if(resourceType == ResourceTypeEnum.Metric){
            return null;
        }else if(resourceType == ResourceTypeEnum.Stat){

        }
        return null;
    }

    return (
        <>
            {getRenderModal()};
        </>
    );
}