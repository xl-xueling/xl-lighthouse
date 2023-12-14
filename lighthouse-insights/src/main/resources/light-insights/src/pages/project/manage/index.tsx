import React, {useEffect, useState} from 'react';
import styles from './style/index.module.less';
import ProjectTree from "../common/project-tree";
import {
    Breadcrumb,
    Button,
    Card,
    Descriptions, Divider,
    Link,
    Skeleton,
    Space,
    Tabs,
    Tag,
    Typography
} from "@arco-design/web-react";
import GroupAddPanel from "@/pages/group/add/group_add";
import GroupManagePanel from "@/pages/group/manage";
import {useParams} from "react-router-dom";
import ProjectMenu from "@/pages/project/display/menu";
import {PrivilegeEnum, Project} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import {requestQueryByIds} from "@/api/project";
import ProjectManageMenu from "@/pages/project/manage/menu";
import {IconFile, IconFire, IconHome, IconMobile, IconSettings, IconStorage} from "@arco-design/web-react/icon";
import Announcement from "@/pages/dashboard/workplace/announcement";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/metricset/manage/locale";
const BreadcrumbItem = Breadcrumb.Item;


export default function ProjectManage() {

    const t = useLocale(locale);
  const [groupId,setGroupId] = useState<number>(null);

  const [showAddPanel, setShowAddPanel] = useState(false);

  const [showManagePanel, setShowManagePanel] = useState(false);

  const [projectInfo,setProjectInfo] = useState<Project>(null);
    const [loading,setLoading] = useState<boolean>(true);
  const { id } = useParams();

    const fetchProjectInfo:Promise<Project> = new Promise<Project>((resolve,reject) => {
        const proc = async () => {
            const result = await requestQueryByIds([id]);
            resolve(result.data[id]);
        }
        proc().then();
    })

    const fetchPrivilegeInfo = async(ids) => {
        return new Promise<Record<number,PrivilegeEnum[]>>((resolve,reject) => {
            requestPrivilegeCheck({type:"project",ids:ids}).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    const menuCallback = async (id) => {
        console.log("click id:" + id);
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const result = await Promise.all([fetchProjectInfo]);
        const projectInfo = result[0];
        Promise.all([fetchPrivilegeInfo([id])])
            .then(([r1]) => {
                const combinedItem = { ...projectInfo, ...{"permissions":r1[projectInfo.id]}};
                setProjectInfo(combinedItem);
                setLoading(false);
            }).catch((error) => {
            console.log(error);
        })
    }

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

    useEffect(() => {
        fetchData().then();
        setGroupId(1);
    },[])

    const data = [
        {
            label: 'Description',
            value: 'Yingdu Building, Zhichun Road, BeijingYingdu Building, Zhichun Road, BeijingYingdu Building, Zhichun Road, BeijingYingdu Building, Zhichun Road, Beijing',
        },
        {
            label: 'CreatedTime',
            value: 'Socrates',
        },
        {
            label: 'Admins',
            value: 'Beijing',
        },

    ];

    const shortcuts = [
        {
            title: '创建统计组',
            key: 'Content Management',
            icon: <IconFile />,
        },
        {
            title: '内容管理',
            key: 'Content Statistic',
            icon: <IconStorage />,
        },
        {
            title: '内容管理',
            key: 'Advanced Management',
            icon: <IconSettings />,
        },
        {
            title: '内容管理',
            key: 'Online Promotion',
            icon: <IconMobile />,
        },
        {
            title: '内容管理',
            key: 'Marketing',
            icon: <IconFire />,
        },
    ];

    const recentShortcuts = [
        {
            title: '内容管理',
            key: 'Content Statistic',
            icon: <IconStorage />,
        },
        {
            title: '内容管理',
            key: 'Content Management',
            icon: <IconFile />,
        },
        {
            title: '内容管理',
            key: 'Advanced Management',
            icon: <IconSettings />,
        },
    ];

  return (
      <>
          {/*<Breadcrumb style={{ fontSize: 11,marginBottom:'8px' }}>*/}
          {/*    <BreadcrumbItem>*/}
          {/*        <IconHome />*/}
          {/*    </BreadcrumbItem>*/}
          {/*    <BreadcrumbItem>工程管理</BreadcrumbItem>*/}
          {/*</Breadcrumb>*/}

      <div className={styles.layout}>
          <div className={styles['layout-left-side']}>
              <Space size={15} direction="vertical" style={{width:'100%'}}>
                  <ProjectManageMenu structure={projectInfo?.structure} callback={menuCallback} />
                  <Card>
                      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Typography.Title heading={6}>
                              {"工程管理"}
                          </Typography.Title>
                          <Link>{t['workplace.seeMore']}</Link>
                      </div>
                      <div className={styles.shortcuts}>
                          {shortcuts.map((shortcut) => (
                              <div
                                  className={styles.item}
                                  key={shortcut.key}
                              >
                                  <div className={styles.icon}>{shortcut.icon}</div>
                                  <div className={styles.title}>{shortcut.title}</div>
                              </div>
                          ))}
                      </div>
                  </Card>
                  <Card>
                      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Typography.Title heading={6}>
                              {"描述信息"}
                          </Typography.Title>
                      </div>
                      <div>
                          <Descriptions colon=' :' layout='horizontal'
                                        style={{whiteSpace:"normal"}}
                                        data={data} column={1}/>
                      </div>
                  </Card>
              </Space>
          </div>
          <div className={styles['layout-content']}>
              <Card>
                  <GroupManagePanel groupId={groupId}/>
              </Card>
          </div>

          {/*{showAddPanel && <GroupAddPanel onClose={() => setShowAddPanel(false)}/>}*/}
      </div>
      </>
  );
}

