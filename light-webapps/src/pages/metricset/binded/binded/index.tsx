import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card, Table, TableColumnProps, Space, Modal
} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import BindedProjectListPanel from "./binded_project";
import {getIcon} from "@/pages/common/desc/base";
import BindedStatisticListPanel from "@/pages/metricset/binded/binded/binded_stat";
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;


export default function NewMetricBindedModal({metricSetInfo,onClose,PRO_ViewBindTab = null}) {

    const t = useLocale(locale);

    return (
        <Modal
            title={t['bindedModal.title']}
            visible={true}
            alignCenter={false}
            style={{ width:'1350px',maxWidth:'90%'
                 ,top:'150px'
            }}
            onCancel={onClose}
            footer={null}
        >
            <Tabs defaultActiveTab='1' tabPosition={"right"} animation={true}>
                <TabPane
                    key='1'
                    title={
                        getIcon('project')
                    }>
                    <BindedProjectListPanel metricSetInfo={metricSetInfo} />
                </TabPane>
                <TabPane
                    key='2'
                    title={
                        getIcon('stat')
                    }>
                    <BindedStatisticListPanel metricSetInfo={metricSetInfo} />
                </TabPane>
                {
                    PRO_ViewBindTab &&
                    PRO_ViewBindTab(metricSetInfo)
                }
            </Tabs>
        </Modal>
    );
}