import React, {useState} from 'react';
import styles from './style/index.module.less';
import ProjectTree from "../common/project-tree";
import {Space} from "@arco-design/web-react";
import GroupAddPanel from "@/pages/project/manage/group_add";
import GroupBasicInfo from "@/pages/project/manage/group_basic";
import GroupEditPanel from "@/pages/project/manage/group_edit";

export default function ProjectManage() {

  const [showGroupAddPanel, setShowGroupAddPanel] = useState(false);

  const [showGroupEditPanel, setShowGroupEditPanel] = useState(false);

  const [groupId,setGroupId] = useState<number>(null);

  const handlerProcess = (action:string,params:any):void => {
      switch (action){
          case 'add-group':{
              setShowGroupAddPanel(true);
              break;
          }
          case 'edit-group':{
              setGroupId(params.groupId);
              setShowGroupEditPanel(true);
              break;
          }
          default:{
              return;
          }
      }
  }

  return (
    <div style={{ minHeight:500 }}>
      <div className={styles.layout}>
        <div className={styles['layout-left-side']}>
          <ProjectTree projectId={0} filterTypes={[1,2]} handlerProcess={handlerProcess}/>
        </div>
          <div className={styles['layout-content']} style={{ display:showGroupAddPanel ? 'block' : 'none' }}>
              <Space size={16} direction="vertical" style={{ width: '100%' }}>
                  <GroupAddPanel />
              </Space>
          </div>

          <div className={styles['layout-content']} style={{ display:showGroupEditPanel ? 'block' : 'none' }}>
              <Space size={16} direction="vertical" style={{ width: '100%' }}>
                  <GroupEditPanel groupId={groupId} />
              </Space>
          </div>
      </div>
    </div>
  );
}

