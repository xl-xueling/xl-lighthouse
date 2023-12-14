import React, {useEffect, useState} from 'react';
import styles from './style/index.module.less';
import ProjectTree from "../common/project-tree";
import {Card, Space, Tabs, Typography} from "@arco-design/web-react";
import GroupAddPanel from "@/pages/group/add/group_add";
import GroupManagePanel from "@/pages/group/manage";
import {useParams} from "react-router-dom";

export default function ProjectManage() {

  const [groupId,setGroupId] = useState<number>(null);

  const [showAddPanel, setShowAddPanel] = useState(false);

  const [showManagePanel, setShowManagePanel] = useState(false);

  const { id } = useParams();

  const handlerProcess = (action:string,params:any):void => {
      switch (action){
          case 'group-add':{
              setShowAddPanel(true);
              break;
          }
          case 'selected-group':{
              setGroupId(params.groupId);
              setShowManagePanel(true);
              break;
          }
          default:{
              return;
          }
      }
  }

  return (
      <div className={styles.layout}>
          <div className={styles['layout-left-side']}>
              <Card>
                  <ProjectTree projectId={id} filterTypes={[1,2]} handlerProcess={handlerProcess} />
              </Card>
          </div>

          {showManagePanel && <GroupManagePanel groupId={groupId}/>}

          {showAddPanel && <GroupAddPanel onClose={() => setShowAddPanel(false)}/>}
      </div>
  );
}

