import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {Breadcrumb, Button, Card, Divider, Grid, Notification, Space, Spin, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {IconDashboard, IconHome, IconTag, IconTags} from "@arco-design/web-react/icon";
const {Row, Col} = Grid;
import StatPreviewPanel from "@/pages/stat/preview/preview";

export default function StatPreviewPage() {

    const {id} = useParams();
    const t = useLocale(locale);

    return (
        <>
            <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                <Breadcrumb.Item>
                    <IconHome/>
                </Breadcrumb.Item>
                <Breadcrumb.Item style={{fontWeight: 20}}>{t['statDisplay.breadcrumbItem']}</Breadcrumb.Item>
            </Breadcrumb>
            <StatPreviewPanel id={id}/>
        </>
    );
}