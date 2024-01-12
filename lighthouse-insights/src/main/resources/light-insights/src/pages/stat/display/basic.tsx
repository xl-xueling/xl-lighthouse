import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {Button, Descriptions, Divider, Form, Grid, Select, Space, TreeSelect, Typography} from "@arco-design/web-react";
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
            value: <span>{statInfo?.project.title}</span>,
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
                    <Space size={6}>
                        <Button shape={"circle"} icon={<IconList/>} size={"mini"}/>
                        <Button shape={"circle"} icon={<IconEdit/>} size={"mini"}/>
                        <Button shape={"circle"} icon={<IconPushpin/>} size={"mini"}/>
                        <Button shape={"circle"} icon={<TbFilterEdit/>} size={"mini"}/>
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
                // title='Basic Information'
                labelStyle={{ width:'10%' }}
                valueStyle={{ width:'40%' }}
                size={"small"} border data={descriptionData} column={2}/>
        </>
    );
}