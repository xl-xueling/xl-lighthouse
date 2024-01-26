import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card, Table, TableColumnProps, Space, Modal
} from '@arco-design/web-react';
import {
    IconCalendar, IconClockCircle,
    IconDownCircle, IconPlus, IconTag, IconThunderbolt, IconUser
} from '@arco-design/web-react/icon';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import {LuLayers} from "react-icons/lu";
import BindedProjectListPanel from "./binded_project";
import {getIcon} from "@/pages/common/desc/base";
import BindedStatisticListPanel from "@/pages/metricset/binded/binded/binded_stat";
import ProjectListPanel from "@/pages/project/list/ProjectListPanel";
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;


export default function AddBindedPanel({metricSetInfo,onClose}) {

    const t = useLocale(locale);

    return (
        <Modal
            title={t['bindedModal.title']}
            visible={true}
            style={{ width:'85%',height:'85%'}}
            onCancel={onClose}>
            <Tabs defaultActiveTab='1' tabPosition={"right"}>
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
            </Tabs>
        </Modal>
    );
}