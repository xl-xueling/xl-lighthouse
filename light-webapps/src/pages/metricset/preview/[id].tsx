import React, {createContext, useEffect, useState} from 'react';
import {
    Typography,
    Grid,
    Tabs,
} from '@arco-design/web-react';
const {Title} = Typography;
const {Row, Col} = Grid;
const TabPane = Tabs.TabPane;
import { useRouter } from 'next/router';
import MetricPreviewPanel from "@/pages/metricset/preview/MetricPreviewPanel";

export default function Index() {
    const router = useRouter();
    const { id } = router.query;
    return (
        <MetricPreviewPanel id={id}/>
    );
}