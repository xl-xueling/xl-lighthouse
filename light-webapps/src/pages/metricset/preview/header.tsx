import React, {useContext, useEffect, useRef, useState} from 'react';
import {
    Button,
    Card,
    Descriptions,
    Divider, Grid,
    Input,
    Link,
    Message, Skeleton,
    Space,
    Spin,
    Tag,
    Tree,
    Typography
} from '@arco-design/web-react';
import {
    IconBook,
    IconClockCircle,
    IconUserGroup,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './/locale';
import { LuLayers } from "react-icons/lu";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import {CiLock} from "react-icons/ci";
import MetricSetStar from "@/pages/metricset/common/MetricSetStar";
import {MetricSetPreviewContext} from "@/pages/common/context";
const { Row, Col } = Grid;
export default function MetricSetPreviewHeader() {

    const t = useLocale(locale);
    const { metricSetInfo, setMetricSetInfo } = useContext(MetricSetPreviewContext);

    const metricDetail = [
        {
            label: <IconClockCircle />,
            value: formatTimeStamp(metricSetInfo?.createTime,DateTimeFormat),
            span:1,
        },
        {
            label: <IconUserGroup/>,
            value: <UserGroup users={metricSetInfo?.admins}/>,
            span:1,
        },
        {
            label: <IconBook/>,
            span : 2,
            value: <div style={{ wordBreak: 'break-word' }}>
                <span>{metricSetInfo?.desc}</span>
            </div>
        },
    ];

    return (
        <>
            {
                metricSetInfo?
                    <Space size={0} direction="vertical" style={{ width: '100%' }}>
                        <Row>
                            <Grid.Col span={15}>
                                <span style={{display:"inline-flex",alignItems:"center"}}>
                                    <Button icon={<LuLayers/>} shape={"circle"} size={"small"} style={{marginRight:'10px',marginBottom:'7px'}}/>
                                    <Typography.Title
                                        heading={6}
                                        style={{marginTop:'0px'}}
                                    >
                                       {t['metricsetPreview.title.prefix']}：{metricSetInfo?.title}
                                        {metricSetInfo?.privateType == 0 && <CiLock style={{marginLeft:'5px',fontSize:14}}/>}
                                    </Typography.Title>
                                </span>
                            </Grid.Col>
                            <Grid.Col span={9} style={{textAlign:"right" }}>
                                <MetricSetStar metricInfo={metricSetInfo}/>
                            </Grid.Col>
                        </Row>
                        <Row>
                            <Descriptions
                                style={{width:'80%'}}
                                size={"mini"}
                                layout="horizontal"
                                data={metricDetail}
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
            }
        </>
    );
}