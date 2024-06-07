import React, {useEffect, useState} from 'react';
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {
    IconBook,
    IconClockCircle, IconLock, IconStar, IconStarFill,
    IconUserGroup
} from "@arco-design/web-react/icon";
import {
    Button,
    Descriptions,
    Grid,
    Message,
    Modal, Notification,
    Popconfirm,
    Skeleton,
    Space,
    Typography
} from "@arco-design/web-react";
import { RiAppsLine } from "react-icons/ri";
import {Project} from "@/types/insights-web";
import UserGroup from "@/pages/user/common/groups";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import styles from "./styles/index.module.less";
import {LuLayers} from "react-icons/lu";
import {CiLock, CiViewTable} from "react-icons/ci";
import {getIcon} from "@/desc/base";
import {requestStarById, requestUnStarById} from "@/api/project";
import {useDispatch,useSelector} from "react-redux";
import {updateStoreStaredProjectInfo} from "@/store";
const { Row, Col } = Grid;

export default function ProjectStar({projectInfo}) {

    const t = useLocale(locale);
    const dispatch = useDispatch();
    const [loading,setLoading] = useState<boolean>(false);
    const staredProjectInfo = useSelector((state: {staredProjectInfo:Array<Project>}) => state.staredProjectInfo);

    const handlerStar = async (record) => {
        setLoading(true);
        const id = record.id;
        await requestStarById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectStar.operations.star.submit.success']});
                localStorage.removeItem('cache_stared_projects');
                const currentFixedData = staredProjectInfo?.filter(x => x.id != record.id);
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
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectStar.operations.unstar.submit.success']});
                localStorage.removeItem('cache_stared_projects');
                const currentFixedData = staredProjectInfo?.filter(x => x.id != record.id);
                dispatch(updateStoreStaredProjectInfo([...currentFixedData]))
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const IconList = [
        IconStarFill,
    ].map((Tag, index) => <Tag key={index} />);


    return (
        staredProjectInfo?.map(z => z.id).includes(projectInfo?.id)?
            <Popconfirm
                focusLock
                position={"br"}
                title='Confirm'
                content={t['projectStar.operations.unstar.confirm']}
                onOk={async (e) => {await handlerUnStar(projectInfo)}}>
                <Button size={"mini"} shape={"round"} icon={<IconStarFill/>} style={{cursor:"pointer",color:'#626aea'}}/>
            </Popconfirm>
            :
            <Popconfirm
                focusLock
                position={"br"}
                title='Confirm'
                content={t['projectStar.operations.star.confirm']}
                onOk={async (e) => {await handlerStar(projectInfo)}}
            >
                <Button size={"mini"} shape={"round"} icon={<IconStar/>} style={{cursor:"pointer",color:'#626aea'}}/>
            </Popconfirm>
    );
}