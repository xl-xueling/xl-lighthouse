import React, { useState, useEffect } from 'react';
import {Button, Card, Divider, Grid, Link, Menu, Modal, Space, Spin, Trigger, Typography} from '@arco-design/web-react';
import {IconMessage, IconClose, IconBug, IconBulb, IconUser, IconArrowRight} from '@arco-design/web-react/icon';
const MenuItem = Menu.Item;
import { RiNavigationFill } from "react-icons/ri";
import { TbNavigationPlus } from "react-icons/tb";
import { MdOutlineNavigation } from "react-icons/md";
import {useSelector} from "react-redux";
import {MetricSet} from "@/types/insights-web";
import Overview from "@/pages/dashboard/workplace/overview";
import MetricSetCardBox from "@/pages/metricset/list/MetricSetCardBox";
import styles from "./style/index.module.less";
import {Avatar} from "@arco-design/web-react/lib";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
const { Row, Col } = Grid;

export default function MetricNavModal ({onClose}){

    const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);

    const t = useLocale(locale);

    return (
        <Modal
            title={t['MetricNavModal.modal.title']}
            visible={true}
            onCancel={onClose}
            alignCenter={false}
            cancelText={t['basic.form.button.close']}
            footer={(cancelBtn,okBtn) => {
                return cancelBtn;
            }}
            style={{ width:'1800px',maxWidth:'90%'
                ,top:'30px',maxHeight:'90%',overflow:'auto',
            }}>
            <Space size={16} direction="vertical" style={{width:'100%'}}>
                <Row gutter={16}>
                {staredMetricInfo.map((item, index) => (
                    <Col span={6} key={index}>
                        <MetricSetCardBox size={'small'} key={index} item={item}/>
                    </Col>
                ))}
                </Row>
            </Space>
        </Modal>
    );
}