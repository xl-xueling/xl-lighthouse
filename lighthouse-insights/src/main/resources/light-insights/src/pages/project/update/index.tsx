import React, {useEffect, useRef, useState} from 'react';
import {Form, Input, Message, Modal, Notification, Radio, Skeleton, Spin, TreeSelect} from "@arco-design/web-react";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import {translate, translateToFlatStruct} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {requestCreate, requestQueryById, requestUpdateById} from "@/api/project";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {getTextBlenLength} from "@/utils/util";
import {ArcoFlatNode, Department, Project, User} from "@/types/insights-web";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {GlobalErrorCodes, TEXT_BASE_PATTERN_2} from "@/utils/constants";

export default function ProjectUpdatePanel({projectInfo,allDepartInfo,onClose,onSuccess}){

    const t = useLocale(locale);
    const [form] = useForm();
    const formRef = useRef(null);
    const [loading, setLoading] = useState(true);
    const [admins,setAdmins] = useState<Array<number>>([]);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [departmentListData,setDepartmentListData] = useState<ArcoFlatNode[]>();

    useEffect(() => {
        setLoading(true);
        form.setFieldsValue(projectInfo);
        setAdmins(projectInfo.adminIds);
        const flatData = translateToFlatStruct(allDepartInfo)
        const filterArray = [projectInfo.departmentId,userInfo.departmentId];
        setDepartmentListData(flatData?.filter(x => filterArray.includes(Number(x.key))));
        setLoading(false);
    },[projectInfo])


    async function handlerSubmit(){
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
        requestUpdateById(project).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectUpdate.form.submit.success']});
                onClose();
                onSuccess();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
            setLoading(false);
        })
    }

    return (
        <Modal
            title= {t['projectUpdate.form.title']}
            visible={true}
            style={{ width:'850px',top:'20px' }}
            onOk={handlerSubmit}
            confirmLoading={loading}
            onCancel={onClose}>
            <Form
                form={form}
                ref={formRef}
                autoComplete='off'
                scrollToFirstError>
                    <Form.Item label='Title' field='title' rules={[
                        { required: true, message: t['projectUpdate.form.name.errMsg'] , validateTrigger : ['onSubmit']},
                        { required: true, match: new RegExp(TEXT_BASE_PATTERN_2,"g"),message: t['projectUpdate.form.name.validate.errMsg'] , validateTrigger : ['onSubmit']},
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
                    <Form.Item label='Department' field="departmentId" rules={[{ required: true ,message: t['projectUpdate.form.department.errMsg'], validateTrigger : ['onSubmit']}]}>
                        <TreeSelect
                            placeholder={"Please Select"}
                            allowClear={true}
                            treeData={departmentListData}
                        />
                    </Form.Item>
                    <Form.Item label={'Description'} field="desc" rules={[
                        {required: true ,message:t['projectUpdate.form.description.errMsg'],validateTrigger : ['onSubmit']}
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
