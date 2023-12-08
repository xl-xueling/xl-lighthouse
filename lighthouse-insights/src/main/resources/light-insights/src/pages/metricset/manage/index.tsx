import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {Tabs, Card, Input, Typography, Grid, Space, Avatar, PaginationProps, Pagination} from '@arco-design/web-react';
import MetricManagePanel from "./metricset_manage";
import styles from "./style/index.module.less";
import ProjectTree from "@/pages/project/common/project-tree";
import GroupManagePanel from "@/pages/group/manage";
import GroupAddPanel from "@/pages/group/add/group_add";
import MetricDetail from "@/pages/metricset/manage/detail";
import MetricNewDetail from "@/pages/metricset/manage/new_detail";


const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});

export default function Index() {

    return (
        <div style={{ minHeight:500 }}>
            <Card>
                <MetricNewDetail/>
                <div style={{marginBottom:'15px'}}></div>
                <MetricManagePanel groupId={0}/>
            </Card>
        </div>

    );
}