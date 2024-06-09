import React, {useEffect, useState} from 'react';
import styles from './style/index.module.less';
import {
    Breadcrumb,
    Card,
    Descriptions, Grid,
    Link,
    Notification,
    Skeleton,
    Space,
    Spin,
    Typography
} from "@arco-design/web-react";
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
import {ResourceTypeEnum} from "@/types/insights-common";
import {getIcon} from "@/desc/base";
import { useRouter } from 'next/router';
import PermissionManageModal from "@/pages/permission/PermissionManageModal";
const BreadcrumbItem = Breadcrumb.Item;

export default function ProjectManagePanel({id}) {

    const t = useLocale(locale);
    const [groupId, setGroupId] = useState<number>(null);
    const [showGroupCreatePanel, setShowGroupCreatePanel] = useState(false);
    const [showManagePanel, setShowManagePanel] = useState(false);
    const [showPermissionManageModal, setShowPermissionManageModal] = useState(false);
    const [errorCode, setErrorCode] = useState<string>(null);
    const [projectInfo, setProjectInfo] = useState<Project>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const router = useRouter();
    const {Row, Col} = Grid;
    const { Text } = Typography;

    const menuCallback = async (id: number) => {
        setGroupId(id);
        setShowManagePanel(true);
    }

    const handlerCreateGroup = () => {
        setShowGroupCreatePanel(true);
    }

    const handlerPermissionManage = () => {
        setShowPermissionManageModal(true);
    }

    const callback = async (operation, data) => {
        switch (operation) {
            case "create-group":
                const newGroup: TreeNode = {
                    key: 'group_' + data.id,
                    label: data.token,
                    value: data.id,
                    type: 'group',
                }
                let groups = projectInfo.structure.children ? projectInfo.structure.children : [];
                groups = [...groups, newGroup];
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
                }, 0);
                break;
            }
            default:
                break;
        }
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        await requestQueryById({id}).then((response) => {
            const {code, data, message} = response;
            if (code == '0') {
                setProjectInfo(data);
            } else if (GlobalErrorCodes.includes(String(code))) {
                setErrorCode(code);
            } else {
                Notification.warning({style: {width: 420}, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error)
        })
    }

    useEffect(() => {
        fetchData().then();
    }, [])


    const descriptionData = [
        {

            label: <span style={{marginTop:'2px'}}>{getIcon('project')}</span>,
            value: <span style={{wordBreak: 'break-word'}}>
                <Link className={'ldp-custom-link'} href={'/project/preview/' + projectInfo?.id} target={'_self'}>
                        {projectInfo?.title}
                    </Link>
                {" ["+projectInfo?.id+"]"}
            </span>
        },
        {
            label: <IconBook/>,
            value: <span style={{wordBreak: 'break-word'}}>{projectInfo?.desc}</span>
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
            label: <IconClockCircle/>,
            value: formatTimeStampBackUp(projectInfo?.createTime),
        },
    ];

    return (
        <>
            {
                errorCode ? <ErrorPage errorCode={errorCode}/>
                    :
                    <>
                        <Row>
                            <Col span={16}>
                                <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                                    <Breadcrumb.Item>
                                        <IconHome/>
                                    </Breadcrumb.Item>
                                    <BreadcrumbItem
                                        style={{fontWeight: 20}}>{t['projectManage.breadcrumbItem']}</BreadcrumbItem>
                                </Breadcrumb>
                            </Col>
                        </Row>

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
                                                <div className={styles.item} onClick={handlerPermissionManage}>
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
                                    {showManagePanel &&
                                    <Card><GroupManagePanel projectInfo={projectInfo} groupId={groupId}
                                                            deleteCallback={callback}/></Card>}
                                </div>
                                {showGroupCreatePanel && <GroupCreateModal projectId={id} callback={callback}
                                                                           onClose={() => setShowGroupCreatePanel(false)}/>}
                                {showPermissionManageModal &&
                                <PermissionManageModal resourceId={id} resourceType={ResourceTypeEnum.Project}
                                                       onClose={() => setShowPermissionManageModal(false)}/>}
                            </div>
                        </Spin>
                    </>
            }
        </>
    );
}

