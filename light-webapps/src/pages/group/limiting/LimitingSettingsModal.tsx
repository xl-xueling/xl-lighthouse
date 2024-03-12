import React, {useEffect, useRef, useState} from 'react';
import {
    Button, Card, Collapse,
    Form,
    Grid,
    Input, InputNumber,
    Message,
    Modal,
    Notification, PaginationProps,
    Radio, Select, Space, Table, TableColumnProps,
    Tabs,
    TreeSelect,
    Typography
} from "@arco-design/web-react";
import {Record, User} from "@/types/insights-web";
import {requestList} from "@/api/record";
import {RecordTypeEnum, ResourceTypeEnum, ResultData} from "@/types/insights-common";
import {GlobalErrorCodes} from "@/utils/constants";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import "./style/index.module.less"
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import {LimitedRecord, translateRecord} from "@/pages/record/record";
import {LimitedRecordModal} from "@/pages/record/limited_records";
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import UsersTransfer from "@/pages/components/transfer/user_transfer";


export function LimitingSettingsModal({groupInfo,onClose}){

    const t = useLocale(locale);
    const CollapseItem = Collapse.Item;
    const FormItem = Form.Item;

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
                    <CollapseItem style={{borderLeft:"none",borderRight:"none",textAlign:'center'}} header={<span>限流阈值修改</span>}
                                  name='1'>
                        <Form
                            autoComplete='off'
                            labelCol={{ span: 8 }}
                            wrapperCol={{ span: 16 }}
                            style={{ width: '75%',textAlign:'center',marginTop:'25px'}}
                            initialValues={{ name: 'admin',currentValue:'2300' }}
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
                                    Submit
                                </Button>
                                <Button
                                    style={{ marginRight: 24 }}
                                >
                                    Reset
                                </Button>
                            </FormItem>
                        </Form>
                    </CollapseItem>
                </Collapse>
            </Space>
        </Modal>
    )
}