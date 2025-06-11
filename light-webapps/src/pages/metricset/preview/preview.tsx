import React, {createContext, useEffect, useState} from 'react';
import {Switch, Route, Redirect, useHistory,useParams} from 'react-router-dom';
import {
    Card,
    Typography,
    Grid,
    Space,
    Tabs,
    Divider,
    Notification,
    Breadcrumb,
    Spin,
    Button, Dropdown, Menu, Modal
} from '@arco-design/web-react';
import MetricSetPreviewHeader from "@/pages/metricset/preview/header";
import {
    IconDashboard,
    IconDelete,
    IconDownCircle,
    IconHome,
    IconTag,
    IconThunderbolt
} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {requestQueryById} from "@/api/metricset";
import {MetricSet} from "@/types/insights-web";
import MetricSetDataViewPanel from "@/pages/metricset/preview/dataview/MetricSetDataViewPanel";
import MetricSetBindListPanel from "@/pages/metricset/binded/list";
const {Title} = Typography;
const {Row, Col} = Grid;
const TabPane = Tabs.TabPane;
import {PiLinkSimple, PiTreeStructure} from "react-icons/pi";
import MetricSetStructurePanel from "@/pages/metricset/structure";
import {PermissionEnum, ResourceTypeEnum} from "@/types/insights-common";
import {VscGistSecret} from "react-icons/vsc";
import {GlobalErrorCodes} from "@/utils/constants";
import ErrorPage from "@/pages/common/error";
import {getIcon} from "@/pages/common/desc/base";
import { FiEdit } from "react-icons/fi";
import {PermissionManageModal} from "@/pages/permission/PermissionManageModal";
import MetricSetUpdateModal from "@/pages/metricset/update";
import {useDispatch, useSelector} from "react-redux";
import {addMetricPreviewHistory} from "@/pages/metricset/preview/history";
import {deepCopyObject} from "@/utils/util";
import {requestDeleteById} from "@/api/metricset";
import {MetricSetPreviewContext} from "@/pages/common/context";
import {GlobalState, updateStoreStaredMetricInfo} from "@/store";

