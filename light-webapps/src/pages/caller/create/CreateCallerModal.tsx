import React, {useEffect, useMemo, useState} from 'react';
import {
    Breadcrumb,
    Button,
    Card,
    Grid, Input,
    Message, Modal,
    Notification,
    PaginationProps,
    Radio,
    Space, Spin,
} from '@arco-design/web-react';
import {IconHome} from "@arco-design/web-react/icon";
import locale from "./locale";
import useLocale from "@/utils/useLocale";
const { Row, Col } = Grid;

export default function CreateCallerModal({onClose}) {

    const t = useLocale(locale);

    return (
        <Modal
            title= {t['permissionManage.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '130px' }}
            visible={true}
            onCancel={onClose}>
            ssss
        </Modal>
    );
}