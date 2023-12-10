import React, {useEffect, useState} from 'react';
import styles from './style/index.module.less';
import {Space, Tabs,Typography} from "@arco-design/web-react";
import GroupAddPanel from "@/pages/group/add/group_add";
import GroupManagePanel from "@/pages/group/manage";
import {useParams} from "react-router-dom";
import StatDisplayPanel from "@/pages/display/stat/stat_display";
import StatDisplay from "@/pages/stat/display";
import ProjectTree from "@/pages/project/common/project-tree";

export default function MetricDisplay() {

    return (
        // <div style={{ minHeight:500 }}>
        //     <div className={styles.layout}>
        //         <div className={styles['layout-left-side']}>
        //             <Space size={24} direction="vertical" className={styles.left}>
        //                 <ProjectTree projectId={0} filterTypes={[1,2]} handlerProcess={null} />
        //             </Space>
        //             <StatDisplay />
        //         </div>
        //     </div>
        // </div>

        <div style={{ minHeight:500 }}>
            <div className={styles.layout}>
                <div className={styles['layout-left-side']} style={{ border:'1px solid var(--color-neutral-3)'}}>
                    <Space size={24} direction="vertical" className={styles.left}>
                        <ProjectTree projectId={0} filterTypes={[1,2]} handlerProcess={null} />
                    </Space>
                </div>
                <StatDisplay />
            </div>
        </div>
    );
}