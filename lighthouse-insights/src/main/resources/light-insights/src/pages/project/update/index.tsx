import React, {useEffect, useRef, useState} from 'react';
import {Form, Input, Modal, Radio, Skeleton, Spin, TreeSelect} from "@arco-design/web-react";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import {translate} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {requestQueryById, requestUpdateById} from "@/api/project";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {getTextBlenLength} from "@/utils/util";
import {Project, User} from "@/types/insights-web";

export default function ProjectUpdatePanel({updateId,allDepartInfo,onClose}){

    const t = useLocale(locale);
    const [form] = useForm();
    const formRef = useRef(null);
    const [loading, setLoading] = useState(true);
    const [admins,setAdmins] = useState<Array<number>>([]);

    const promiseOfFetchProjectInfo:Promise<Project> = new Promise<Project>((resolve,reject) => {
        const proc = async () => {
            requestQueryById({"id":updateId}).then((result) => {
                resolve(result.data);
            }).catch(error => {
                reject(error);
            });
        }
        proc().then();
    })

    useEffect(() => {
        setLoading(true);
        const promiseAll:Promise<[Project]> = Promise.all([
            promiseOfFetchProjectInfo,
        ]);
        promiseAll.then((result) => {
            const project = result[0];
            form.setFieldsValue(project);
            form.setFieldValue("departmentId",project.departmentId.toString());
            setAdmins(project.admins);
            setLoading(false);
        }).catch(error => {
            console.log(error);
        })
    },[updateId])


    function handlerSubmit(){
        console.log("create submit!")
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
                scrollToFirstError
            >
                    <Form.Item label='Name' field='name' rules={[
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
                        <Input.TextArea placeholder='Please enter ...' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                    </Form.Item>
                    <Form.Item label={'Private'} field="private">
                        <Radio.Group defaultValue={0}>
                            <Radio value={0}>Private</Radio>
                            <Radio value={1}>Public</Radio>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item label={'Admins'} field="admins" rules={[{ required: true,validateTrigger : ['onBlur']}]}>
                        <UserTermQuery initValues={admins} formRef={formRef}/>
                    </Form.Item>
                </Form>
        </Modal>
    );
}