export default function MetricPreviewPanel ({id,PRO_ViewBindTab = null,PRO_ViewPreview = null,PRO_StatPreview = null,PRO_ProjectApplyModal = null,PRO_StatApplyModal = null}) {

    const t = useLocale(locale);
    const history = useHistory();
    const { Text } = Typography;
    const dispatch = useDispatch();
    const [loading, setLoading] = useState<boolean>(false);
    const [initLoading, setInitLoading] = useState<boolean>(true);
    const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo || []);
    const [metricSetInfo, setMetricSetInfo] = useState<MetricSet>(null);
    const [reloadTime, setReloadTime] = useState<number>(Date.now());
    const [errorCode, setErrorCode] = useState<string>(null);
    const [showPermissionManageModal,setShowPermissionManageModal] = useState<boolean>(false);
    const [showUpdatePanel,setShowUpdatePanel] = useState<boolean>(false);
    const [showDeleteMetricConfirm,setShowDeleteMetricConfirm] = useState<boolean>(false);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const proEdition = userInfo?.sysInfo?.proEdition || false;

    const handlerProcess = async (action):Promise<void> => {
        switch (action){
            case 'deleteMetricSet':{
                setShowDeleteMetricConfirm(true);
                break;
            }
            default:{
                return;
            }
        }
    }

    const deleteMetricConfirm = (
        <Modal
            title={t['basic.modal.confirm.delete.title']}
            visible={showDeleteMetricConfirm}
            onOk={() => {
                handlerDeleteMetric();
                setShowDeleteMetricConfirm(false);
            }}
            alignCenter={false}
            style={{top:'200px'}}
            onCancel={() => setShowDeleteMetricConfirm(false)}
            okText={t['basic.form.button.yes']}
            cancelText={t['basic.form.button.no']}
        >
            {t['metricSetPreview.operations.delete.confirm']}
        </Modal>
    );

    const handlerDeleteMetric = async () => {
        setLoading(true);
        await requestDeleteById({id:id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['metricSetPreview.form.submit.deleteSuccess']});
                localStorage.removeItem('cache_stared_metrics');
                const currentFixedData = staredMetricInfo?.filter(x => x.id != id);
                dispatch(updateStoreStaredMetricInfo([...currentFixedData]))
                setTimeout(() => {
                    history.push('/metricset/list');
                },2000)
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        await requestQueryById({id}).then((response) => {
            const {code, data, message} = response;
            if (code == '0') {
                setMetricSetInfo(data);
            } else if (GlobalErrorCodes.includes(String(code))) {
                setErrorCode(code);
            } else {
                Notification.warning({style: {width: 420}, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
            setInitLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        if(metricSetInfo){
            const newObject = deepCopyObject(metricSetInfo);
            delete newObject.structure;
            addMetricPreviewHistory(newObject);
        }
    },[metricSetInfo])


    useEffect(() => {
        fetchData().then();
    }, [reloadTime])

    return (
        <MetricSetPreviewContext.Provider value={{metricSetInfo, setMetricSetInfo, reloadTime, setReloadTime,PRO_ViewBindTab,PRO_ViewPreview,PRO_StatPreview,PRO_ProjectApplyModal,PRO_StatApplyModal}}>
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
                                        <Breadcrumb.Item style={{fontWeight: 20}}>{t['metricSetPreview.breadcrumb']}</Breadcrumb.Item>
                                    </Breadcrumb>
                                </Col>
                                <Col span={8} style={{textAlign:'right',fontSize:'13px',color:'#43454a'}}>
                                    <Text style={{cursor:'pointer'}} onClick={() => history.goBack()}>[{t['basic.route.back']}]</Text>
                                </Col>
                            </Row>
                            <Spin loading={initLoading} style={{ width: '100%',minHeight:'800px' }}>
                                <Space size={16} direction="vertical" style={{width: '100%'}}>
                                    <Card>
                                        <MetricSetPreviewHeader/>
                                    </Card>
                                    <Tabs type="line" defaultActiveTab={'1'}
                                          extra={
                                              metricSetInfo?.permissions.includes(PermissionEnum.ManageAble)?
                                                  <Space size={2}>
                                                      <Button type={"secondary"}  size={"mini"} onClick={() => setShowUpdatePanel(true)} icon={<FiEdit/>}>{t['metricSetPreview.updateMetricSet']}</Button>
                                                      <Button type={"secondary"}  size={"mini"} onClick={() => setShowPermissionManageModal(true)} icon={getIcon('permission')}>{t['metricSetPreview.permissionsManage']}</Button>
                                                      <Dropdown
                                                          position={"br"}
                                                          trigger={"click"}
                                                          droplist={
                                                              <Menu style={{ maxHeight:'280px' }} onClickMenuItem={handlerProcess}>
                                                                  <Menu.Item key={'deleteMetricSet'}>
                                                                      <Button type={"secondary"} shape={"circle"} size={"mini"} icon={<IconDelete/>} />&nbsp;&nbsp;
                                                                      {t['metricSetPreview.deleteMetricSet']}</Menu.Item>
                                                              </Menu>
                                                          }>
                                                          <Button size={"mini"} type={"secondary"}><IconDownCircle />{t['metricSetPreview.more']}</Button>
                                                      </Dropdown>
                                                  </Space>
                                                  :null
                                          }
                                    >
                                        {
                                            metricSetInfo?.permissions.includes(PermissionEnum.AccessAble)?
                                                <TabPane
                                                    key='1'
                                                    title={
                                                        <span style={{display: "inline-flex", alignItems: "center"}}><IconDashboard
                                                            style={{marginRight: 6}}/>{t['metricSetPreview.tab.title.dataView']}</span>
                                                    }>
                                                    <MetricSetDataViewPanel parentLoading={loading}/>
                                                </TabPane>
                                                :null
                                        }

                                        {
                                            metricSetInfo?.permissions.includes(PermissionEnum.AccessAble)?
                                                <TabPane
                                                    key='2'
                                                    title={
                                                        <span style={{display: "inline-flex", alignItems: "center"}}><PiLinkSimple
                                                            style={{marginRight: 6}}/>{t['metricSetPreview.tab.title.bindItems']}</span>
                                                    }>
                                                    {metricSetInfo && <MetricSetBindListPanel/>}
                                                </TabPane>
                                                :null
                                        }
                                        {
                                            metricSetInfo?.permissions.includes(PermissionEnum.ManageAble)?
                                                <TabPane key='3' title={
                                                    <span>
                <span style={{display: "inline-flex", alignItems: "center"}}><PiTreeStructure
                    style={{marginRight: 6}}/>{t['metricSetPreview.tab.title.structure']}</span>
                </span>}>
                                                    {metricSetInfo && <MetricSetStructurePanel/>}
                                                </TabPane>
                                                : null
                                        }
                                    </Tabs>
                                </Space>
                            </Spin>
                        </>
                }

                {showPermissionManageModal &&
                <PermissionManageModal resourceId={id} resourceType={ResourceTypeEnum.Metric}
                                       onClose={() => setShowPermissionManageModal(false)} showTabs={proEdition?['1','2','3']:['3']}/>}
                {showUpdatePanel && <MetricSetUpdateModal onClose={() => setShowUpdatePanel(false)} onSuccess={() => {console.log("-")}} />}
                {deleteMetricConfirm}
            </>
        </MetricSetPreviewContext.Provider>
    );
}