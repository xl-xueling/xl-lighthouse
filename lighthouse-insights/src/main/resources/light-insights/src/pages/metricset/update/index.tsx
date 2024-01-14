import React, {useRef, useState} from 'react';
import {Form, Grid, Input, Message, Modal, Notification, Radio, Tabs, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Col} from "antd";
import UsersTransfer from "@/pages/components/transfer/user_transfer";
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import {IconCaretDown, IconCaretRight} from "@arco-design/web-react/icon";
import {GrantPrivileges, MetricSet} from "@/types/insights-web";
import {ResultData} from "@/types/insights-common";
import {requestGrantPrivilege} from "@/api/privilege";
import {requestCreate, requestUpdate} from "@/api/metricset";


export default function MetricSetUpdateModal({metricInfo,onClose,onSuccess}) {

    const [form] = Form.useForm();
    const t = useLocale(locale);
    const FormItem = Form.Item;
    const formRef = useRef(null);
    const { Col, Row } = Grid;
    const [showPickUpPanel,setShowPickUpPanel] = useState<boolean>(false);
    const [showGrantPrivileges,setShowGrantPrivileges] = useState<boolean>(true);
    const departmentTransferRef = useRef(null);
    const userTransferRef = useRef(null);
    const [loading,setLoading] = useState<boolean>(false);

    function changeVisibleType(value){
        if(value == '0'){
            setShowGrantPrivileges(true);
        }else{
            setShowPickUpPanel(false);
            setShowGrantPrivileges(false);
        }
    }

    function toggleShowPickupPanel(){
        setShowPickUpPanel(!showPickUpPanel);
    }

    async function handlerSubmit(){
        await formRef.current.validate().catch()
        setLoading(true);
        const values = formRef.current.getFieldsValue();
        const updateParams:MetricSet = {
            id:metricInfo?.id,
            title:values.title,
            desc:values.desc,
            privateType:values.privateType,
        }
        await requestUpdate(updateParams).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['updateMetricSet.form.submit.success']});
                setLoading(false);
                onClose();
                onSuccess();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                setLoading(false);
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    return (
        <Modal
            title={t['updateMetricSet.modal.title']}
            visible={true}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            confirmLoading={loading}
            onCancel={onClose}
            onOk={handlerSubmit}
        >
            <Form
                form={form}
                colon={true}
                ref={formRef}
                autoComplete={"off"}
                initialValues={{
                    title:metricInfo?.title,
                    privateType: metricInfo?.privateType,
                    desc:metricInfo?.desc,
                }}
                style={{ minHeight:'300px' }}
                labelCol={{span: 4, offset: 0}}
                layout={"horizontal"}>
                <Form.Item field="title" label={t['updateMetricSet.form.label.title']}
                           rules={[
                               { required: true, message: t['updateMetricSet.form.title.errMsg'], validateTrigger : ['onSubmit'] },
                           ]}>
                    <Input
                        allowClear
                        placeholder={'Please Input Title'} />
                </Form.Item>

                <Form.Item field="desc" label={t['updateMetricSet.form.label.description']} rules={[
                    { required: true, message: t['updateMetricSet.form.description.errMsg'], validateTrigger : ['onSubmit'] },
                ]}>
                    <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}  placeholder={'Please Input Description'}/>
                </Form.Item>

                <Form.Item style={{ marginBottom: 0 }} label={t['updateMetricSet.form.label.private.type']} rules={[{ required: true }]} >
                    <Grid.Row gutter={8}>
                        <Grid.Col span={20}>
                            <Form.Item field={"privateType"}>
                                <Radio.Group defaultValue={0} onChange={changeVisibleType}>
                                    <Radio value={0}>{t['updateMetricSet.form.private.type.private']}</Radio>
                                    <Radio value={1}>{t['updateMetricSet.form.private.type.public']}</Radio>
                                </Radio.Group>
                            </Form.Item>
                        </Grid.Col>
                        <Grid.Col span={4} style={{ textAlign:"right" }}>
                            {showGrantPrivileges &&
                            <div style={{cursor:"pointer",userSelect:"none"}} onClick={toggleShowPickupPanel}>
                                {showPickUpPanel?<IconCaretDown />:<IconCaretRight />}
                                <Typography.Text>{t['updateMetricSet.form.label.grant.privilege']}</Typography.Text>
                            </div>
                            }
                        </Grid.Col>
                    </Grid.Row>
                </Form.Item>

                {showPickUpPanel &&
                <Form.Item label={t['updateMetricSet.form.label.crowd.pickup']}>
                    <Row>
                        <Col
                            span={24}
                            style={{ marginBottom: 12 }}
                        >
                            <Tabs key='card' tabPosition={"right"}>
                                <Tabs.TabPane key='1' title='Tab1'>
                                    <DepartmentsTransfer ref={departmentTransferRef}/>
                                </Tabs.TabPane>
                                <Tabs.TabPane key='2' title='Tab2'>
                                    <UsersTransfer ref={userTransferRef}/>
                                </Tabs.TabPane>
                            </Tabs>
                        </Col>
                    </Row>
                </Form.Item>
                }
            </Form>
        </Modal>
    );
}
