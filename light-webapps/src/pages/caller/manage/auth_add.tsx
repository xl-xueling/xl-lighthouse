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
const TabPane = Tabs.TabPane;

export default function AuthAdd({onClose}){

    const {callerInfo,PRO_ViewBindTab} = useContext(CallerManageContext);

    useEffect(() => {
        console.log("PRO_ViewBindTab is:" + PRO_ViewBindTab);
    },[PRO_ViewBindTab])

    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [activeTab, setActiveTab] = useState(0);
    const t = useLocale(locale);
    const formRefs = useRef([]);

    const handleSubmit = () => {
        const currentForm = formRefs.current[activeTab];
        if (currentForm) {
            const values = currentForm.getFieldsValue();
            if(activeTab === 0){
                const applyParam = {
                    orderType:OrderTypeEnum.CALLER_PROJECT_ACCESS,
                    userId:userInfo?.id,
                    reason:values?.reason,
                    extendConfig:{
                        projectId:values.project,
                        callerId:callerInfo?.id,
                        expired:values.expired,
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
            }else if(activeTab === 1){
                const applyParam = {
                    orderType:OrderTypeEnum.CALLER_STAT_ACCESS,
                    userId:userInfo?.id,
                    reason:values?.reason,
                    extendConfig:{
                        statId:values.stat,
                        callerId:callerInfo?.id,
                        expired:values.expired,
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
            }else if(activeTab === 2){
                const applyParam = {
                    orderType:OrderTypeEnum.CALLER_VIEW_ACCESS,
                    userId:userInfo?.id,
                    reason:values?.reason,
                    extendConfig:{
                        viewId:values.view,
                        callerId:callerInfo?.id,
                        expired:values.expired,
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
            title='新增授权'
            autoFocus={false}
            focusLock={true}
        >
            <Tabs defaultActiveTab='0' tabPosition={'right'} onChange={handleTabChange}>
                <TabPane
                    key='0'
                    title={
                        <span>
                            {getIcon('project')}
                        </span>}>
                    <Form ref={(ref) => (formRefs.current[0] = ref)} initialValues={{expired:2592000}}>
                        <ProjectApply callerInfo={callerInfo}/>
                    </Form>
                </TabPane>
                <TabPane
                    key='1'
                    title={
                        <span>
                            {getIcon('stat')}
                        </span>}>
                    <Form ref={(ref) => (formRefs.current[1] = ref)} initialValues={{expired:2592000}}>
                        <StatApply callerInfo={callerInfo}/>
                    </Form>
                </TabPane>
                {
                    PRO_ViewBindTab &&
                    <TabPane
                        key='2'
                        title={
                            <span>
                            {getIcon('view')}
                        </span>
                        }>
                        <Form ref={(ref) => (formRefs.current[2] = ref)} initialValues={{expired:2592000}}>
                            {PRO_ViewBindTab(callerInfo)}
                        </Form>
                    </TabPane>
                }
            </Tabs>
        </Modal>
    );
}