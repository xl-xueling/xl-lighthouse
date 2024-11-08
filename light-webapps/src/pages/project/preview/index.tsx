import React, {useEffect, useState} from 'react';
import {useParams,useHistory} from "react-router-dom";
import styles from "./style/index.module.less";
import {
    Card,
    Grid,
    Space,
    Spin,
    Notification,
    Breadcrumb, Typography
} from "@arco-design/web-react";
import ProjectMenu from "@/pages/project/preview/menu";

const {Row, Col} = Grid;
import PreviewHeader from "@/pages/project/preview/head";
import {Project} from "@/types/insights-web";
import {requestQueryById} from "@/api/project";
import {IconHome} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import StatPreviewPanel from "@/pages/stat/preview/preview";
import ProjectPreviewPanel from "@/pages/project/preview/preview";


export default function ProjectPreviewPage() {

    const {id} = useParams();
    const t = useLocale(locale);

    return (
        <ProjectPreviewPanel id={id} />
    );
}