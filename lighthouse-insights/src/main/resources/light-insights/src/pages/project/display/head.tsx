import React, {useEffect, useState} from 'react';
import useLocale from "@/utils/useLocale";
import locale from "@/pages/metricset/manage/locale";
import {
    IconBook,
    IconClockCircle, IconLock,
    IconUserGroup
} from "@arco-design/web-react/icon";
import {Button, Descriptions, Grid, Message, Modal, Skeleton, Space, Typography} from "@arco-design/web-react";
import { RiAppsLine } from "react-icons/ri";
import {Project} from "@/types/insights-web";
import UserGroup from "@/pages/user/common/groups";
const { Row, Col } = Grid;
export default function DisplayHeader({projectInfo}:{projectInfo:Project}) {
    const t = useLocale(locale);
    const [data,setData] = useState(null);

    useEffect(() => {
        if(!projectInfo){
            return;
        }
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
                    统计工程：{projectInfo?.name}
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
                    column={2}
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