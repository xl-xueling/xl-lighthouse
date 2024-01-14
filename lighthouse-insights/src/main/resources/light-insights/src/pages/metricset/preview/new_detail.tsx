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
import locale from './/locale';
import styles from './style/shortcuts.module.less';
import {requestStructure} from "@/api/project";
import {ArcoTreeNode} from "@/types/insights-web";
import { LuLayers } from "react-icons/lu";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";

export default function MetricNewDetail({metricSetInfo}) {

    const t = useLocale(locale);

    const metricDetail = [
        {
            label: <IconClockCircle />,
            value: formatTimeStamp(metricSetInfo?.createTime,DateTimeFormat),
            span:1,
        },
        {
            label: <IconUserGroup/>,
            value: <UserGroup users={metricSetInfo.admins}/>,
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
                    <LuLayers/>
                </div>
                <Typography.Title
                    heading={6}
                >
                    指标集：{'首页用户行为数据集'}
                    <IconLock style={{ marginLeft:'10px',fontSize:13,marginBottom:1}}/>
                </Typography.Title>

            </Space>
                <div style={{width:'80%',marginTop:'10px'}}>
                    <Descriptions
                        size={"mini"}
                        layout="horizontal"
                        data={metricDetail}
                        column={2}
                    />
                </div>
        </>
    );
}