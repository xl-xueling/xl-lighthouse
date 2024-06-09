import React, {useEffect, useState} from 'react';
import {
    Grid,
} from "@arco-design/web-react";
import {useRouter} from "next/router";
const {Row, Col} = Grid;
import ProjectPreviewPanel from "@/pages/project/preview/ProjectPreviewPanel";

export default function ProjectPreviewPage() {
    const router = useRouter();
    const { id } = router.query;
    return (
        <ProjectPreviewPanel id={id}/>
    );
}