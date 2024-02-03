import React, { useEffect, useState } from 'react';
import {
    Tabs,
    Card,
    Input,
    Typography,
    Grid,
    Breadcrumb, Notification, Spin
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import CardBlock from './card-block';
import {MetricSet, Project, TreeNode} from "@/types/insights-web";
import {requestStarById, requestUnStarById} from "@/api/project";
import {useDispatch,useSelector} from "react-redux";
import {updateStoreStaredMetricInfo, updateStoreStaredProjectInfo} from "@/index";
const { Title } = Typography;
const { Row, Col } = Grid;

export default function ProjectCardBox ({item,size = 'default'}){

    const t = useLocale(locale);
    const dispatch = useDispatch();
    const staredProjectInfo = useSelector((state: {staredProjectInfo:Array<Project>}) => state.staredProjectInfo);
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
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.star.submit.success']});
                localStorage.removeItem('cache_stared_projects');
                const currentFixedData = staredProjectInfo.filter(x => x.id != record.id);
                dispatch(updateStoreStaredProjectInfo([record,...currentFixedData]))
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
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.unstar.submit.success']});
                localStorage.removeItem('cache_stared_projects');
                const currentFixedData = staredProjectInfo.filter(x => x.id != record.id);
                dispatch(updateStoreStaredProjectInfo([...currentFixedData]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    return (
        <CardBlock item={item} callback={tableCallback} size={size} />
    );
}