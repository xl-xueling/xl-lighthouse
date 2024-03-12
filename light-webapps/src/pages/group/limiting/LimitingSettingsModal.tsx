import React, {useEffect, useRef, useState} from 'react';
import {
    Button, Card, Collapse,
    Form,
    Input, InputNumber,
    Modal, Select, Space,
} from "@arco-design/web-react";
import {RecordTypeEnum, ResourceTypeEnum} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import "./style/index.module.less"
import {LimitedRecordModal} from "@/pages/record/limited_records";

export function LimitingSettingsModal({groupInfo,onClose}){

    const t = useLocale(locale);
    const CollapseItem = Collapse.Item;
    const FormItem = Form.Item;
    const formRef = useRef(null);

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
                    <CollapseItem style={{borderLeft:"none",borderRight:"none",textAlign:'center'}} header={<span>{t['limitingConfig.collapse.title']}</span>}
                                  name='1'>
                        <Form
                            ref={formRef}
                            autoComplete='off'
                            colon={'：'}
                            labelCol={{ span: 8 }}
                            wrapperCol={{ span: 16 }}
                            style={{ width: '75%',textAlign:'center',marginTop:'25px'}}
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
                                rules={[{ required: true, type: 'number', min: 0, max: 99 }]}
                            >
                                <Input disabled={true} />
                            </FormItem>
                            <FormItem
                                label={t['limitingConfig.form.label.updateValue']}
                                field='updateValue'
                                rules={[{ required: true, type: 'number', min: 0, max: 99 }]}
                            >
                                <InputNumber placeholder='Please Enter...' />
                            </FormItem>
                            <FormItem wrapperCol={{ offset: 4 }}>
                                <Button
                                    type='primary'
                                    htmlType='submit'
                                    style={{ marginRight: 24 }}
                                >
                                    {t['basic.form.button.submit']}
                                </Button>
                                <Button
                                    style={{ marginRight: 24 }}
                                >
                                    {t['basic.form.button.reset']}
                                </Button>
                            </FormItem>
                        </Form>
                    </CollapseItem>
                </Collapse>
            </Space>
        </Modal>
    )
}