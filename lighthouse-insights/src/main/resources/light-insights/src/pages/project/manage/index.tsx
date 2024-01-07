import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from './style/index.module.less';
import {
    Breadcrumb,
    Card, Descriptions,
    Link, Notification, Skeleton,
    Space, Spin,
    Typography
} from "@arco-design/web-react";
import GroupManagePanel from "@/pages/group/manage";
import {ArcoTreeNode, Project, TreeNode} from "@/types/insights-web";
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
import DepartmentLabel from "@/pages/department/common/depart";
import { ImTree } from "react-icons/im";

export default function ProjectManage() {

  const t = useLocale(locale);
  const [groupId,setGroupId] = useState<number>(null);
  const [showGroupCreatePanel, setShowGroupCreatePanel] = useState(false);
  const [showManagePanel, setShowManagePanel] = useState(false);
  const [projectInfo,setProjectInfo] = useState<Project>(null);
  const [loading,setLoading] = useState<boolean>(true);
  const { id } = useParams();

    const menuCallback = async (id) => {
        setGroupId(Number(id));
        setShowManagePanel(true);
    }

    const handlerCreateGroup = () => {
        setShowGroupCreatePanel(true);
    }

    const callback = async (operation,data) => {
        switch (operation){
            case "create-group":
                const newGroup:TreeNode = {
                    "id":String(data.id),
                    "name":data.token,
                    "pid":String(projectInfo.id),
                    "type":'2',
                }
                let groups = projectInfo.structure[0].children?projectInfo.structure[0].children:[];
                groups = [...groups,newGroup];
                projectInfo.structure[0].children = groups;
                setProjectInfo(projectInfo);
                break;
            default:
                break;
        }
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        await requestQueryById({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setProjectInfo(data);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error)
        })
    }

    useEffect(() => {
        fetchData().then();
    },[])


    const descriptionData = [
        {
            label: <IconBook/>,
            value: <span style={{ wordBreak: 'break-word' }}>{projectInfo?.desc}</span>
        },
        {
            label: <IconUserGroup/>,
            value: <UserGroup users={projectInfo?.admins}/>,
        },
        {
            label: <ImTree/>,
            value: <DepartmentLabel departmentId={projectInfo?.departmentId}/>,
        },
        {
            label: <IconClockCircle />,
            value: formatTimeStamp(projectInfo?.createTime),
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
      <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
          <BreadcrumbItem>
              <IconHome />
          </BreadcrumbItem>
          <BreadcrumbItem style={{fontWeight:20}}>{t['projectManage.breadcrumbItem']}</BreadcrumbItem>
      </Breadcrumb>
      <Spin loading={loading} style={{ width: '100%' }}>
        <div className={styles.layout}>
          <div className={styles['layout-left-side']}>
              <Space size={15} direction="vertical" style={{width:'100%'}}>
                  <ProjectManageMenu structure={projectInfo?.structure} callback={menuCallback} />
                  <Card>
                      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Typography.Title heading={6}>
                              {"工程管理"}
                          </Typography.Title>
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
                  <Card>
                      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Typography.Title heading={6}>
                              {"描述信息"}
                          </Typography.Title>
                      </div>
                      <div>
                          <Skeleton
                              loading={loading}
                              text={{
                                  rows:3,
                                  width: ['100%'],
                              }}
                              animation
                          >
                          <Descriptions colon=' :' layout='horizontal'
                                        labelStyle={{ textAlign: 'left', width:'24px',}}
                                        style={{whiteSpace:"normal"}}
                                        data={descriptionData} column={1}/>
                          </Skeleton>
                      </div>
                  </Card>
              </Space>
          </div>
          {/*<div className={styles['layout-content']}>*/}
          {/*    <Card>*/}
          {/*        /!*{showManagePanel && <GroupManagePanel groupId={groupId}/>}*!/*/}
          {/*    </Card>*/}
          {/*</div>*/}

          {showGroupCreatePanel && <GroupCreateModal projectId={id} callback={callback} onClose={() => setShowGroupCreatePanel(false)}/>}
      </div>
      </Spin>
      </>
  );
}

