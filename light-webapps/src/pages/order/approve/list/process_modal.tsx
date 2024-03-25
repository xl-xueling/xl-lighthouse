import React, {useEffect, useRef, useState} from 'react';
import {
    Button,
    Form,
    Input,
    Message,
    Modal,
    Notification, Popconfirm,
    Skeleton,
    Space,
    Spin,
    Typography
} from "@arco-design/web-react";
import OrderDetail from "@/pages/order/common/detail";
import {requestProcess, requestQueryById} from "@/api/order";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {OrderStateEnum, ResultData} from "@/types/insights-common";
import {Order} from "@/types/insights-web";
import {getRandomString} from "@/utils/util";

export default function OrderProcessModal({orderId,onClose,onReload}) {

    const t = useLocale(locale);
    const formRef = useRef(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [orderInfo,setOrderInfo] = useState<Order>(null);
    const [agreeLoading, setAgreeLoading] = useState(false);
    const [rejectLoading, setRejectLoading] = useState(false);

    async function fetchData () {
        const id = orderId;
        setLoading(true);
        requestQueryById({id}).then((response:ResultData) => {
            setOrderInfo(response.data);
        }).catch((error) => {
            console.log(error);
        }).finally(() => {
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchData().then();
    },[])

    async function approvedSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const approveParam = {
            id:orderInfo.id,
            roleId:orderInfo.currentNode,
            reply:values.reply,
            state:1,
        }
        setAgreeLoading(true);
        requestProcess(approveParam).then((result) => {
            if(result.code === '0'){
                Notification.info({
                    style: { width: 420 },
                    title: 'Notification',
                    content: t['approveModal.form.submit.success'],
                })
            }else{
                Notification.warning({
                    style: { width: 420 },
                    title: 'Warning',
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
            setAgreeLoading(false);
            onClose();
            onReload();
        })
    }

    async function rejectedSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const approveParam = {
            id:orderInfo.id,
            roleId:orderInfo.currentNode,
            reply:values.reply,
            state:2,
        }
        setRejectLoading(true);
        requestProcess(approveParam).then((result) => {
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
                title: 'Notification',
                content: t['system.error'],
            })
        }).finally(() => {
            setRejectLoading(false);
            onClose();
            onReload();
        })
    }

    return(

        <Modal
            title= {t['approveModal.title']}
            style={{ width:'1150px',maxWidth:'80%',top:'20px' }}
            visible={true}
            footer={null}
            onCancel={onClose}>
            <Skeleton
                loading={loading}
                text={{
                    rows:5,
                    width: ['100%'],
                }}
                animation
            >
                {<OrderDetail orderInfo={orderInfo} />}
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
                        <Input.TextArea maxLength={100} rows={2} showWordLimit={true}/>
                    </Form.Item>
                </Form>
                <div style={{ textAlign: 'center', marginTop: '35px' }}>
                    <Space size={10}>
                        <Popconfirm key={getRandomString()}
                                    focusLock
                                    position={"tr"}
                                    title='Confirm'
                                    content={t['approveModal.button.agree.confirm']}
                                    onOk={approvedSubmit}
                        >
                            <Button type="primary" loading={agreeLoading}>{t['approveModal.button.agree']}</Button>
                        </Popconfirm>
                        <Popconfirm key={getRandomString()}
                                    focusLock
                                    position={"tr"}
                                    title='Confirm'
                                    okButtonProps={{ status:"danger" }}
                                    content={t['approveModal.button.reject.confirm']}
                                    onOk={rejectedSubmit}
                        >
                            <Button type="primary" loading={rejectLoading} status='danger'>{t['approveModal.button.reject']}</Button>
                        </Popconfirm>
                    </Space>
                </div>
            </Skeleton>
        </Modal>

    );
}