import React, {useEffect, useRef, useState} from 'react';
import {
    Button,
    Card,
    Descriptions,
    Divider,
    Input,
    Link,
    Message,
    Space,
    Spin,
    Tag,
    Tree,
    Typography
} from '@arco-design/web-react';
import {
    IconBook,
    IconCalendar,
    IconClockCircle,
    IconDesktop,
    IconFile, IconFire,
    IconFolder, IconLink, IconLock, IconMobile,
    IconPlus, IconPushpin, IconSettings, IconStar, IconStarFill, IconStorage, IconTag, IconUser, IconUserGroup,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from '../manage/locale';
import styles from './style/shortcuts.module.less';
import {requestStructure} from "@/api/project";
import {ArcoTreeNode} from "@/types/insights-web";

export default function MetricNewDetail() {

    const t = useLocale(locale);

    const shortcuts = [
        {
            title: '添加指标',
            key: 'Content Management',
            icon: <IconFile />,
        },
        {
            title: '数据预览',
            key: 'Content Statistic',
            icon: <IconStorage />,
        },
        {
            title: '权限管理',
            key: 'Advanced Management',
            icon: <IconSettings />,
        },
        // {
        //     title: '线上推广',
        //     key: 'Online Promotion',
        //     icon: <IconMobile />,
        // },
        // {
        //     title: '内容投放',
        //     key: 'Marketing',
        //     icon: <IconFire />,
        // },
    ];


    const recentShortcuts = [
        {
            title: '内容数据',
            key: 'Content Statistic',
            icon: <IconStorage />,
        },
        {
            title: '内容管理',
            key: 'Content Management',
            icon: <IconFile />,
        },
        {
            title: '高级管理',
            key: 'Advanced Management',
            icon: <IconSettings />,
        },
    ];


    function onClickShortcut(key) {
        Message.info({
            content: (
                <span>
          You clicked <b>{key}</b>
        </span>
            ),
        });
    }



    const dataPicture = [
        {
            label: <IconClockCircle />,
            value: '2023-12-08 11:00:08',
            span:1,
        },
        {
            label: <IconUserGroup/>,
            value: 'zhangsan;lisi;wangwu;',
            span:1,
        },
        {
            label: <IconBook/>,
            span : 2,
            value: <div style={{ wordBreak: 'break-word' }}>
                <span>This is a very long text that needs to be wrapped to multiple lines ithat needs to be wrapped to multiple lines if necessary.f necessary.This is a very long text that needs to be wrapped to multiple lines ithat needs to be wrapped to multiple lines if necessary.f necessary.</span>
            </div>
        },

    ];

    return (
        <>
            <Space align="start">
                <div className={styles.icon}>
                    <IconPushpin/>
                </div>
                <Typography.Title
                    style={{ marginTop: 4,marginLeft: 5,marginBottom:15 }}
                    heading={6}
                >
                    指标集：{'首页用户行为数据集'}
                    <IconLock style={{ marginLeft:'10px',fontSize:13,marginBottom:1}}/>
                </Typography.Title>

            </Space>
                <div style={{width:'80%'}}>
                    <Descriptions
                        layout="horizontal"
                        colon="&nbsp;&nbsp;"
                        data={dataPicture}
                        column={2}
                    />
                </div>
        </>
    );
}