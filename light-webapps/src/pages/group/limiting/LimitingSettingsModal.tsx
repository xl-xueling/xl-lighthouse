import React, {useEffect, useRef, useState} from 'react';
import {
    Button, Card, Collapse,
    Form,
    Input, InputNumber, Message,
    Modal, Notification, Popconfirm, Select, Space,
} from "@arco-design/web-react";
import {OrderTypeEnum, RecordTypeEnum, ResourceTypeEnum} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import "./style/index.module.less"
import {LimitedRecordModal} from "@/pages/record/limited_records";
import {MetricSet} from "@/types/insights-web";
import {requestUpdate, requestUpdateLimitingSettings} from "@/api/group";
import {updateStoreStaredMetricInfo} from "@/index";
import {requestCreateApply} from "@/api/order";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {getRandomString} from "@/utils/util";

export function LimitingSettingsModal({groupInfo,onClose}){

    const t = useLocale(locale);
    const CollapseItem = Collapse.Item;
    const FormItem = Form.Item;
    const [form] = Form.useForm();

    const formRef = useRef(null);
    const [loading,setLoading] = useState(false);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);

    const changeSelect = (v) => {
        if(v == 1){
            formRef.current.setFieldsValue({
                currentValue: groupInfo?.extendConfig?.limitingConfig.GROUP_MESSAGE_SIZE_LIMITING
            });
        }else{
            formRef.current.setFieldsValue({
                currentValue: groupInfo?.extendConfig?.limitingConfig.STAT_RESULT_SIZE_LIMITING
            });
        }
    }

    async function submit(){
        setLoading(true);
        const values = formRef.current.getFieldsValue();
        const applyParam = {
            orderType:OrderTypeEnum.LIMITING_SETTINGS,
            userId:userInfo?.id,
            reason:values?.reason,
            extendConfig:{
                groupId:groupInfo?.id,
                strategy:values.strategy == 1?'GROUP_MESSAGE_SIZE_LIMITING':'STAT_RESULT_SIZE_LIMITING',
                currentValue:values.currentValue,
                updateValue:values.updateValue,
            }
        }
        await requestCreateApply(applyParam).then((response) => {
            const {code, data ,message} = response;
            console.log("response is:" + JSON.stringify(response));
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectApply.form.submit.success']});
                onClose();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        }).finally(() => {
            setLoading(false);
        })
    }

    const handleSubmit = () => {
        formRef.current.validate().then(() => {
            submit().then();
        }).catch((error) =>{
            console.log(error);
        })
    };


    return (
        <Modal
            title= {t['limitingConfig.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '150px' }}
            visible={true}
            onCancel={onClose}
            footer={null}>

            <Space direction={"vertical"} style={{width:'100%',border:'1px solid var(--color-border)'}}>
                <LimitedRecordModal resourceId={groupInfo?.id} recordTypes={[RecordTypeEnum.GROUP_MESSAGE_LIMITED]} resourceType={ResourceTypeEnum.Group} />
                <Collapse style={{marginTop:'10px',borderLeft:"none",borderRight:"none"}}>
                    <CollapseItem style={{borderLeft:"none",borderRight:"none"}} header={<span>{t['limitingConfig.collapse.title']}</span>}
                                  name='1'>
                        <Form
                            form={form}
                            ref={formRef}
                            autoComplete='off'
                            colon={'：'}
                            labelCol={{ span: 8 }}
                            wrapperCol={{ span: 16 }}
                            style={{ width: '80%',marginTop:'25px'}}
                            initialValues={{ strategy: '1',currentValue:groupInfo?.extendConfig?.limitingConfig.GROUP_MESSAGE_SIZE_LIMITING }}
                            onValuesChange={(v, vs) => {
                                console.log(v, vs);
                            }}
                            onSubmit={(v) => {
                                return new Promise((resolve) => {
                                    setTimeout(() => {
                                        resolve(1)
                                    }, 3000)
                                })
                            }}
                        >
                            <FormItem label={t['limitingConfig.form.label.strategy']} field='strategy' rules={[{ required: true }]}>
                                <Select
                                    placeholder='Please select'
                                    onChange={
                                        changeSelect
                                    }
                                >
                                    <Select.Option key={1} value={'1'}>
                                        {t['basic.limitingStrategy.messageSizeLimiting']}
                                    </Select.Option>
                                    <Select.Option key={2} value={'2'}>
                                        {t['basic.limitingStrategy.resultSizeLimiting']}
                                    </Select.Option>
                                </Select>
                            </FormItem>
                            <FormItem
                                label={t['limitingConfig.form.label.currentValue']}
                                field='currentValue'
                                rules={[
                                    { required: true, type: 'number', validateTrigger : ['onSubmit'] },
                                ]}
                            >
                                <Input disabled={true} />
                            </FormItem>
                            <FormItem
                                label={t['limitingConfig.form.label.updateValue']}
                                field='updateValue'
                                rules={[
                                    { required: true, type: 'number', validateTrigger : ['onSubmit'] },
                                ]}
                            >
                                <Input placeholder='Please Enter...' />
                            </FormItem>
                            <FormItem
                                label={t['limitingConfig.form.label.reason']}
                                field='reason'
                                rules={[
                                    { required: true, validateTrigger : ['onSubmit'] },
                                ]}
                            >
                                <Input.TextArea maxLength={200} rows={2}  showWordLimit={true}/>
                            </FormItem>
                            <FormItem wrapperCol={{ offset: 13 }}>
                                <Popconfirm
                                    focusLock
                                    position={"tr"}
                                    title='Confirm'
                                    content={t['limitingConfig.form.submit.confirm']}
                                    onOk={() => handleSubmit()}
                                >
                                    <Button
                                        type='primary'
                                        style={{ marginRight: 24 }}
                                    >
                                        {t['basic.form.button.submit']}
                                    </Button>
                                </Popconfirm>
                                <Button
                                    onClick={onClose}
                                    style={{ marginRight: 24 }}
                                >
                                    {t['basic.form.button.cancel']}
                                </Button>
                            </FormItem>
                        </Form>
                    </CollapseItem>
                </Collapse>
            </Space>
        </Modal>
    )
}