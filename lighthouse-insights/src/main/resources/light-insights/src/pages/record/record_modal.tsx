import React, {useEffect, useRef, useState} from 'react';
import {
    Form,
    Grid,
    Input,
    Message,
    Modal,
    Notification,
    Radio,
    Tabs,
    TreeSelect,
    Typography
} from "@arco-design/web-react";



export function RecordModal({resourceId,resourceType,recordType,onClose}){



    useEffect(() => {
    },[])

    return (
        <Modal
            title= {'限流记录'}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            visible={true}
            onCancel={onClose}>
            ssssssss
        </Modal>
    );
}