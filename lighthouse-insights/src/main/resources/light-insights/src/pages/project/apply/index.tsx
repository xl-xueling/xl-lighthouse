import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';
import {Descriptions, Form, Input, Message, Modal, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/create/locale";
import {requestUpdateById} from "@/api/project";
import {Order, Project} from "@/types/insights-web";
import {requestCreateApply} from "@/api/apply";

export default function ProjectApplyModal({onClose}) {

    const t = useLocale(locale);
    const [loading, setLoading] = useState(false);
    const formRef = useRef(null);

    async function handlerSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        console.log("handler submit,values:" + JSON.stringify(values));
        const order:Order = {
            orderType:3,
            desc:"ss",
        }
        console.log("order is:" + JSON.stringify(order));
        requestCreateApply(order).then((result) => {
            console.log("result:" + JSON.stringify(result));
            if(result.code === '0'){
                Message.success(t['projectCreate.form.submit.success']);
                // setTimeout(() => {
                //     window.location.href = "/project/list";
                // },3000)
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

    const data = [
        {
            label: 'Name',
            value: 'Socrates',
        },
        {
            label: 'Mobile',
            value: '123-1234-1234',
        },
        {
            label: 'Residence',
            value: 'Beijing',
        },
        {
            label: 'Hometown',
            value: 'Beijing',
        },
        {
            label: 'Address',
            value: 'Yingdu Building, Zhichun Road, Beijing',
        },
    ];

    return (
        <Modal
            title= '申请工程权限'
            style={{ width:'750px',top:'20px' }}
            visible={true}
            onOk={handlerSubmit}
            onCancel={onClose}>
            <Typography.Title
                style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
            >
                {'工程信息：'}
            </Typography.Title>
            <Descriptions
                column={1}
                data={data}
                style={{ marginBottom: 20 }}
                labelStyle={{ paddingRight: 36 }}
            />
            <Form
                colon={":"}
                ref={formRef}
                layout={"vertical"}
            >
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'申请原因：'}
                </Typography.Title>
                <Form.Item  field="desc" rules={[
                    {required: true ,message:t['projectCreate.form.description.errMsg'],validateTrigger : ['onSubmit']}
                ]}>
                    <Input.TextArea placeholder='Please enter description.' style={{ minHeight: 64}} maxLength={150} showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}