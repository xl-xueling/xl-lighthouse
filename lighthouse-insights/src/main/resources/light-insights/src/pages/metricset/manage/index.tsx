import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {Tabs, Card, Input, Typography, Grid, Space, Avatar, PaginationProps, Pagination} from '@arco-design/web-react';
import MetricManagePanel from "@/pages/metricset/manage/metricset_manage";
import styles from "./style/index.module.less";
import ProjectTree from "@/pages/project/common/project-tree";
import GroupManagePanel from "@/pages/group/manage";
import GroupAddPanel from "@/pages/group/add/group_add";
import MetricDetail from "@/pages/metricset/manage/detail";


const { Title } = Typography;
const { Row, Col } = Grid;

const defaultList = new Array(10).fill({});

export default function Index() {

    return (

        <div style={{ minHeight:500 }}>
            <div className={styles.layout}>
                <div className={styles['layout-left-side']}>
                    <Space size={24} direction="vertical" className={styles.left}>
                        <MetricDetail />
                    </Space>
                </div>
                <MetricManagePanel groupId={0}/>
            </div>
        </div>

    );
}