import React, {useEffect, useState} from 'react';
import useLocale from "@/utils/useLocale";
import locale from "@/pages/metricset/manage/locale";
import {
    IconBook,
    IconClockCircle, IconLock,
    IconUserGroup
} from "@arco-design/web-react/icon";
import {Button, Descriptions, Message, Space, Typography} from "@arco-design/web-react";
import { RiAppsLine } from "react-icons/ri";
import {Project} from "@/types/insights-web";
import UserGroup from "@/pages/user/common/groups";

export default function DisplayHeader({projectInfo}:{projectInfo:Project}) {
    const t = useLocale(locale);
    const detail = [
        {
            label: <IconUserGroup/>,
            value: <UserGroup users={projectInfo?.admins}/>,
        },
        {
            label: <IconClockCircle />,
            value: projectInfo?.createdTime,
        },
        {
            label: <div style={{ marginTop: 0 }}><IconBook/></div>,
            value: <div style={{ wordBreak: 'break-word' }}>
                <span>{projectInfo?.desc}</span>
            </div>,
            span : 2,
        },

    ];

    return (
        <>
            <Space align="start">
                <Button icon={<RiAppsLine/>} shape={"circle"} size={"small"}/>
                <Typography.Title
                    style={{marginTop:'2px'}}
                    heading={6}>
                    统计工程：{projectInfo?.name}
                </Typography.Title>
                <IconLock
                    style={{fontSize:13,marginTop:'7px'}}
                />
            </Space>
            <div style={{width:'80%'}}>
                <Descriptions
                    size={"mini"}
                    layout="horizontal"
                    data={detail}
                    column={2}
                />
            </div>
        </>
    );
}