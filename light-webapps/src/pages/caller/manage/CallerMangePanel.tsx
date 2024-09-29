import React, {useEffect, useState} from "react";
import {Caller} from "@/types/caller";
import {requestQueryById} from "@/api/caller";
import {Breadcrumb, Button, Card, Notification, Space, Tabs, Typography} from "@arco-design/web-react";
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
const TabPane = Tabs.TabPane;

export default function CallerManagePanel({id}){

    const [callerInfo,setCallerInfo] = useState<Caller>(null);

    const [showUpdateModal,setShowUpdateModal] = useState<boolean>(false);

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


    const handlerUpdateProcess = (callerInfo) => {
        setCallerInfo(callerInfo);
    }

    useEffect(() => {
        fetchData().then();
    },[id])



    return (
        <CallerManageContext.Provider value={{callerInfo}}>
            <>
                <Breadcrumb  style={{fontSize: 12, marginBottom: '10px'}}>
                    <Breadcrumb.Item>
                        <IconHome/>
                    </Breadcrumb.Item>
                    <Breadcrumb.Item style={{fontWeight: 20}}>{'调用方管理'}</Breadcrumb.Item>
                </Breadcrumb>
                <Space size={16} direction="vertical" style={{width: '100%'}}>
                    <Card>
                        <Header callerInfo={callerInfo}/>
                    </Card>
                    <Tabs defaultActiveTab='2' extra={
                        <Space size={1}>
                            <Button type={"secondary"}  size={"mini"} icon={<FiEdit/>} onClick={() => setShowUpdateModal(true)}>{'修改调用方'}</Button>
                            <Button type={"secondary"}  size={"mini"} icon={getIcon('permission')}>{'权限管理'}</Button>
                            <Button type={"secondary"}  size={"mini"} icon={<RiShieldKeyholeLine/>} onClick={() => setShowSecretKeyModal(true)}>{'秘钥信息'}</Button>
                        </Space>

                    }>
                        <TabPane
                            key='1'
                            title={
                                <span>
            <FiMonitor size={12} style={{ marginRight: 6 }} />
            监控数据
          </span>
                            }
                        >
                            <Typography.Paragraph>Content of Tab Panel 1</Typography.Paragraph>
                        </TabPane>
                        <TabPane
                            key='2'
                            title={
                                <span>
            <AiOutlineSafety size={14} style={{ marginRight: 6 }} />
            授权信息
          </span>
                            }
                        >
                            <AuthList/>
                        </TabPane>
                    </Tabs>

                    {showUpdateModal && <CallerUpdateModal callerInfo={callerInfo} onClose={() => setShowUpdateModal(false)} onSuccess={handlerUpdateProcess} />}
                    {showSecretKeyModal && <SecretKeyModal callerId={callerInfo?.id} onClose={() => setShowSecretKeyModal(false)} />}
                </Space>
            </>
        </CallerManageContext.Provider>
    );
}