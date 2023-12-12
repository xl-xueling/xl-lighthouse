import React, {useEffect, useState} from 'react';
import useLocale from "@/utils/useLocale";
import locale from "@/pages/metricset/manage/locale";
import {
    IconBook,
    IconClockCircle, IconEdit,
    IconFile, IconList, IconLock, IconPushpin,
    IconSettings, IconStar,
    IconStorage,
    IconUserGroup
} from "@arco-design/web-react/icon";
import {Button, Descriptions, Message, Space, Typography} from "@arco-design/web-react";
import styles from "@/pages/metricset/manage/style/shortcuts.module.less";
import { RiAppsLine } from "react-icons/ri";

export default function DisplayHeader() {
    const t = useLocale(locale);

    const dataPicture = [
        {
            label: <IconUserGroup/>,
            value: 'zhangsan;lisi;wangwu;',
        },
        {
            label: <IconClockCircle />,
            value: '2023-12-08 11:00:08',
        },
        {
            label: <div style={{ marginTop: 0 }}><IconBook/></div>,
            value: <div style={{ wordBreak: 'break-word' }}>
                <span>This is a very long text that needs to be wrapped to multiple lines ithat needs to be wrapped to multiple lines if necessary.f necessary.</span>
            </div>,
            span : 2,
        },

    ];

    return (
        <>
            <Space align="start">
                {/*<div className={styles.icon}>*/}
                {/*    <IconPushpin/>*/}
                {/*</div>*/}
                {/*<Typography.Title*/}
                {/*    heading={6}*/}
                {/*>*/}
                {/*    指标集：{'首页用户行为数据集'}*/}
                {/*    /!*<IconLock style={{ marginLeft:'10px',fontSize:13,marginBottom:1}}/>*!/*/}
                {/*</Typography.Title>*/}

                <Button icon={<RiAppsLine/>} shape={"circle"} size={"small"}/>

                <Typography.Title
                    heading={6}
                >
                    统计工程：{'首页用户行为数据统计'}
                    <IconLock style={{ marginLeft:'10px',fontSize:13,marginBottom:1}}/>
                </Typography.Title>

            </Space>
            <div style={{width:'80%',marginTop:'10px'}}>
                <Descriptions
                    size={"mini"}
                    layout="horizontal"
                    data={dataPicture}
                    column={2}
                />
            </div>
        </>
    );
}