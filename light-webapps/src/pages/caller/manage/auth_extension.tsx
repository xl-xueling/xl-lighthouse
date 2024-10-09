import React, {useContext, useRef} from 'react';
import {Form, Input, Modal, Notification, Select} from "@arco-design/web-react";
import {AuthRecord} from "@/types/caller";
import {LabelValue, OrderTypeEnum, ResourceTypeEnum} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/caller/manage/locale";
import {requestCreateApply} from "@/api/order";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {CallerManageContext} from "@/pages/common/context";

const FormItem = Form.Item;
const Option = Select.Option;
const TextArea = Input.TextArea;

export interface Props {
    authRecord?:AuthRecord;
    onClose?: () => void;
}

const AuthExtension:React.FC<Props> = ({
                                           authRecord,
                                           onClose = null}) => {

    const {callerInfo} = useContext(CallerManageContext);
    const t = useLocale(locale);
    const periodOptions:Array<LabelValue> = [{label:t['callerManage.authAdd.extension.3month'],value:7776000}
        ,{label:t['callerManage.authAdd.extension.6month'],value:15552000},{label:t['callerManage.authAdd.extension.1year'],value:31104000}
        ,{label:t['callerManage.authAdd.extension.2year'],value:62208000}]
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const formRef = useRef(null);

    const handleSubmit = async () => {
        try{
            await formRef.current.validate();
        }catch (error){
            console.log(error)
            return;
        }
        const values = formRef.current.getFieldsValue();
        const resourceType = authRecord?.resourceType;
        if(resourceType == ResourceTypeEnum.Project){
            const applyParam = {
                orderType:OrderTypeEnum.CALLER_PROJECT_ACCESS_EXTENSION,
                userId:userInfo?.id,
                reason:values?.reason,
                extendConfig:{
                    projectId:values.project,
                    callerId:callerInfo?.id,
                    extension:values.extension,
                    permissionId:authRecord.id,
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
        }else if(resourceType == ResourceTypeEnum.Stat){
            const applyParam = {
                orderType:OrderTypeEnum.CALLER_STAT_ACCESS_EXTENSION,
                userId:userInfo?.id,
                reason:values?.reason,
                extendConfig:{
                    statId:values.stat,
                    callerId:callerInfo?.id,
                    extension:values.extension,
                    permissionId:authRecord.id,
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
        }else if(resourceType == ResourceTypeEnum.View){
            const applyParam = {
                orderType:OrderTypeEnum.CALLER_VIEW_ACCESS_EXTENSION,
                userId:userInfo?.id,
                reason:values?.reason,
                extendConfig:{
                    viewId:values.view,
                    callerId:callerInfo?.id,
                    extension:values.extension,
                    permissionId:authRecord.id,
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

    const getFormItems = () => {
        if(authRecord.resourceType == ResourceTypeEnum.Project){
            const project = authRecord?.extend;
            return <Form ref={formRef} initialValues={{extension:7776000,project:authRecord.extend.id}}>
                <Form.Item field='project' label={t['callerManage.extension.label.project']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select disabled={true}>
                        <Option key={authRecord.extend.id} value={authRecord.extend.id}>
                            {project?.title + ' [' +project?.id+ ']'}
                        </Option>
                    </Select>
                </Form.Item>
                <Form.Item field={'extension'} label={t['callerManage.extension.label.extension']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select
                        placeholder='Select period' defaultValue={periodOptions[0].value}>
                        {periodOptions.map((option, index) => (
                            <Option key={option.value} value={option.value}>
                                {option.label}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item field={'reason'} label={t['callerManage.extension.label.reason']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
                </Form.Item>
            </Form>
        }else if(authRecord.resourceType == ResourceTypeEnum.Stat){
            const stat = authRecord?.extend;
            return <Form ref={formRef} initialValues={{extension:7776000,stat:authRecord.extend.id}}>
                <Form.Item field='stat' label={t['callerManage.extension.label.stat']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select disabled={true}>
                        <Option key={authRecord.extend.id} value={authRecord.extend.id}>
                            {stat?.projectTitle + ' > ' + stat?.token + ' > ' + stat?.title + " ["+stat?.id+"]"}
                        </Option>
                    </Select>
                </Form.Item>
                <Form.Item field={'extension'} label={t['callerManage.extension.label.extension']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select
                        placeholder='Select period' defaultValue={periodOptions[0].value}>
                        {periodOptions.map((option, index) => (
                            <Option key={option.value} value={option.value}>
                                {option.label}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item field={'reason'} label={t['callerManage.extension.label.reason']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
                </Form.Item>
            </Form>
        }else if(authRecord.resourceType == ResourceTypeEnum.View){
            const view = authRecord?.extend;
            return <Form ref={formRef} initialValues={{extension:7776000,view:authRecord.extend.id}}>
                <Form.Item field='view' label={t['callerManage.extension.label.view']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select disabled={true}>
                        <Option key={view?.id} value={view?.id}>
                            {view?.title + ' [' +view?.id+ ']'}
                        </Option>
                    </Select>
                </Form.Item>
                <Form.Item field={'extension'} label={t['callerManage.extension.label.extension']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <Select
                        placeholder='Select period' defaultValue={periodOptions[0].value}>
                        {periodOptions.map((option, index) => (
                            <Option key={option.value} value={option.value}>
                                {option.label}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item field={'reason'} label={t['callerManage.extension.label.reason']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                    <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
                </Form.Item>
            </Form>
        }
    }

    return (
        <Modal
            visible={true}
            alignCenter={false}
            style={{ width:'1000px',maxWidth:'80%',verticalAlign:'top', top: '150px' }}
            onCancel={onClose}
            onOk={handleSubmit}
            title={t['callerManage.extension.title']}
            autoFocus={false}
            focusLock={true}
        >
            {getFormItems()}
        </Modal>
    );
}

export default AuthExtension;