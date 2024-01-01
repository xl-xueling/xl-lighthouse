import React, {useEffect, useMemo, useRef, useState} from 'react';
import {Button, Form, Input, Message, Modal, Space, Typography} from "@arco-design/web-react";
import OrderDetail from "@/pages/order/common/detail";


export default function OrderDetailModal({orderInfo,onClose}) {

    return(
        <Modal
            title= '工单详情'
            style={{ width:'850px',top:'20px' }}
            visible={true}
            footer={null}
            onCancel={onClose}>
            <OrderDetail orderInfo={orderInfo} />
            <div style={{ textAlign: 'center', marginTop: '35px' }}>
                <Space size={10}>
                    <Button type="primary" onClick={onClose}>关闭</Button>
                </Space>
            </div>
        </Modal>
    );
}