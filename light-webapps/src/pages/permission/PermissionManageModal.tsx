import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Collapse,
    Modal,
    Tabs
} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import './styles/index.module.less'
import {PermissionPanel} from "@/pages/permission/PermissionPanel";

const CollapseItem = Collapse.Item;
const TabPane = Tabs.TabPane;

export function PermissionManageModal({type = 2,resourceId,resourceType,onClose,showTabs = ['1','2','3']}){

    const t = useLocale(locale);

    return (
        <Modal
            title= {t['permissionManage.modal.title']}
            alignCenter={false}
            style={{width:'1180px',maxWidth:'80%', top: '100px' }}
            visible={true}
            footer={null}
            onCancel={onClose}>
            <Tabs type={"card-gutter"} defaultActiveTab={showTabs[0]}>
                {
                    showTabs.includes('1') && <TabPane key='1' title= {t['permissionManage.department.accessPermission']}>
                        <PermissionPanel type={1} resourceType={resourceType} resourceId={resourceId}/>
                    </TabPane>
                }
                {
                    showTabs.includes('2') && <TabPane key='2' title={t['permissionManage.user.accessPermission']}>
                        <PermissionPanel type={2} resourceType={resourceType} resourceId={resourceId}/>
                    </TabPane>
                }
                {
                    showTabs.includes('3') && <TabPane key='3' title={t['permissionManage.user.managePermission']}>
                        <PermissionPanel type={3} resourceType={resourceType} resourceId={resourceId}/>
                    </TabPane>
                }
            </Tabs>
        </Modal>
    );
}