import React, {useEffect, useMemo, useRef, useState} from 'react';
import {Button, Form, Input, Message, Modal, Space, Typography} from "@arco-design/web-react";
import OrderDetail from "@/pages/order/common/detail";
import styles from "@/pages/metricset/display/style/index.module.less";
import {Order} from "@/types/insights-web";
import {requestApprove} from "@/api/order";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/login/locale";

export default function OrderProcessPanel({orderInfo,onClose}) {

    const [visible, setVisible] = React.useState(false);
    const formRef = useRef(null);
    const t = useLocale(locale);
    const [loading, setLoading] = useState(false);

    async function approvedSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        console.log("values is:" + JSON.stringify(values))
        const approveParam = {
            id:orderInfo.id,
            roleId:orderInfo.currentNode,
            reply:values.reply,
            result:1,
        }
        console.log("approvedSubmit is:" + JSON.stringify(approveParam))
        requestApprove(approveParam).then((result) => {
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

    async function rejectedSubmit() {
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        console.log("values is:" + JSON.stringify(values))
        const approveParam = {
            id:orderInfo.id,
            roleId:orderInfo.currentNode,
            reply:values.reply,
            result:2,
        }
        console.log("rejectedSubmit is:" + JSON.stringify(approveParam))
    }

    return(
        <Modal
            title= '审核工单'
            style={{ width:'850px',top:'20px' }}
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
                    审核批复
                </Typography.Title>
                <Form.Item field={'reply'}>
                    <Input.TextArea maxLength={200} rows={2}  showWordLimit={true}/>
                </Form.Item>
            </Form>
            <div style={{ textAlign: 'center', marginTop: '35px' }}>
                <Space size={10}>
                    <Button type="primary" onClick={approvedSubmit}>通过</Button>
                    <Button type="primary" status='danger' onClick={rejectedSubmit}>拒绝</Button>
                </Space>
            </div>
        </Modal>
    );
}