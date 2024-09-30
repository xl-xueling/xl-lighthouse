import React, {useContext, useEffect, useRef, useState} from 'react';
import {Modal} from "@arco-design/web-react";

export default function AuthExtension({onClose}){

    const handleSubmit = () => {

    }

    return (
        <Modal
            visible={true}
            alignCenter={false}
            style={{ width:'1200px',maxWidth:'80%',verticalAlign:'top', top: '150px' }}
            onCancel={onClose}
            onOk={handleSubmit}
            title='授权续签'
            autoFocus={false}
            focusLock={true}
        >

        </Modal>
    );
}