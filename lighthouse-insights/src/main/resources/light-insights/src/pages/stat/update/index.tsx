import {
    Card, Form, Input, Modal, Radio, Skeleton, TreeSelect,
} from '@arco-design/web-react';
import React, {useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "./locale";
import SearchForm from "@/pages/stat/list/form";
import {getTextBlenLength} from "@/utils/util";
import {translate} from "@/pages/department/common";
import UserTermQuery from "@/pages/user/common/userTermQuery";


export default function StatUpdateModal({statInfo,onClose}) {
    const formRef = useRef(null);
    const t = useLocale(locale);

    return (
        <Modal
            title= {"修改统计项"}
            visible={true}
            style={{ width:'750px',top:'20px' }}
            onCancel={onClose}>

            <Skeleton
                text={{
                    rows:3,
                    width: ['100%'],
                }}
                animation
            />
            <Form
                ref={formRef}
                autoComplete='off'
                scrollToFirstError
            >
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
                <Form.Item label={'Description'} field="desc" rules={[
                    {required: true ,message:t['projectUpdate.form.description.errMsg'],validateTrigger : ['onBlur']}
                ]}>
                    <Input.TextArea placeholder='Please enter description' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                </Form.Item>

            </Form>
        </Modal>

    );
}