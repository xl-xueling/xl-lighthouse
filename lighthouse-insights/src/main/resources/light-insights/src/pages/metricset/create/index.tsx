import React, {useRef, useState} from 'react';
import {Form, Grid, Input, Message, Modal, Radio, Tabs, Typography} from "@arco-design/web-react";
import styles from "@/pages/project/manage/style/index.module.less";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/manage/locale";
import {Col} from "antd";
import UsersTransfer from "@/pages/components/transfer/user_transfer";
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import {Project} from "@/types/insights-web";
import {requestCreate} from "@/api/project";
import {
    IconCaretDown,
    IconDoubleDown,
    IconDown,
    IconDownCircle,
    IconSafe,
    IconStamp
} from "@arco-design/web-react/icon";


export default function MetricSetAddPanel({onClose}) {

    const [confirmLoading, setConfirmLoading] = useState(false);
    const [form] = Form.useForm();
    const t = useLocale(locale);
    const FormItem = Form.Item;
    const { Col, Row } = Grid;
    const [showPickUpPanel,setShowPickUpPanel] = useState<boolean>(false);
    const [showGrantPrivileges,setShowGrantPrivileges] = useState<boolean>(false);
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

    const formRef = useRef(null);

    async function handlerSubmit(){
        await formRef.current.validate();
        const departments = departmentTransferRef.current.getData();
        const users = userTransferRef.current.getData();
        const values = formRef.current.getFieldsValue();
        setLoading(true);
        // const project: = {
        //     name:values.name,
        //     departmentId:Number(values.departmentId),
        //     adminIds:values.admins,
        //     desc:values.desc,
        //     isPrivate:values.isPrivate,
        // }
        // requestCreate(project).then((result) => {
        //     if(result.code === '0'){
        //         Message.success(t['projectCreate.form.submit.success']);
        //         setTimeout(() => {
        //             window.location.href = "/project/list";
        //         },3000)
        //     }else{
        //         Message.error(result.message || t['system.error']);
        //     }
        // }).catch((error) => {
        //     console.log(error);
        //     Message.error(t['system.error'])
        // }).finally(() => {
        //     setLoading(false);
        // })
    }

    return (
        <Modal
            title='创建指标集'
            visible={true}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            confirmLoading={confirmLoading}
            onCancel={onClose}
            onOk={handlerSubmit}
        >
            <Form
                form={form}
                colon={true}
                ref={formRef}
                style={{ minHeight:'300px' }}
                labelCol={{span: 4, offset: 0}}
                className={styles['search-form']}
                layout={"horizontal"}>
                <Form.Item field="name" label={"Name"}
                           rules={[
                               { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onSubmit'] },
                               { required: true, match: new RegExp(/^[a-z0-9_]{5,20}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onSubmit']},
                           ]}>
                    <Input
                        allowClear
                        placeholder={'Please Input Token'} />
                </Form.Item>
                <Form.Item field="desc" label={"Description"} rules={[
                    { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onSubmit'] },
                    { required: true, match: new RegExp(/^[^￥{}【】#@=^&|《》]{0,200}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onSubmit']},
                ]}>
                    <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
                </Form.Item>
                <Form.Item field="isPrivate" label={'IsPrivate'}>
                    <Row>
                        <Col span={20}>
                            <Radio.Group defaultValue={"0"} onChange={changeVisibleType}>
                                <Radio value={"0"}>私有</Radio>
                                <Radio value={"1"}>公有</Radio>
                            </Radio.Group>
                        </Col>
                        {showGrantPrivileges && <Col span={4} style={{ textAlign:"right"}}>
                            <div style={{cursor:"pointer"}} onClick={toggleShowPickupPanel}><Typography.Text>初始权限</Typography.Text><IconCaretDown /></div>
                        </Col>}
                    </Row>
                </Form.Item>

                {showPickUpPanel &&
                <Form.Item label={'人员圈选'}>
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
