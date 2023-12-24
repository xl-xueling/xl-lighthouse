import React, {useRef, useState} from 'react';
import {Form, Input, Message, Modal, Radio, TreeSelect} from "@arco-design/web-react";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import {translate} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {getTextBlenLength} from "@/utils/util";
import {requestCreate} from "@/api/project";
import {Simulate} from "react-dom/test-utils";
import {Project} from "@/types/insights-web";

function ProjectCreatePanel({onClose,allDepartInfo}){

    const t = useLocale(locale);
    const formRef = useRef(null);
    const [loading,setLoading] = useState<boolean>(false);

    async function handlerSubmit(){
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        setLoading(true);
        const project:Project = {
            title:values.title,
            departmentId:Number(values.departmentId),
            desc:values.desc,
            privateType:values.privateType,
        }
        console.log("project is:" + JSON.stringify(project));
        requestCreate(project).then((result) => {
            if(result.code === '0'){
                Message.success(t['projectCreate.form.submit.success']);
                setTimeout(() => {
                    window.location.href = "/project/list";
                },3000)
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        }).finally(() => {
            setLoading(false);
        })
    }

    return (
        <Modal
            title= {t['projectCreate.form.title']}
            style={{ width:'750px',top:'20px' }}
            visible={true}
            confirmLoading={loading}
            onOk={handlerSubmit}
            onCancel={() => onClose()}
        >
            <div>
                <Form
                    autoComplete='off'
                    ref={formRef}
                    initialValues={{
                        privateType:0,
                    }}
                >
                    <Form.Item label='Title' field='title' rules={[
                        { required: true, message: t['projectCreate.form.name.errMsg'] , validateTrigger : ['onBlur']},
                        {
                        required:true,
                        validator: (v, cb) => {
                            if (getTextBlenLength(v) < 5) {
                                return cb(t['projectCreate.form.name.less.limit'])
                            }else if (getTextBlenLength(v) > 26) {
                                return cb(t['projectCreate.form.name.exceeds.limit'])
                            }
                            cb(null);
                        }
                        , validateTrigger : ['onBlur']
                    }]}>
                        <Input placeholder='Please enter project name' autoFocus={false} />
                    </Form.Item>
                    <Form.Item label='Department' field="departmentId" rules={[{ required: true ,message: t['projectCreate.form.department.errMsg'], validateTrigger : ['onBlur']}]}>
                        <TreeSelect
                            placeholder={"Please Select"}
                            allowClear={true}
                            treeData={translate(allDepartInfo)}
                        />
                    </Form.Item>
                    <Form.Item label={'Description'} field="desc" rules={[
                        {required: true ,message:t['projectCreate.form.description.errMsg'],validateTrigger : ['onBlur']}
                        ]}>
                        <Input.TextArea placeholder='Please enter ...' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                    </Form.Item>
                    <Form.Item label={'PrivateType'} field="privateType">
                        <Radio.Group defaultValue={0}>
                            <Radio value={0}>Private</Radio>
                            <Radio value={1}>Public</Radio>
                        </Radio.Group>
                    </Form.Item>
                    {/*<Form.Item label={'Admins'} field="admins" rules={[{ required: true,validateTrigger : ['onBlur']}]}>*/}
                    {/*    <UserTermQuery formRef={formRef}/>*/}
                    {/*</Form.Item>*/}
                </Form>
            </div>
        </Modal>
    );
}

export default ProjectCreatePanel;