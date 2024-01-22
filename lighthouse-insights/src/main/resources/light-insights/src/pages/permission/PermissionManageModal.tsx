import React, {useEffect, useRef, useState} from 'react';
import {
    Collapse, Divider,
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
const CollapseItem = Collapse.Item;

export function PermissionManageModal({resourceId,resourceType,onClose}){

    useEffect(() => {
        console.log("resourceId:" + resourceId + ",resourceType:" + resourceType)
    },[])

    return (
        <Modal
            title= {'权限管理'}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            visible={true}
            onCancel={onClose}>

            <Collapse
                defaultActiveKey={['1', '2']}
                style={{ maxWidth: 1180 }}
            >
                <CollapseItem header='部门访问权限' name='1'>
                    Beijing Toutiao Technology Co., Ltd.
                    <Divider style={{ margin: '8px 0' }}/>
                    Beijing Toutiao Technology Co., Ltd.
                    <Divider style={{ margin: '8px 0' }}/>
                    Beijing Toutiao Technology Co., Ltd.
                </CollapseItem>
                <CollapseItem header='用户访问权限' name='2'>
                   sss
                </CollapseItem>

                <CollapseItem header='管理员权限' name='3'>
                    ssasg
                </CollapseItem>
            </Collapse>
        </Modal>
    );
}