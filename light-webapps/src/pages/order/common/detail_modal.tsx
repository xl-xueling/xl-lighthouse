import React, {useEffect, useMemo, useRef, useState} from 'react';
import {Button, Form, Input, Message, Modal, Skeleton, Space, Typography} from "@arco-design/web-react";
import OrderDetail from "@/pages/order/common/detail";
import {Order} from "@/types/insights-web";
import {requestQueryById} from "@/api/order";
import {ResultData} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/order/common/locale";


export default function OrderDetailModal({orderId,onClose}) {

    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(true);
    const [orderInfo,setOrderInfo] = useState<Order>(null);

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

    return(
        <Modal
            title= {t['detailModal.title']}
            alignCenter={false}
            style={{ width:'1150px',maxWidth:'80%',verticalAlign:'top', top: '80px' }}
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
                <OrderDetail orderInfo={orderInfo} />
                <div style={{ textAlign: 'center', marginTop: '35px' }}>
                    <Space size={10}>
                        <Button type="primary" onClick={onClose}>{t['detailModal.form.button.close']}</Button>
                    </Space>
                </div>
            </Skeleton>
        </Modal>
    );
}