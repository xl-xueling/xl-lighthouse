import React, {useEffect, useRef, useState} from 'react';
import {Form, Input, Modal, Notification, TreeSelect} from "@arco-design/web-react";
import locale from "./locale/index";
import {Caller} from "@/types/caller";
import {requestCreate, requestUpdate} from "@/api/caller";
import useLocale from "@/utils/useLocale";
import {TEXT_BASE_PATTERN_4} from "@/utils/constants";
import {getTextBlenLength} from "@/utils/util";
import {translateToFlatStruct} from "@/pages/department/base";
import {useSelector} from "react-redux";
import {TreeNode} from "@/types/insights-web";


export default function CallerUpdateModal({callerInfo = null,onClose,onSuccess}){

    const t = useLocale(locale);
    const formRef = useRef(null);
    const [loading,setLoading] = useState<boolean>(false);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);

    async function handlerSubmit(){
        try{
            await formRef.current.validate();
        }catch (error){
            console.log(error)
            return;
        }
        setLoading(true);
        const values = formRef.current.getFieldsValue();
        const caller:Caller = {
            id:callerInfo?.id,
            name:'caller:' + values.name,
            departmentId:values.departmentId,
            desc:values.desc,
        }
        requestUpdate(caller).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['callerUpdate.form.submit.success']});
                const newCallerInfo = {...callerInfo,...caller};
                onSuccess(newCallerInfo);
                setTimeout(() => {
                    onClose();
                },50)
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
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
            title= {t['callerUpdate.title']}
            alignCenter={false}
            style={{ width:'960px',maxWidth:'80%',verticalAlign:'top', top: '150px' }}
            visible={true}
            onOk={handlerSubmit}
            confirmLoading={loading}
            onCancel={onClose}
            okText={t['basic.form.button.submit']}
            focusLock={false}
            autoFocus={false}
            >
            <Form
                autoComplete='off'
                colon={" : "}
                ref={formRef}
                labelCol={{span: 4, offset: 0}}
                initialValues={{
                    name: callerInfo?.name.replace('caller:',''),
                    departmentId:callerInfo?.departmentId,
                    desc:callerInfo?.desc
                }}>
                <Form.Item label={t['callerUpdate.form.label.name']} field='name' rules={[
                    { required: true, message: t['basic.form.verification.empty.warning'] , validateTrigger : ['onSubmit']},
                    { required: true, match: new RegExp(TEXT_BASE_PATTERN_4,"g"),message: t['basic.form.verification.failed.warning'] , validateTrigger : ['onSubmit']},
                    {
                        required:true,
                        validator: (v, cb) => {
                            if (getTextBlenLength(v) < 5) {
                                return cb(t['basic.form.verification.length.less.warning'])
                            }else if (getTextBlenLength(v) > 30) {
                                return cb(t['basic.form.verification.length.larger.warning'])
                            }
                            cb(null);
                        }
                        , validateTrigger : ['onSubmit']
                    }]}>
                    <Input addBefore={'caller:'} disabled={true} autoFocus={false} />
                </Form.Item>
                <Form.Item label={t['callerUpdate.form.label.department']} field="departmentId" rules={[{ required: true ,message: t['callerUpdate.form.department.errMsg'], validateTrigger : ['onSubmit']}]}>
                    <TreeSelect
                        disabled={true}
                        treeProps={{
                            height: 200,
                            renderTitle: (props) => {
                                return (
                                    <span style={{ whiteSpace: 'nowrap', }} >
                                            {props.title}
                                        </span>
                                );
                            },
                        }}
                        placeholder={"Please Select"}
                        allowClear={true}
                        treeData={translateToFlatStruct(allDepartInfo)}
                    />
                </Form.Item>
                <Form.Item label={t['callerUpdate.form.label.desc']} field='desc' rules={[
                    { required: true, message: t['basic.form.verification.empty.warning'] , validateTrigger : ['onSubmit']},
                    ]}>
                    <Input.TextArea placeholder='Please enter description' rows={3} maxLength={90} showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}