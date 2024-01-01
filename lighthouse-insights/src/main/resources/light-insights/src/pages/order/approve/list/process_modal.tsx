import React, {useRef, useState} from 'react';
import {Button, Form, Input, Message, Modal, Notification, Space, Typography} from "@arco-design/web-react";
import OrderDetail from "@/pages/order/common/detail";
import {requestApprove} from "@/api/order";
import useLocale from "@/utils/useLocale";
import locale from "./locale";

export default function OrderProcessModal({orderInfo,onClose}) {

    const formRef = useRef(null);
    const t = useLocale(locale);
    const [loading, setLoading] = useState(false);

    async function approvedSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const approveParam = {
            id:orderInfo.id,
            roleId:orderInfo.currentNode,
            reply:values.reply,
            result:1,
        }
        setLoading(true);
        requestApprove(approveParam).then((result) => {
            if(result.code === '0'){
                Notification.info({
                    style: { width: 420 },
                    title: 'Notification',
                    content: t['approveModal.form.submit.success'],
                })
            }else{
                Notification.warning({
                    style: { width: 420 },
                    title: 'Notification',
                    content: result.message || t['system.error'],
                })
            }
        }).catch((error) => {
            console.log(error);
            Notification.error({
                style: { width: 420 },
                title: 'Error',
                content: t['system.error'],
            })
        }).finally(() => {
            setLoading(false);
        })
    }

    async function rejectedSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const approveParam = {
            id:orderInfo.id,
            roleId:orderInfo.currentNode,
            reply:values.reply,
            result:2,
        }
        setLoading(true);
        requestApprove(approveParam).then((result) => {
            console.log("result:" + JSON.stringify(result));
            if(result.code === '0'){
                Notification.info({
                    style: { width: 420 },
                    title: 'Notification',
                    content: t['approveModal.form.submit.success'],
                })
                // setTimeout(() => {
                //     window.location.href = "/project/list";
                // },3000)
            }else{
                Notification.warning({
                    style: { width: 420 },
                    title: 'Notification',
                    content: result.message || t['system.error'],
                })
            }
        }).catch((error) => {
            console.log(error);
            Notification.error({
                style: { width: 420 },
                title: 'Notification',
                content: t['system.error'],
            })
        }).finally(() => {
            setLoading(false);
        })
    }

    return(
        <Modal
            title= {t['approveModal.title']}
            style={{ width:'850px',top:'20px' }}
            confirmLoading={loading}
            visible={true}
            footer={null}
            onCancel={onClose}>
            <OrderDetail orderInfo={orderInfo} />
            <Form
                ref={formRef}
                wrapperCol={{ span: 24 }}
            >
                <Typography.Title
                    style={{ marginTop: 30 }}
                    heading={6}
                >
                    {t['approveModal.label.reply']}
                </Typography.Title>
                <Form.Item field={'reply'}>
                    <Input.TextArea maxLength={200} rows={2}  showWordLimit={true}/>
                </Form.Item>
            </Form>
            <div style={{ textAlign: 'center', marginTop: '35px' }}>
                <Space size={10}>
                    <Button type="primary" onClick={approvedSubmit}>{t['approveModal.button.agree']}</Button>
                    <Button type="primary" status='danger' onClick={rejectedSubmit}>{t['approveModal.button.reject']}</Button>
                </Space>
            </div>
        </Modal>
    );
}