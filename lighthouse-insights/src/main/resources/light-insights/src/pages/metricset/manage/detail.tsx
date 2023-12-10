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
    IconFolder, IconLock, IconMobile,
    IconPlus, IconSettings, IconStar, IconStarFill, IconStorage, IconTag, IconUser, IconUserGroup,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from '../manage/locale';
import styles from './style/shortcuts.module.less';
import {requestStructure} from "@/api/project";
import {ArcoTreeNode} from "@/types/insights-web";

export default function MetricDetail() {

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

    const dataStatus = [
        {
            label: (
                <span>
          <Typography.Text style={{ paddingRight: 8 }}>
            {'Title'}
          </Typography.Text>
        </span>
            ),
            value: '6 Mbps',
        },
        {
            label: "Title3",
            value: '60',
        },
        {
            label: (
                <span>
          <Typography.Text style={{ paddingRight: 8 }}>
            {"Title"}
          </Typography.Text>
                    {"Title"}
        </span>
            ),
            value: '6 Mbps',
        },
        {
            label: "Title",
            value: '60',
        },
        {
            label: (
                <span>
          <Typography.Text style={{ paddingRight: 8 }}>
            {t['monitor.studioStatus.coldStandby']}
          </Typography.Text>
                    {t['monitor.studioStatus.bitRate']}
        </span>
            ),
            value: '6 Mbps',
        },
        {
            label: t['monitor.studioStatus.frameRate'],
            value: '60',
        },
    ];
    const dataPicture = [
        {
            label: <div style={{ marginTop: 0 }}><IconBook/></div>,
            value: <div style={{ wordBreak: 'break-word' }}>
                <span>This is a very long text that needs to be wrapped to multiple lines ithat needs to be wrapped to multiple lines if necessary.f necessary.</span>
            </div>
        },
        {
            label: <IconUserGroup/>,
            value: 'zhangsan;lisi;wangwu;',
        },
        {
            label: <IconClockCircle />,
            value: '2023-12-08 11:00:08',
        },
    ];


    return (
            <Card>
                <Space align="start">
                    <Typography.Title
                        //style={{ marginTop: 3,marginLeft: 5,marginBottom:25 }}
                        style={{ textAlign:"center" }}
                        heading={6}
                    >
                        指标集：{'首页用户行为数据集'}
                        <IconLock style={{ marginLeft:'10px',fontSize:13,marginBottom:2}}/>
                    </Typography.Title>

                </Space>
                <div className={styles.shortcuts}>
                    {shortcuts.map((shortcut) => (
                        <div
                            className={styles.item}
                            key={shortcut.key}
                            onClick={() => onClickShortcut(shortcut.key)}
                        >
                            <div className={styles.icon}>{shortcut.icon}</div>
                            <div className={styles.title}>{shortcut.title}</div>
                        </div>
                    ))}
                </div>
                <Divider />
                <div className={styles.recent}>{t['workplace.recent']}</div>
                <Descriptions
                    layout="horizontal"
                    colon="&nbsp;&nbsp;"
                    data={dataPicture}
                    column={1}
                />
            </Card>
    );
}