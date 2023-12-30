import React, {useEffect, useMemo, useState} from 'react';
import {Modal} from "@arco-design/web-react";
import OrderDetail from "@/pages/order/common/detail";

export default function OrderProcessPanel({onClose}) {

    return(
        <Modal
            title= '审核工单'
            style={{ width:'850px',top:'20px' }}
            visible={true}
            onCancel={onClose}>
            <OrderDetail />
        </Modal>
    );
}