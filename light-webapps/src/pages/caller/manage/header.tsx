import React, {useEffect, useRef, useState} from 'react';
import {Button, Descriptions, Grid, Skeleton, Space,Typography} from "@arco-design/web-react";
import {LuLayers} from "react-icons/lu";
import { LuFunctionSquare } from "react-icons/lu";
import { TbApi } from "react-icons/tb";
import { VscDebugDisconnect } from "react-icons/vsc";
import { GoPaperAirplane } from "react-icons/go";
import { VscCode } from "react-icons/vsc";
import { VscSymbolParameter } from "react-icons/vsc";
import {CiLock} from "react-icons/ci";
import locale from "./locale/index"
const { Row, Col } = Grid;
import { VscTools } from "react-icons/vsc";
import {IconBook, IconClockCircle, IconUserGroup} from "@arco-design/web-react/icon";
import Text from "@arco-design/web-react/es/Typography/text";
import useLocale from "@/utils/useLocale";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import UserGroup from "@/pages/user/common/groups";
import { IoExtensionPuzzleOutline } from "react-icons/io5";
import {PiSparkleLight} from "react-icons/pi";



export default function Header({callerInfo}){

    const t = useLocale(locale);

    const callerDetail = [
        {
            label: <IconClockCircle />,
            value: formatTimeStamp(callerInfo?.createTime,DateTimeFormat),
            span:1,
        },
        {
            label: <IconUserGroup/>,
            value: <UserGroup users={callerInfo?.admins}/>,
            span:1,
        },
        {
            label: <IconBook/>,
            span : 2,
            value: <div style={{ wordBreak: 'break-word' }}>
                <span>{callerInfo?.desc}</span>
            </div>
        },
    ];

    return (
        <>
            {
                callerInfo?
                    <Space size={0} direction="vertical" style={{ width: '100%' }}>
                        <Row>
                            <Grid.Col span={15}>
                                <span style={{display:"inline-flex",alignItems:"center"}}>
                                    <Button icon={<PiSparkleLight style={{strokeWidth:'7px'}} size={15}/>} shape={"circle"} size={"small"} style={{marginRight:'10px',marginBottom:'7px'}}/>
                                    <Typography.Title
                                        heading={6}
                                        style={{marginTop:'0px'}}
                                    >
                                        {t['callerManage.label.caller.prefix']}<Text copyable={true}>{callerInfo?.name}</Text>
                                    </Typography.Title>
                                </span>
                            </Grid.Col>
                            <Grid.Col span={9} style={{textAlign:"right" }}>

                            </Grid.Col>
                        </Row>
                        <Row>
                            <Descriptions
                                style={{width:'80%',marginLeft:'3px',marginTop:'2px'}}
                                size={"mini"}
                                layout="horizontal"
                                data={callerDetail}
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