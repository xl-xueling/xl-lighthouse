import React from 'react';
import {Breadcrumb,Grid} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import { useRouter } from 'next/router';
const {Row, Col} = Grid;

import StatPreviewPanel from "@/pages/stat/preview/StatPreviewPanel";
import {IconHome} from "@arco-design/web-react/icon";

export default function StatPreviewPage() {
    const router = useRouter();
    const { id } = router.query;
    const t = useLocale(locale);

    return (
        <>
            <>
                <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                    <Breadcrumb.Item>
                        <IconHome/>
                    </Breadcrumb.Item>
                    <Breadcrumb.Item style={{fontWeight: 20}}>{t['statDisplay.breadcrumbItem']}</Breadcrumb.Item>
                </Breadcrumb>
                <StatPreviewPanel id={id}/>
            </>
        </>
    );
}