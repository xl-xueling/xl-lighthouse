import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from './style/index.module.less';
import {
    Breadcrumb,
    Card, Descriptions,
    Link,
    Space,
    Typography
} from "@arco-design/web-react";
import GroupManagePanel from "@/pages/group/manage";
import {Project} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import {requestQueryById, requestQueryByIds} from "@/api/project";
import ProjectManageMenu from "@/pages/project/manage/menu";
import {IconBook, IconClockCircle, IconHome, IconUserGroup} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
const BreadcrumbItem = Breadcrumb.Item;
import { VscGistSecret } from "react-icons/vsc";
import {CiViewTable} from "react-icons/ci";
import GroupCreateModal from "@/pages/group/create/group_create";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStamp} from "@/utils/util";

export default function ProjectManage() {

  const t = useLocale(locale);
  const [groupId,setGroupId] = useState<number>(null);
  const [showGroupCreatePanel, setShowGroupCreatePanel] = useState(false);
  const [showManagePanel, setShowManagePanel] = useState(false);
  const [projectInfo,setProjectInfo] = useState<Project>(null);
  const [loading,setLoading] = useState<boolean>(true);
  const { id } = useParams();

    const fetchProjectInfo:Promise<Project> = new Promise<Project>((resolve,reject) => {
        const proc = async () => {
            const result = await requestQueryById({id});
            resolve(result.data);
        }
        proc().then();
    })

    const menuCallback = async (id) => {
        setGroupId(Number(id));
        setShowManagePanel(true);
    }

    const handlerCreateGroup = () => {
        setShowGroupCreatePanel(true);
    }

    const callback = async (record,type) => {
        switch (type){
            case "create-group":

                break;
            default:
                break;
        }
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const result = await Promise.all([fetchProjectInfo]);
        setProjectInfo(result[0]);
    }

  // const handlerProcess = (action:string,params:any):void => {
  //     switch (action){
  //         case 'group-add':{
  //             setShowAddPanel(true);
  //             break;
  //         }
  //         case 'selected-group':{
  //             setGroupId(params.groupId);
  //             setShowManagePanel(true);
  //             break;
  //         }
  //         default:{
  //             return;
  //         }
  //     }
  // }

    useEffect(() => {
        fetchData().then();
        setGroupId(1);
    },[])

    const data = [
        {
            label: <IconBook/>,
            value: <span style={{ wordBreak: 'break-word' }}>{projectInfo?.desc}</span>
        },
        {
            label: <IconClockCircle />,
            value: formatTimeStamp(projectInfo?.createTime),
        },
        {
            label: <IconUserGroup/>,
            value: <UserGroup users={projectInfo?.admins}/>,
        },

    ];

    const shortcuts = [
        {
            title: '创建统计组',
            key: 'Content Management',
            icon: <CiViewTable />,
        },
        {
            title: '权限管理',
            key: 'Content Statistic',
            icon: <VscGistSecret />,
        },
    ];

  return (
      <>
      {/*<Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>*/}
      {/*    <BreadcrumbItem>*/}
      {/*        <IconHome />*/}
      {/*    </BreadcrumbItem>*/}
      {/*    <BreadcrumbItem style={{fontWeight:20}}>{t['projectManage.breadcrumbItem']}</BreadcrumbItem>*/}
      {/*</Breadcrumb>*/}

      <div className={styles.layout}>
          <div className={styles['layout-left-side']}>
              <Space size={15} direction="vertical" style={{width:'100%'}}>
                  <Card>
                      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Typography.Title heading={6}>
                              {"工程管理"}
                          </Typography.Title>
                          <Link>{t['workplace.seeMore']}</Link>
                      </div>
                      <div className={styles.shortcuts}>
                          <div className={styles.item} onClick={handlerCreateGroup}>
                              <div className={styles.icon}><CiViewTable /></div>
                              <div className={styles.title}>创建统计组</div>
                          </div>
                          <div className={styles.item}>
                              <div className={styles.icon}><VscGistSecret /></div>
                              <div className={styles.title}>权限管理</div>
                          </div>
                      </div>
                  </Card>
                  {projectInfo?.structure.length > 0 && <ProjectManageMenu structure={projectInfo.structure[0]?.children} callback={menuCallback} />}
                  <Card>
                      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Typography.Title heading={6}>
                              {"描述信息"}
                          </Typography.Title>
                      </div>
                      <div>
                          <Descriptions colon=':' layout='horizontal'
                                        labelStyle={{ textAlign: 'left', width:'24px',}}
                                        style={{whiteSpace:"normal"}}
                                        data={data} column={1}/>
                      </div>
                  </Card>
              </Space>
          </div>
          <div className={styles['layout-content']}>
              <Card>
                  {showManagePanel && <GroupManagePanel groupId={groupId}/>}
              </Card>
          </div>

          {showGroupCreatePanel && <GroupCreateModal projectId={id} callback={callback} onClose={() => setShowGroupCreatePanel(false)}/>}
      </div>
      </>
  );
}

