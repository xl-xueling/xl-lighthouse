import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {RenderTypeEnum, ResultData} from "@/types/insights-common";
import {requestQueryDimensValue} from "@/api/group";
import {Button, Descriptions, Divider, Form, Grid, Select, TreeSelect, Typography} from "@arco-design/web-react";
import {Col} from "antd";
import {translate} from "@/pages/department/common";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "@/pages/stat/display/style/index.module.less";
import { Line } from '@ant-design/plots';
import { Chart, Line as Line2, Point, Tooltip,getTheme } from "bizcharts";
import { LineAdvance} from 'bizcharts';
import {IconEdit, IconList, IconPublic, IconPushpin} from "@arco-design/web-react/icon";
import UserGroup from "@/pages/user/common/groups";
import { TbFilterEdit } from "react-icons/tb";


export default function BasicInfo({statInfo}) {

    const descriptionData = [
        {
            label: 'Title',
            value: statInfo?.title,
        },
        {
            label: 'CreatedTime',
            value: statInfo?.createdTime,
        },
        {
            label: 'Group',
            value: <span>{statInfo?.group.token}</span>,
        },
        {
            label: 'Project',
            value: <span>{statInfo?.project.name}</span>,
        },
        {
            label: 'Expired',
            value: <span>{statInfo?.expired}</span>,
        },
        {
            label: 'Admins',
            value: <UserGroup users={statInfo?.admins}/>,
        },

        {
            label: 'TimeParam',
            value: statInfo?.timeparam,
        },
        {
            label: 'Operation',
            value:
                <div>
                    <IconList style={{marginRight:'10px'}} />
                    <IconEdit style={{marginRight:'10px'}} />
                    <IconPushpin style={{marginRight:'10px'}} />
                    <TbFilterEdit style={{marginRight:'10px'}} />
                    <Button shape={"square"} icon={<TbFilterEdit/>} size={"mini"}/>
                </div>,
        },
        {
            label: 'Template',
            value: statInfo?.template,
            span:2,
        },

    ];

    return (
        <>
            <Descriptions
                // title='Basic Information'
                labelStyle={{ width:'10%' }}
                valueStyle={{ width:'40%' }}
                size={"small"} border data={descriptionData} column={2}/>
        </>
    );
}