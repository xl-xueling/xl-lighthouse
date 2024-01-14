import React, {useEffect, useState} from 'react';
import useLocale from "@/utils/useLocale";
import locale from "@/pages/metricset/preview/locale";
import {
    IconBook,
    IconClockCircle, IconLock,
    IconUserGroup
} from "@arco-design/web-react/icon";
import {Button, Descriptions, Grid, Message, Modal, Skeleton, Space, Typography} from "@arco-design/web-react";
import { RiAppsLine } from "react-icons/ri";
import {Project} from "@/types/insights-web";
import UserGroup from "@/pages/user/common/groups";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
const { Row, Col } = Grid;
export default function PreviewHeader({projectInfo}:{projectInfo:Project}) {
    const t = useLocale(locale);
    const [data,setData] = useState(null);

    useEffect(() => {
        const detail = [
            {
                label: <IconClockCircle />,
                value: formatTimeStamp(projectInfo?.createTime,DateTimeFormat),
                span: 1,
            },
            {
                label: <IconUserGroup/>,
                value: <UserGroup users={projectInfo?.admins}/>,
                span: 2,
            },
            {
                label: <div style={{ marginTop: 0 }}><IconBook/></div>,
                value: <div style={{ wordBreak: 'break-word' }}>
                    <span>{projectInfo?.desc}</span>
                </div>,
                span : 3,
            },
        ];
        setData(detail);
    },[projectInfo])

    return (
        projectInfo?
        <Space size={0} direction="vertical" style={{ width: '100%' }}>
            <Row>
                <Button icon={<RiAppsLine/>} shape={"circle"} size={"small"} style={{marginRight:'10px'}}/>
                <Typography.Title
                    style={{marginRight:'5px'}}
                    heading={6}>
                    统计工程：{projectInfo?.title}
                </Typography.Title>
                <IconLock
                    style={{fontSize:13,marginTop:'6px'}}
                />
            </Row>
            <Row>
                <Descriptions
                    style={{width:'80%'}}
                    size={"mini"}
                    layout="horizontal"
                    data={data}
                    column={3}
                />
            </Row>
        </Space>
        :
            <Skeleton
                text={{
                    rows:3,
                    width: ['100%'],
                }}
                animation
            />
    );
}