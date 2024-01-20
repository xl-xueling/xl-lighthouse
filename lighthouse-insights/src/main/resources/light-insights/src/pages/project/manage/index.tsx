import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from './style/index.module.less';
import {Breadcrumb, Card, Descriptions, Notification, Skeleton, Space, Spin, Typography} from "@arco-design/web-react";
import GroupManagePanel from "@/pages/group/manage";
import {Project, TreeNode} from "@/types/insights-web";
import {requestQueryById} from "@/api/project";
import ProjectManageMenu from "@/pages/project/manage/menu";
import {IconBook, IconClockCircle, IconHome, IconUserGroup} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {VscGistSecret} from "react-icons/vsc";
import {CiViewTable} from "react-icons/ci";
import GroupCreateModal from "@/pages/group/create/group_create";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp, stringifyObj} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import {ImTree} from "react-icons/im";
import {GlobalErrorCodes} from "@/utils/constants";
import ErrorPage from "@/pages/common/error";

const BreadcrumbItem = Breadcrumb.Item;

export default function ProjectManage() {

  const t = useLocale(locale);
  const [groupId,setGroupId] = useState<number>(null);
  const [showGroupCreatePanel, setShowGroupCreatePanel] = useState(false);
  const [showManagePanel, setShowManagePanel] = useState(false);
  const [errorCode,setErrorCode] = useState<string>(null);
  const [projectInfo,setProjectInfo] = useState<Project>(null);
  const [loading,setLoading] = useState<boolean>(true);
  const { id } = useParams();

    const menuCallback = async (id:number) => {
        setGroupId(id);
        setShowManagePanel(true);
    }

    const handlerCreateGroup = () => {
        setShowGroupCreatePanel(true);
    }

    const callback = async (operation,data) => {
        switch (operation){
            case "create-group":
                const newGroup:TreeNode = {
                    key:String(data.id),
                    label:data.token,
                    value:data.id,
                    type:'group',
                }
                let groups = projectInfo.structure.children?projectInfo.structure.children:[];
                groups = [...groups,newGroup];
                projectInfo.structure.children = groups;
                setProjectInfo(projectInfo);
                break;
            case 'deleteGroup': {
                setLoading(true);
                projectInfo.structure.children = projectInfo.structure.children.filter(x => x.value != String(data));
                setProjectInfo(projectInfo);
                setTimeout(() => {
                    setLoading(false);
                    setShowManagePanel(false);
                },0)
                break;
            }
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
            }else if(GlobalErrorCodes.includes(String(code))){
                setErrorCode(code);
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
            value: formatTimeStampBackUp(projectInfo?.createTime),
        },
    ];

  return (
      <>
      {
          errorCode ? <ErrorPage errorCode={errorCode}/>
              :
              <>
                  <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                      <BreadcrumbItem>
                          <IconHome/>
                      </BreadcrumbItem>
                      <BreadcrumbItem style={{fontWeight: 20}}>{t['projectManage.breadcrumbItem']}</BreadcrumbItem>
                  </Breadcrumb>
                  <Spin loading={loading} style={{width: '100%'}}>
                      <div className={styles.layout}>
                          <div className={styles['layout-left-side']}>
                              <Space size={15} direction="vertical" style={{width: '100%'}}>
                                  <ProjectManageMenu structure={projectInfo?.structure} callback={menuCallback}/>
                                  <Card>
                                      <div style={{display: 'flex', justifyContent: 'space-between'}}>
                                          <Typography.Title heading={6}>
                                              {t['projectManage.card.label.projectManage']}
                                          </Typography.Title>
                                      </div>
                                      <div className={styles.shortcuts}>
                                          <div className={styles.item} onClick={handlerCreateGroup}>
                                              <div className={styles.icon}><CiViewTable/></div>
                                              <div
                                                  className={styles.title}>{t['projectManage.shortcuts.createGroup']}</div>
                                          </div>
                                          <div className={styles.item}>
                                              <div className={styles.icon}><VscGistSecret/></div>
                                              <div
                                                  className={styles.title}>{t['projectManage.shortcuts.permissionsManage']}</div>
                                          </div>
                                      </div>
                                  </Card>
                                  <Card>
                                      <Space size={10} direction="vertical" style={{width: '100%'}}>
                                          <div style={{display: 'flex', justifyContent: 'space-between'}}>
                                              <Typography.Title heading={6}>
                                                  {t['projectManage.card.label.description']}
                                              </Typography.Title>
                                          </div>
                                          <div>
                                              <Skeleton
                                                  loading={loading}
                                                  text={{
                                                      rows: 3,
                                                      width: ['100%'],
                                                  }}
                                                  animation
                                              >
                                                  <Descriptions colon=' :' layout='horizontal'
                                                                labelStyle={{textAlign: 'left', width: '24px',}}
                                                                style={{whiteSpace: "normal"}}
                                                                data={descriptionData} column={1}/>
                                              </Skeleton>
                                          </div>
                                      </Space>
                                  </Card>
                              </Space>
                          </div>
                          <div className={styles['layout-content']}>
                              {showManagePanel && <Card><GroupManagePanel projectInfo={projectInfo} groupId={groupId}
                                                                          deleteCallback={callback}/></Card>}
                          </div>
                          {showGroupCreatePanel && <GroupCreateModal projectId={id} callback={callback}
                                                                     onClose={() => setShowGroupCreatePanel(false)}/>}
                      </div>
                  </Spin>
              </>
      }
      </>
  );
}

