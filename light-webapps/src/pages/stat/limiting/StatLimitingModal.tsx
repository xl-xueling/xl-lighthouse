import React, {useContext, useEffect, useRef, useState} from 'react';
import {
    Modal,
} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {StatLimitingRecordsPanel} from "@/pages/stat/limiting/limit_records_panel";
import {StatInfoPreviewContext} from "@/pages/common/context";

function StatLimitingModal({onClose}) {

    const t = useLocale(locale);
    const { statInfo, setStatInfo } = useContext(StatInfoPreviewContext);

    return (
        <Modal
            title= {t['limitingRecords.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '150px' }}
            visible={true}
            onCancel={onClose}
            footer={null}>
            <StatLimitingRecordsPanel resourceId={statInfo?.id}/>
        </Modal>
    );
}

export default StatLimitingModal;