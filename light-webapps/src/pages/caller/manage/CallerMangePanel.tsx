import React, {useEffect, useState} from "react";
import {Caller} from "@/types/caller";
import {requestQueryById} from "@/api/caller";
import {Breadcrumb, Button, Card, Grid, Link, Notification, Space, Tabs, Typography} from "@arco-design/web-react";
import {CallerManageContext} from "@/pages/common/context";
import {IconHome} from "@arco-design/web-react/icon";
import Header from "@/pages/caller/manage/header";
import {FiEdit, FiMonitor} from "react-icons/fi";
import {getIcon} from "@/pages/common/desc/base";
import {RiShieldKeyholeLine} from "react-icons/ri";
import {AiOutlineSafety} from "react-icons/ai";
import AuthList from "@/pages/caller/manage/auth_list";
import CallerUpdateModal from "@/pages/caller/update";
import SecretKeyModal from "@/pages/caller/manage/secret_key";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/caller/manage/locale";
const TabPane = Tabs.TabPane;
import { useLocation, useHistory } from 'react-router-dom';
import {PermissionManageModal} from "@/pages/permission/PermissionManageModal";
import {ResourceTypeEnum} from "@/types/insights-common";
const {Row, Col} = Grid;
const { Text } = Typography;

export default function CallerManagePanel({id,PRO_ViewBindTab=null}){

    const t = useLocale(locale);

    const [callerInfo,setCallerInfo] = useState<Caller>(null);
    const history = useHistory();

    const [showUpdateModal,setShowUpdateModal] = useState<boolean>(false);
    const [showPermissionManageModal,setShowPermissionManageModal] = useState<boolean>(false);
    const [loading,setLoading] = useState<boolean>(false);

    const [showSecretKeyModal,setShowSecretKeyModal] = useState<boolean>(false);

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        await requestQueryById({id}).then((response) => {
            const {code, data, message} = response;
            if (code == '0') {
                setCallerInfo(data);
            } else {
                Notification.warning({style: {width: 420}, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const goBack = () => {
        history.goBack();
    };

    const handlerUpdateProcess = (callerInfo) => {
        setCallerInfo(callerInfo);
    }

    useEffect(() => {
        fetchData().then();
    },[id])

    return (
        <CallerManageContext.Provider value={{callerInfo,PRO_ViewBindTab}}>
            <Row>
                <Col span={16}>
                    <Breadcrumb  style={{fontSize: 12, marginBottom: '10px'}}>
                        <Breadcrumb.Item>
                            <IconHome/>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item style={{fontWeight: 20}}>{t['callerManage.title']}</Breadcrumb.Item>
                    </Breadcrumb>
                </Col>
                <Col span={8} style={{textAlign:'right',fontSize:'13px',color:'#43454a'}}>
                    <Text style={{cursor:'pointer'}} onClick={() => history.goBack()}>[{t['basic.route.back']}]</Text>
                </Col>
            </Row>
            <Space size={16} direction="vertical" style={{width: '100%'}}>
                <Card>
                    <Header callerInfo={callerInfo}/>
                </Card>
                <Tabs defaultActiveTab='2' extra={
                    <Space size={1}>
                        <Button type={"secondary"}  size={"mini"} icon={<FiEdit/>} onClick={() => setShowUpdateModal(true)}>{t['callerManage.button.update']}</Button>
                        <Button type={"secondary"}  size={"mini"} icon={getIcon('permission')} onClick={() => setShowPermissionManageModal(true)}>{t['callerManage.button.permission']}</Button>
                        <Button type={"secondary"}  size={"mini"} icon={<RiShieldKeyholeLine/>} onClick={() => setShowSecretKeyModal(true)}>{t['callerManage.button.secretKey']}</Button>
                    </Space>

                }>
                    <TabPane
                        key='1'
                        title={
                            <span style={{ display: "inline-flex", alignItems: "center" }}><FiMonitor size={12} style={{ marginRight: 6 }} />{t['callerManage.tab.monitor']}</span>
                        }
                    >
                        <Typography.Paragraph>Content of Tab Panel 1</Typography.Paragraph>
                    </TabPane>
                    <TabPane
                        key='2'
                        title={
                            <span style={{ display: "inline-flex", alignItems: "center" }}><AiOutlineSafety size={14} style={{ marginRight: 6 }} />{t['callerManage.tab.authorization']}</span>
                        }>
                        <AuthList/>
                    </TabPane>
                </Tabs>

                {showUpdateModal && <CallerUpdateModal callerInfo={callerInfo} onClose={() => setShowUpdateModal(false)} onSuccess={handlerUpdateProcess} />}
                {showSecretKeyModal && <SecretKeyModal callerId={callerInfo?.id} onClose={() => setShowSecretKeyModal(false)} />}
                {showPermissionManageModal &&
                <PermissionManageModal resourceId={id} resourceType={ResourceTypeEnum.Caller}
                                       onClose={() => setShowPermissionManageModal(false)}/>}
            </Space>
        </CallerManageContext.Provider>
    );
}