import React from 'react';
import {Breadcrumb,Grid} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import { useRouter } from 'next/router';
const {Row, Col} = Grid;

import StatPreviewPanel from "@/pages/stat/preview/StatPreviewPanel";

export default function StatPreviewPage() {
    const router = useRouter();
    const { id } = router.query;
    const t = useLocale(locale);

    return (
        <>
            <>
                <StatPreviewPanel id={id}/>
            </>
        </>
    );
}