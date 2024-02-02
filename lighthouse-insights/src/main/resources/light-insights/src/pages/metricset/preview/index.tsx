import React, {createContext, useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
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
    Button, Dropdown, Menu
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
import MetricSetPermissionsPanel from "@/pages/metricset/permissions";
import {ResourceTypeEnum} from "@/types/insights-common";
import {VscGistSecret} from "react-icons/vsc";
import {GlobalErrorCodes} from "@/utils/constants";
import ErrorPage from "@/pages/common/error";
import {FaRegChartBar} from "react-icons/fa";
import {AiOutlineDashboard} from "react-icons/ai";
import {CiViewTable} from "react-icons/ci";
import {HiMiniBoltSlash} from "react-icons/hi2";
import {RiShieldKeyholeLine} from "react-icons/ri";
import {getIcon} from "@/pages/common/desc/base";
import { FiEdit } from "react-icons/fi";
import {PermissionManageModal} from "@/pages/permission/PermissionManageModal";
import MetricSetUpdateModal from "@/pages/metricset/update";
import {useSelector} from "react-redux";
import {addMetricPreviewHistory} from "@/pages/metricset/preview/history";
import {deepCopyObject} from "@/utils/util";

export const MetricSetPreviewContext = React.createContext(null)

export default function Index() {
    const {id} = useParams();
    const t = useLocale(locale);
    const [loading, setLoading] = useState<boolean>(false);
    const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);
    const [metricSetInfo, setMetricSetInfo] = useState<MetricSet>(null);
    const [reloadTime, setReloadTime] = useState<number>(Date.now());
    const [errorCode, setErrorCode] = useState<string>(null);
    const [showPermissionManageModal,setShowPermissionManageModal] = useState<boolean>(false);
    const [showUpdatePanel,setShowUpdatePanel] = useState<boolean>(false);

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
        <MetricSetPreviewContext.Provider value={{metricSetInfo, setMetricSetInfo, reloadTime, setReloadTime}}>
            <>
                {
                    errorCode ? <ErrorPage errorCode={errorCode}/>
                        :
                        <>
                            <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                                <Breadcrumb.Item>
                                    <IconHome/>
                                </Breadcrumb.Item>
                                <Breadcrumb.Item
                                    style={{fontWeight: 20}}>{t['metricSetPreview.breadcrumb']}</Breadcrumb.Item>
                            </Breadcrumb>
                            <Space size={16} direction="vertical" style={{width: '100%'}}>
                                <Card>
                                    <MetricSetPreviewHeader/>
                                </Card>

                                <Tabs type="line" defaultActiveTab={'1'}
                                      extra={
                                          <Space size={1}>
                                              <Button type={"secondary"}  size={"mini"} onClick={() => setShowUpdatePanel(true)} icon={<FiEdit/>}>{'修改指标集'}</Button>
                                              <Button type={"secondary"}  size={"mini"} onClick={() => setShowPermissionManageModal(true)} icon={getIcon('permission')}>{'权限管理'}</Button>
                                              <Dropdown
                                                  position={"br"}
                                                  trigger={"click"}
                                                  droplist={
                                                      <Menu style={{ maxHeight:'280px' }}>
                                                          <Menu.Item key={'deleteGroup'}>
                                                              <Button type={"secondary"} shape={"circle"} size={"mini"} icon={<IconDelete/>} />&nbsp;&nbsp;
                                                              {'删除指标集'}</Menu.Item>
                                                      </Menu>
                                                  }>
                                                  <Button size={"mini"} type={"secondary"}><IconDownCircle />{'更多'}</Button>
                                              </Dropdown>
                                          </Space>
                                      }
                                >
                                    <TabPane
                                        key='1'
                                        title={
                                            <span style={{display: "inline-flex", alignItems: "center"}}><IconDashboard
                                                style={{marginRight: 6}}/>{t['metricSetPreview.tab.title.dataView']}</span>
                                        }>
                                        <MetricSetDataViewPanel parentLoading={loading}/>
                                    </TabPane>
                                    <TabPane
                                        key='2'
                                        title={
                                            <span style={{display: "inline-flex", alignItems: "center"}}><PiLinkSimple
                                                style={{marginRight: 6}}/>{t['metricSetPreview.tab.title.bindItems']}</span>
                                        }>
                                        {metricSetInfo && <MetricSetBindListPanel/>}
                                    </TabPane>
                                    <TabPane key='3' title={
                                        <span>
                <span style={{display: "inline-flex", alignItems: "center"}}><PiTreeStructure
                    style={{marginRight: 6}}/>{t['metricSetPreview.tab.title.structure']}</span>
                </span>}>
                                        {metricSetInfo && <MetricSetStructurePanel/>}
                                    </TabPane>
                                </Tabs>
                            </Space>
                        </>
                }

                {showPermissionManageModal &&
                <PermissionManageModal resourceId={id} resourceType={ResourceTypeEnum.Metric}
                                       onClose={() => setShowPermissionManageModal(false)}/>}
                {showUpdatePanel && <MetricSetUpdateModal metricInfo={metricSetInfo} onClose={() => setShowUpdatePanel(false)} onSuccess={() => {console.log("-")}} />}
            </>
        </MetricSetPreviewContext.Provider>
    );
}