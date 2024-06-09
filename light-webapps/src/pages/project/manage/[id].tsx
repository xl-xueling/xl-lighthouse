import React, {useEffect, useState} from 'react';
import { useRouter } from 'next/router';
import ProjectManagePanel from "@/pages/project/manage/ProjectManagePanel";

export default function ProjectManagePage() {

    const router = useRouter();
    const { id } = router.query;
    return (
      <ProjectManagePanel id={id} />
    );
}

