import {
    Breadcrumb,
    Card,
} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "./locale";
import SearchForm from "@/pages/stat/list/form";
import {IconHome} from "@arco-design/web-react/icon";
import StatList from "@/pages/stat/list/list";
import KeepAlive, {useAliveController} from "react-activation";
import {useHistory } from 'react-router-dom';
const BreadcrumbItem = Breadcrumb.Item;

export default function Index() {

    const t = useLocale(locale);
    const [formParams, setFormParams] = useState({});
    const history = useHistory();
    const { refreshScope } = useAliveController();

    function handleSearch(params) {
        setFormParams(params);
    }

    useEffect(() => {
        const action = history.action;
        if(action == 'PUSH'){
            refreshScope('StatListKeepAlive').then();
        }
    },[])

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <BreadcrumbItem>
                    <IconHome />
                </BreadcrumbItem>
                <BreadcrumbItem style={{fontWeight:20}}>{t['statList.breadcrumb.title']}</BreadcrumbItem>
            </Breadcrumb>
            <KeepAlive name="StatListKeepAlive" cacheKey={"StatListKeepAlive"} id={"StatListKeepAlive"} autoFreeze={true} when={() => {
                const targetPath = history.location?.pathname;
                return targetPath && targetPath.startsWith("/stat/preview");
            }}>
                <StatList/>
            </KeepAlive>
        </>
    );
}