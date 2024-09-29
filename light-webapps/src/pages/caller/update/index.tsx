import React, {useEffect, useRef, useState} from 'react';
import {Form, Input, Modal,Notification} from "@arco-design/web-react";
import locale from "./locale/index";
import {Caller} from "@/types/caller";
import {requestCreate, requestUpdate} from "@/api/caller";
import useLocale from "@/utils/useLocale";
import {TEXT_BASE_PATTERN_4} from "@/utils/constants";
import {getTextBlenLength} from "@/utils/util";


export default function CallerUpdateModal({callerInfo = null,onClose,onSuccess}){

    const t = useLocale(locale);
    const formRef = useRef(null);
    const [loading,setLoading] = useState<boolean>(false);

    async function handlerSubmit(){
        try{
            await formRef.current.validate();
        }catch (error){
            console.log(error)
            return;
        }
        setLoading(true);
        const values = formRef.current.getFieldsValue();
        console.log("values is:" + JSON.stringify(values));
        const caller:Caller = {
            id:callerInfo?.id,
            name:'caller:' + values.name,
            desc:values.desc,
        }
        console.log('caller is:' + JSON.stringify(caller));
        requestUpdate(caller).then((response) => {
            console.log("---caller is:" + caller);
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['callerUpdate.form.submit.success']});
                const newCallerInfo = {...callerInfo,...caller};
                console.log("newCallerInfo is:" + JSON.stringify(newCallerInfo));
                onSuccess(newCallerInfo);
                onClose();
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
            title= {'修改调用方'}
            alignCenter={false}
            style={{ width:'960px',maxWidth:'80%',verticalAlign:'top', top: '150px' }}
            visible={true}
            onOk={handlerSubmit}
            confirmLoading={loading}
            onCancel={onClose}
            okText={t['basic.form.button.submit']}
            >
            <Form
                autoComplete='off'
                colon={" : "}
                ref={formRef}
                labelCol={{span: 4, offset: 0}}
                initialValues={{name: callerInfo?.name.replace('caller:','') ,desc:callerInfo?.desc}}
            >
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
                <Form.Item label={t['callerUpdate.form.label.desc']} field='desc' rules={[
                    { required: true, message: t['basic.form.verification.empty.warning'] , validateTrigger : ['onSubmit']},
                    ]}>
                    <Input.TextArea placeholder='Please enter description' rows={3} maxLength={90} showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}