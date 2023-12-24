import React, {useEffect, useRef, useState} from 'react';
import {Form, Input, Message, Modal, Radio, Skeleton, Spin, TreeSelect} from "@arco-design/web-react";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import {translate} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {requestCreate, requestQueryById, requestUpdateById} from "@/api/project";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {getTextBlenLength} from "@/utils/util";
import {Project, User} from "@/types/insights-web";

export default function ProjectUpdatePanel({projectInfo,allDepartInfo,onClose}){

    const t = useLocale(locale);
    const [form] = useForm();
    const formRef = useRef(null);
    const [loading, setLoading] = useState(true);
    const [admins,setAdmins] = useState<Array<number>>([]);

    useEffect(() => {
        setLoading(true);
        form.setFieldsValue(projectInfo);
        form.setFieldValue("departmentId",projectInfo.departmentId.toString());
        setAdmins(projectInfo.adminIds);
        setLoading(false);
    },[projectInfo])


    async function handlerSubmit(){
        console.log("update submit!")
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        setLoading(true);
        const project:Project = {
            id:projectInfo.id,
            title:values.title,
            departmentId:Number(values.departmentId),
            desc:values.desc,
            privateType:values.privateType,
        }
        console.log("project is:" + JSON.stringify(project));
        requestUpdateById(project).then((result) => {
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
            title= {t['projectUpdate.form.title']}
            visible={true}
            style={{ width:'750px',top:'20px' }}
            onOk={handlerSubmit}
            onCancel={onClose}>

            <Skeleton
                style={{ marginTop:15,display:loading ? 'block' : 'none' }}
                text={{
                    rows:3,
                    width: ['100%'],
                }}
                animation
            />
            <Form
                style={{ display:loading ? 'none' : 'block' }}
                form={form}
                ref={formRef}
                autoComplete='off'
                scrollToFirstError>
                    <Form.Item label='Title' field='title' rules={[
                        { required: true, message: t['projectUpdate.form.name.errMsg'] , validateTrigger : ['onBlur']},
                        {
                            required:true,
                            validator: (v, cb) => {
                                if (getTextBlenLength(v) < 5) {
                                    return cb(t['projectUpdate.form.name.less.limit'])
                                }else if (getTextBlenLength(v) > 26) {
                                    return cb(t['projectUpdate.form.name.exceeds.limit'])
                                }
                                cb(null);
                            }
                            , validateTrigger : ['onBlur']
                        }]}>
                        <Input placeholder='Please enter project name' autoFocus={false} />
                    </Form.Item>
                    <Form.Item label='Department' field="departmentId" rules={[{ required: true ,message: t['projectUpdate.form.department.errMsg'], validateTrigger : ['onBlur']}]}>
                        <TreeSelect
                            placeholder={"Please Select"}
                            allowClear={true}
                            treeData={translate(allDepartInfo)}
                        />
                    </Form.Item>
                    <Form.Item label={'Description'} field="desc" rules={[
                        {required: true ,message:t['projectUpdate.form.description.errMsg'],validateTrigger : ['onBlur']}
                    ]}>
                        <Input.TextArea placeholder='Please enter description' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                    </Form.Item>
                    <Form.Item label={'PrivateType'} field="privateType">
                        <Radio.Group defaultValue={0}>
                            <Radio value={0}>Private</Radio>
                            <Radio value={1}>Public</Radio>
                        </Radio.Group>
                    </Form.Item>

                </Form>
        </Modal>
    );
}
