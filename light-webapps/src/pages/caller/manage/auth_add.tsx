import React, {useContext, useEffect, useRef, useState} from 'react';
import {Form, Modal, Notification, Tabs, Typography} from "@arco-design/web-react";
import {IconCalendar, IconClockCircle, IconUser} from "@arco-design/web-react/icon";
import ProjectApply from "@/pages/caller/manage/apply/project_apply";
import {CallerManageContext} from "@/pages/common/context";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import locale from "./locale/index";
import StatApply from "@/pages/caller/manage/apply/stat_apply";
import useLocale from "@/utils/useLocale";
import {getIcon} from "@/pages/common/desc/base";
import {requestCreateApply} from "@/api/order";
import {OrderTypeEnum} from "@/types/insights-common";
import {TbBrandVisualStudio} from "react-icons/tb";
import {PiDiamondsFour} from "react-icons/pi";
const TabPane = Tabs.TabPane;

export default function AuthAdd({onClose}){

    const {callerInfo,PRO_ViewBindTab} = useContext(CallerManageContext);

    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [activeTab, setActiveTab] = useState("0");
    const t = useLocale(locale);
    const formRefs = useRef([]);

    const handleSubmit = async () => {
        const currentForm = formRefs.current[activeTab];
        if (currentForm) {
            try{
                await currentForm.validate();
            }catch (error){
                console.log(error)
                return;
            }
            const values = currentForm.getFieldsValue();
            if(activeTab == '0'){
                const applyParam = {
                    orderType:OrderTypeEnum.CALLER_PROJECT_ACCESS,
                    userId:userInfo?.id,
                    reason:values?.reason,
                    extendConfig:{
                        projectId:values.project,
                        callerId:callerInfo?.id,
                        extension:values.extension,
                    }
                }
                requestCreateApply(applyParam).then((response) => {
                    const {code, data ,message} = response;
                    if(code == '0'){
                        Notification.info({style: { width: 420 }, title: 'Notification', content: t['callerAuthApply.form.submit.success']});
                        onClose();
                    }else{
                        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                    }
                }).catch((error) => {
                    console.log(error);
                    Notification.error({
                        style: { width: 420 },
                        title: 'Error',
                        content:t['system.error'],
                    })
                })
            }else if(activeTab == '1'){
                const applyParam = {
                    orderType:OrderTypeEnum.CALLER_STAT_ACCESS,
                    userId:userInfo?.id,
                    reason:values?.reason,
                    extendConfig:{
                        statId:values.stat,
                        callerId:callerInfo?.id,
                        extension:values.extension,
                    }
                }
                requestCreateApply(applyParam).then((response) => {
                    const {code, data ,message} = response;
                    if(code == '0'){
                        Notification.info({style: { width: 420 }, title: 'Notification', content: t['callerAuthApply.form.submit.success']});
                        onClose();
                    }else{
                        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                    }
                }).catch((error) => {
                    console.log(error);
                    Notification.error({
                        style: { width: 420 },
                        title: 'Error',
                        content:t['system.error'],
                    })
                })
            }else if(activeTab == '2'){
                const applyParam = {
                    orderType:OrderTypeEnum.CALLER_VIEW_ACCESS,
                    userId:userInfo?.id,
                    reason:values?.reason,
                    extendConfig:{
                        viewId:values.view,
                        callerId:callerInfo?.id,
                        extension:values.extension,
                    }
                }
                requestCreateApply(applyParam).then((response) => {
                    const {code, data ,message} = response;
                    if(code == '0'){
                        Notification.info({style: { width: 420 }, title: 'Notification', content: t['callerAuthApply.form.submit.success']});
                        onClose();
                    }else{
                        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                    }
                }).catch((error) => {
                    console.log(error);
                    Notification.error({
                        style: { width: 420 },
                        title: 'Error',
                        content:t['system.error'],
                    })
                })
            }
        }
    };

    const handleTabChange = (index) => {
        setActiveTab(index);
    };

    return (
        <Modal
            visible={true}
            alignCenter={false}
            style={{ width:'1200px',maxWidth:'80%',verticalAlign:'top', top: '150px' }}
            onCancel={onClose}
            onOk={handleSubmit}
            title={t['callerManage.authAdd.title']}
            autoFocus={false}
            focusLock={true}
        >
            <Tabs defaultActiveTab="1" activeTab={activeTab} tabPosition={'right'} onChange={handleTabChange}>
                <TabPane
                    key="0"
                    title={
                        <span>
                            <PiDiamondsFour style={{marginBottom:'-2px'}}/>
                        </span>}>
                    <Form ref={(ref) => (formRefs.current["0"] = ref)} initialValues={{extension:7776000}}>
                        <ProjectApply callerInfo={callerInfo}/>
                    </Form>
                </TabPane>
                <TabPane
                    key="1"
                    title={
                        <span>
                            {getIcon('stat')}
                        </span>}>
                    <Form ref={(ref) => (formRefs.current["1"] = ref)} initialValues={{extension:7776000}}>
                        <StatApply callerInfo={callerInfo}/>
                    </Form>
                </TabPane>
                {
                    PRO_ViewBindTab &&
                    <TabPane
                        key="2"
                        title={
                            <span>
                                <TbBrandVisualStudio size={16} style={{marginBottom:'-2px'}}/>
                            </span>
                        }>
                        <Form ref={(ref) => (formRefs.current["2"] = ref)} initialValues={{extension:7776000}}>
                            {PRO_ViewBindTab(callerInfo)}
                        </Form>
                    </TabPane>
                }
            </Tabs>
        </Modal>
    );
}