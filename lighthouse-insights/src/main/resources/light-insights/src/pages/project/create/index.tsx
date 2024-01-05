import React, {useRef, useState} from 'react';
import {
    Form,
    Grid,
    Input,
    Message,
    Modal,
    Notification,
    Radio,
    Tabs,
    TreeSelect,
    Typography
} from "@arco-design/web-react";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import {translate, translateToFlatStruct} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {getTextBlenLength} from "@/utils/util";
import {requestCreate} from "@/api/project";
import {Simulate} from "react-dom/test-utils";
import {Project} from "@/types/insights-web";
import {IconCaretDown, IconCaretRight} from "@arco-design/web-react/icon";
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import UsersTransfer from "@/pages/components/transfer/user_transfer";

function ProjectCreatePanel({onClose,allDepartInfo}){
    const { Col, Row } = Grid;
    const t = useLocale(locale);
    const formRef = useRef(null);
    const [loading,setLoading] = useState<boolean>(false);
    const [showPickUpPanel,setShowPickUpPanel] = useState<boolean>(false);
    const departmentTransferRef = useRef(null);
    const userTransferRef = useRef(null);
    const [showGrantPrivileges,setShowGrantPrivileges] = useState<boolean>(true);

    function toggleShowPickupPanel(){
        setShowPickUpPanel(!showPickUpPanel);
    }

    function changeVisibleType(value){
        if(value == '0'){
            setShowGrantPrivileges(true);
        }else{
            setShowPickUpPanel(false);
            setShowGrantPrivileges(false);
        }
    }
    async function handlerSubmit(){
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        setLoading(true);
        const privateType = values.privateType;
        let departmentsPermission = [];
        let usersPermission = [];
        if(privateType == 0 && departmentTransferRef?.current){
            departmentsPermission = departmentTransferRef.current.getData();
        }
        if(privateType == 0 && userTransferRef?.current){
            usersPermission = userTransferRef.current.getData();
        }
        const project:Project = {
            title:values.title,
            departmentId:Number(values.departmentId),
            desc:values.desc,
            privateType:privateType,
            usersPermission:usersPermission,
            departmentsPermission:departmentsPermission,
        }
        requestCreate(project).then((result) => {
            if(result.code === '0'){
                Notification.info({
                    style: { width: 420 },
                    title: 'Notification',
                    content: t['projectCreate.form.submit.success'],
                })
                setTimeout(() => {
                    window.location.href = "/project/list";
                    setLoading(false);
                },3000)
            }else{
                Notification.warning({
                    style: { width: 420 },
                    title: 'Warning',
                    content: result.message || t['system.error'],
                })
                setLoading(false);
            }
        }).catch((error) => {
            console.log(error);
            Notification.error({
                style: { width: 420 },
                title: 'Error',
                content:t['system.error'],
            })
            setLoading(false);
        })
    }

    return (
        <Modal
            title= {t['projectCreate.form.title']}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            visible={true}
            confirmLoading={loading}
            onOk={handlerSubmit}
            onCancel={() => onClose()}
        >
            <div>
                <Form
                    autoComplete='off'
                    ref={formRef}
                    labelCol={{span: 4, offset: 0}}
                    initialValues={{
                        privateType:0,
                    }}
                >
                    <Form.Item label={t['projectCreate.form.label.title']} field='title' rules={[
                        { required: true, message: t['projectCreate.form.name.errMsg'] , validateTrigger : ['onSubmit']},
                        {
                        required:true,
                        validator: (v, cb) => {
                            if (getTextBlenLength(v) < 5) {
                                return cb(t['projectCreate.form.name.less.limit'])
                            }else if (getTextBlenLength(v) > 25) {
                                return cb(t['projectCreate.form.name.exceeds.limit'])
                            }
                            cb(null);
                        }
                        , validateTrigger : ['onSubmit']
                    }]}>
                        <Input autoFocus={false} />
                    </Form.Item>
                    <Form.Item label={t['projectCreate.form.label.department']} field="departmentId" rules={[{ required: true ,message: t['projectCreate.form.department.errMsg'], validateTrigger : ['onSubmit']}]}>
                        <TreeSelect
                            placeholder={"Please Select"}
                            allowClear={true}
                            treeData={translateToFlatStruct(allDepartInfo)}
                        />
                    </Form.Item>
                    <Form.Item label={t['projectCreate.form.label.desc']} field="desc" rules={[
                        {required: true ,message:t['projectCreate.form.description.errMsg'],validateTrigger : ['onSubmit']}
                        ]}>
                        <Input.TextArea placeholder='Please enter ...' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                    </Form.Item>
                    <Form.Item style={{ marginBottom: 0 }} label={t['projectCreate.form.label.privateType']} rules={[{ required: true }]} >
                        <Grid.Row gutter={8}>
                            <Grid.Col span={20}>
                                <Form.Item field={"privateType"}>
                                    <Radio.Group defaultValue={0} onChange={changeVisibleType}>
                                        <Radio value={0}>{t['projectCreate.form.label.privateType.private']}</Radio>
                                        <Radio value={1}>{t['projectCreate.form.label.privateType.public']}</Radio>
                                    </Radio.Group>
                                </Form.Item>
                            </Grid.Col>
                            <Grid.Col span={4} style={{ textAlign:"right" }}>
                                {showGrantPrivileges &&
                                <div style={{cursor:"pointer",userSelect:"none"}} onClick={toggleShowPickupPanel}>
                                    {showPickUpPanel?<IconCaretDown />:<IconCaretRight />}
                                    <Typography.Text>{t['projectCreate.form.button.grantPrivilege']}</Typography.Text>
                                </div>
                                }
                            </Grid.Col>
                        </Grid.Row>
                    </Form.Item>

                    {showPickUpPanel &&
                    <Form.Item label={t['projectCreate.form.label.crowdPickUp']}>
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
            </div>
        </Modal>
    );
}

export default ProjectCreatePanel;