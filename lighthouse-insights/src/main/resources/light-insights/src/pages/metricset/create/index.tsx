import React, {useRef, useState} from 'react';
import {Form, Grid, Input, Message, Modal, Radio, Tabs, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Col} from "antd";
import UsersTransfer from "@/pages/components/transfer/user_transfer";
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import {IconCaretDown, IconCaretRight} from "@arco-design/web-react/icon";
import {GrantPrivileges, MetricSet} from "@/types/insights-web";
import {ResultData} from "@/types/insights-common";
import {requestGrantPrivilege} from "@/api/privilege";
import {requestCreate} from "@/api/metricset";


export default function MetricSetAddPanel({onClose}) {

    const [form] = Form.useForm();
    const t = useLocale(locale);
    const FormItem = Form.Item;
    const formRef = useRef(null);
    const { Col, Row } = Grid;
    const [showPickUpPanel,setShowPickUpPanel] = useState<boolean>(false);
    const [showGrantPrivileges,setShowGrantPrivileges] = useState<boolean>(true);
    const departmentTransferRef = useRef(null);
    const [loading,setLoading] = useState<boolean>(false);
    const userTransferRef = useRef(null);

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
        console.log("values is:" + JSON.stringify(values));
        const metricSet:MetricSet = {
            title:values.title,
            description:values.description,
        }

        let metricId;
        try{
            const result:ResultData = await requestCreate(metricSet);
            if(result.code != '0'){
                Message.error(result.message || t['system.error']);
                return;
            }
            metricId = result.data.id;
        }catch (error){
            console.log(error);
            Message.error(t['system.error']);
            return;
        }
        console.log("create metric success,metricId:" + metricId);
        try{
            const grantPrivilege:GrantPrivileges = {id:metricId};
            if(departmentTransferRef.current){
                grantPrivilege.departments = departmentTransferRef.current.getData();
            }
            if(userTransferRef.current){
                grantPrivilege.users = userTransferRef.current.getData();
            }
            if(grantPrivilege.departments || grantPrivilege.users){
                const result:ResultData = await requestGrantPrivilege(grantPrivilege);
                console.log("grant result is:" + JSON.stringify(result));
                if(result.code == '0'){
                    Message.success("创建数据集成功！");
                }else{
                    Message.error(result.message || t['system.error']);
                }
            }
        }catch (error){
            console.log(error);
            Message.error(t['system.error']);
            return;
        }finally {
            setLoading(false);
        }
    }

    return (
        <Modal
            title={t['createMetricSet.modal.title']}
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
                initialValues={{private_type: 0}}
                style={{ minHeight:'300px' }}
                labelCol={{span: 4, offset: 0}}
                layout={"horizontal"}>
                <Form.Item field="title" label={t['createMetricSet.form.label.title']}
                           rules={[
                               { required: true, message: t['createMetricSet.form.title.errMsg'], validateTrigger : ['onSubmit'] },
                           ]}>
                    <Input
                        allowClear
                        placeholder={'Please Input Title'} />
                </Form.Item>

                <Form.Item field="desc" label={t['createMetricSet.form.label.description']} rules={[
                    { required: true, message: t['createMetricSet.form.description.errMsg'], validateTrigger : ['onSubmit'] },
                ]}>
                    <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}  placeholder={'Please Input Description'}/>
                </Form.Item>

                <Form.Item style={{ marginBottom: 0 }} label={t['createMetricSet.form.label.private.type']} rules={[{ required: true }]} >
                    <Grid.Row gutter={8}>
                        <Grid.Col span={20}>
                            <Form.Item field={"private_type"}>
                                <Radio.Group defaultValue={0} onChange={changeVisibleType}>
                                    <Radio value={0}>{t['createMetricSet.form.private.type.private']}</Radio>
                                    <Radio value={1}>{t['createMetricSet.form.private.type.public']}</Radio>
                                </Radio.Group>
                            </Form.Item>
                        </Grid.Col>
                        <Grid.Col span={4} style={{ textAlign:"right" }}>
                                {showGrantPrivileges &&
                                <div style={{cursor:"pointer",userSelect:"none"}} onClick={toggleShowPickupPanel}>
                                    {showPickUpPanel?<IconCaretDown />:<IconCaretRight />}
                                    <Typography.Text>{t['createMetricSet.form.label.grant.privilege']}</Typography.Text>
                                </div>
                                }
                        </Grid.Col>
                    </Grid.Row>
                </Form.Item>

                {showPickUpPanel &&
                <Form.Item label={t['createMetricSet.form.label.crowd.pickup']}>
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
