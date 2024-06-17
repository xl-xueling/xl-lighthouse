import {
    Breadcrumb,
    Card,
    Checkbox,
    Empty,
    Form,
    Grid,
    Input,
    Message,
    Notification, Popconfirm, Popover,
    Space,
    Spin,
    Switch,
    Table,
    Typography,
} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from "./locale";
import {IconCheck, IconClose, IconHome} from "@arco-design/web-react/icon";
import styles from "./style/index.module.less";
import {Stat} from "@/types/insights-web";
import {requestQueryById} from "@/api/stat";
import {requestEnableDebugMode, requestDisableDebugMode, requestFetchTrackMessages} from "@/api/track";
import {useRouter} from "next/router";
import {getDataWithLocalCache} from "@/utils/localCache";
import ChartPanel from "@/pages/stat/preview/chart_panel";
import {formatString} from "@/utils/util";
import {DateTimeFormat, formatTimeStamp, getDateFormat} from "@/utils/date";
import TrackStatPanel from "@/pages/track/TrackStatPanel";
const BreadcrumbItem = Breadcrumb.Item;

export default function TrackStatPage() {

    const router = useRouter();
    const { id } = router.query;
    return (
        <TrackStatPanel id={id} />
    );
}