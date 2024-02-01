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
import {requestFixedById, requestList, requestUnFixedById} from "@/api/metricset";
import {IconHome} from "@arco-design/web-react/icon";
import {requestDeleteById} from "@/api/metricset";
import {useDispatch,useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {updateStoreFixedMetricInfo} from "@/index";
const { Title } = Typography;
const { Row, Col } = Grid;

export default function MetricSetCardBox ({item}){

    const t = useLocale(locale);
    const dispatch = useDispatch();
    const fixedMetricInfo = useSelector((state: {fixedMetricInfo:Array<MetricSet>}) => state.fixedMetricInfo);
    const [loading, setLoading] = useState(true);

    const tableCallback = async (type,record) => {
        if(type == 'fixed'){
            await handlerFixed(record).then();
        }else if(type == 'unfixed'){
            await handlerUnFixed(record).then();
        }
    };

    const handlerFixed = async (record) => {
        setLoading(true);
        const id = record.id;
        await requestFixedById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['metricSetList.operations.fix.submit.success']});
                sessionStorage.removeItem('cache_fixed_metrics');
                const currentFixedData = fixedMetricInfo.filter(x => x.id != record.id);
                dispatch(updateStoreFixedMetricInfo([...currentFixedData,record]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerUnFixed = async (record) => {
        setLoading(true);
        const id = record.id;
        await requestUnFixedById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['metricSetList.operations.unfix.submit.success']});
                sessionStorage.removeItem('cache_fixed_metrics');
                const currentFixedData = fixedMetricInfo.filter(x => x.id != record.id);
                dispatch(updateStoreFixedMetricInfo([...currentFixedData]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    return (
        <CardBlock item={item} callback={tableCallback} />
    );
}