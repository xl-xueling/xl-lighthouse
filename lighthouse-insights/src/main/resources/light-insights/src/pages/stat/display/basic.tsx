import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {Button, Descriptions, Divider, Form, Grid, Select, Space, TreeSelect, Typography} from "@arco-design/web-react";
import {IconEdit, IconList, IconPublic, IconPushpin} from "@arco-design/web-react/icon";
import UserGroup from "@/pages/user/common/groups";
import { TbFilterEdit } from "react-icons/tb";
import {getStatExpiredEnumDescription} from "@/pages/common/desc/base";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import DepartmentLabel from "@/pages/department/common/depart";


export default function BasicInfo({statInfo,callback}) {

    const descriptionData = [
        {
            label: 'ID',
            value: statInfo?.id,
        },
        {
            label: 'Title',
            value: statInfo?.title,
        },
        {
            label: 'Project',
            value: <span>{statInfo?.project.title}</span>,
        },
        {
            label: 'Group',
            value: <span>{statInfo?.group.token}</span>,
        },
        {
            label: 'Department',
            value: <DepartmentLabel departmentId={statInfo.project?.departmentId} />,
        },
        {
            label: 'Expired',
            value: <span>{getStatExpiredEnumDescription(statInfo?.expired)}</span>,
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
            label: 'CreateTime',
            value: formatTimeStamp(statInfo?.createTime,DateTimeFormat),
        },
        {
            label: 'Operation',
            value:
                <div>
                    <Space size={6}>
                        <Button shape={"circle"} icon={<IconList/>} size={"mini"}/>
                        <Button shape={"circle"} icon={<IconEdit/>} size={"mini"}/>
                        <Button shape={"circle"} icon={<IconPushpin/>} size={"mini"}/>
                        <Button shape={"circle"} icon={<TbFilterEdit/>} size={"mini"} onClick={() => callback('showFilterConfigModal')}/>
                    </Space>
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
                labelStyle={{ width:'10%' }}
                valueStyle={{ width:'40%' }}
                size={"small"} border data={descriptionData} column={2}/>
        </>
    );
}