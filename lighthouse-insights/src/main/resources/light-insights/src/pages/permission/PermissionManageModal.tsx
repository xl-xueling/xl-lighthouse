import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Button,
    Collapse,
    Form,
    Grid,
    Input,
    Modal,
    Notification,
    PaginationProps,
    Space,
    Table,
    Tabs
} from "@arco-design/web-react";
import {Permission} from "@/types/insights-web";
import {OwnerTypeEnum, ResourceTypeEnum, RoleTypeEnum} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {getRandomString} from "@/utils/util";
import {requestGrantProjectPermission, requestQueryList, requestReleaseProjectPermission} from "@/api/permission";
import {getDepartPermissionColumns, getUserPermissionColumns} from "./constants";
import './styles/index.module.less'
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import {PermissionPanel} from "@/pages/permission/PermissionPanel";

const CollapseItem = Collapse.Item;
const TabPane = Tabs.TabPane;

export function PermissionManageModal({type = 2,resourceId,resourceType,onClose}){

    const t = useLocale(locale);

    return (
        <Modal
            title= {t['permissionManage.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '130px' }}
            visible={true}
            footer={null}
            onCancel={onClose}>
            <Tabs type={"card-gutter"} defaultActiveTab='1'>
                <TabPane key='1' title= {t['permissionManage.department.accessPermission']}>
                    <PermissionPanel type={1} resourceType={resourceType} resourceId={resourceId}/>
                </TabPane>
                <TabPane key='2' title={t['permissionManage.user.accessPermission']}>
                    <PermissionPanel type={2} resourceType={resourceType} resourceId={resourceId}/>
                </TabPane>
                <TabPane key='3' title={t['permissionManage.user.managePermission']}>
                    <PermissionPanel type={3} resourceType={resourceType} resourceId={resourceId}/>
                </TabPane>
            </Tabs>
        </Modal>
    );
}