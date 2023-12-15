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
import BindedProject from "@/pages/metricset/binded/binded/binded_project";
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;


export default function AddBindedPanel({metricId}) {

    const t = useLocale(locale);

    return (
        <Modal
            title={'绑定数据项 - [统计工程]'}
            visible={true}
            style={{ width:'85%',height:'85%'}}
        >
            <Tabs defaultActiveTab='1' tabPosition={"right"}>
                <TabPane
                    key='1'
                    title={
                        <span>
            <IconCalendar style={{ marginRight: 6 }} />
            Tab 1
          </span>
                    }
                >
                    {/*<Typography.Paragraph >Content of Tab Panel 1</Typography.Paragraph>*/}
                    <BindedProject />
                </TabPane>
                <TabPane
                    key='2'
                    title={
                        <span>
            <IconClockCircle style={{ marginRight: 6 }} />
            Tab 2
          </span>
                    }
                >
                    <Typography.Paragraph >Content of Tab Panel 2</Typography.Paragraph>
                </TabPane>
                <TabPane
                    key='3'
                    title={
                        <span>
            <IconUser style={{ marginRight: 6 }} />
            Tab 3
          </span>
                    }
                >
                    <Typography.Paragraph>Content of Tab Panel 3</Typography.Paragraph>
                </TabPane>
            </Tabs>

        </Modal>
    );
}