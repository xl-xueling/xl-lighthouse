import React, {useEffect, useRef, useState} from 'react';
import {
    Form,
    Grid,
    Input,
    Message,
    Modal,
    Notification, PaginationProps,
    Radio, Table, TableColumnProps,
    Tabs,
    TreeSelect,
    Typography
} from "@arco-design/web-react";
import {Record, User} from "@/types/insights-web";
import {requestList} from "@/api/record";
import {RecordTypeEnum, ResultData} from "@/types/insights-common";
import {GlobalErrorCodes} from "@/utils/constants";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {formatTimeStampBackUp} from "@/utils/util";
import {LimitedRecord, translateRecord} from "@/pages/record/record";


export function LimitingSettingsModal(){

    const t = useLocale(locale);

    return (
        <Modal
            title= {t['limitingConfig.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '130px' }}
            visible={true}
            footer={null}>
            sss
        </Modal>
    )
}