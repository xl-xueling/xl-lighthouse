import React, { useEffect, useState } from 'react';
import {
    Tabs,
    Card,
    Input,
    Typography,
    Grid,
    PaginationProps,
    Pagination,
    Breadcrumb, Notification, Spin
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import AddCard from './card-add';
import MetricSetAddPanel from "@/pages/metricset/create";
import {MetricSet, Project, TreeNode} from "@/types/insights-web";
import {requestStarById, requestList, requestUnStarById} from "@/api/metricset";
import {IconHome} from "@arco-design/web-react/icon";
import {requestDeleteById} from "@/api/metricset";
import {useDispatch,useSelector} from "react-redux";
import {GlobalState, updateStoreStaredMetricInfo} from "@/store";
const { Title } = Typography;
const { Row, Col } = Grid;

export default function MetricSetCardBox ({from=null,item,size = 'default'}){

    const t = useLocale(locale);
    const dispatch = useDispatch();
    const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);
    const [loading, setLoading] = useState(true);

    const tableCallback = async (type,record) => {
        if(type == 'star'){
            await handlerStar(record).then();
        }else if(type == 'unstar'){
            await handlerUnStar(record).then();
        }
    };

    const handlerStar = async (record) => {
        setLoading(true);
        const id = record.id;
        await requestStarById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['metricSetList.operations.star.submit.success']});
                localStorage.removeItem('cache_stared_metrics');
                const currentFixedData = staredMetricInfo.filter(x => x.id != record.id);
                dispatch(updateStoreStaredMetricInfo([record,...currentFixedData]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerUnStar = async (record) => {
        setLoading(true);
        const id = record.id;
        await requestUnStarById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['metricSetList.operations.unstar.submit.success']});
                localStorage.removeItem('cache_stared_metrics');
                const currentFixedData = staredMetricInfo.filter(x => x.id != record.id);
                dispatch(updateStoreStaredMetricInfo([...currentFixedData]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    return (
        <CardBlock from={from} item={item} callback={tableCallback} size={size} />
    );
}