import React from 'react';
import styles from './style/index.module.less';
import './mock';
import ProjectTree from "./project-manage";
import {Space} from "@arco-design/web-react";
import Studio from "./studio";
import StudioStatus from "@/pages/dashboard/monitor/studio-status";
import QuickOperation from "@/pages/dashboard/monitor/quick-operation";
import StudioInformation from "@/pages/dashboard/monitor/studio-information";
import GroupManage from "@/pages/project/manage/group_manage";

export default function ProjectManage() {
  return (
    <div style={{ minHeight:500 }}>
      <div className={styles.layout}>
        <div className={styles['layout-left-side']}>
          <ProjectTree />
        </div>

          <div className={styles['layout-content']}>
              <Space size={16} direction="vertical" style={{ width: '100%' }}>
                  <Studio userInfo={null} />
                  <GroupManage />
              </Space>
          </div>
          <div className={styles['layout-right-side']}>
              <Space size={16} direction="vertical" style={{ width: '100%' }}>
                  <StudioStatus />
                  <QuickOperation />
                  <StudioInformation />
              </Space>
          </div>
      </div>
    </div>
  );
}

