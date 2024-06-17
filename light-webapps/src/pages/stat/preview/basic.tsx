import React, {useContext, useEffect, useState} from 'react';
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {
    Button,
    Descriptions,
    Divider,
    Form,
    Grid,
    Link,
    Select,
    Space,
    TreeSelect,
    Typography
} from "@arco-design/web-react";
import {IconEdit} from "@arco-design/web-react/icon";
import UserGroup from "@/pages/user/common/groups";
import { TbFilterEdit } from "react-icons/tb";
import {getIcon, getStatExpiredEnumDescription} from "@/desc/base";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";
import DepartmentLabel from "@/pages/department/common/depart";
import {PermissionEnum} from "@/types/insights-common";
import { GoBug } from "react-icons/go";
import {StatInfoPreviewContext} from "@/pages/common/context";


export default function BasicInfo({callback}) {

    const { statInfo, setStatInfo } = useContext(StatInfoPreviewContext);

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
            value:
            <span>
                {statInfo?.permissions.includes(PermissionEnum.ManageAble)?
                    <span style={{display:"inline-flex",alignItems:"center"}}>
                        <Link className={'ldp-custom-link'} href={'/project/manage/' + statInfo?.projectId} target={'_self'}>{statInfo?.projectTitle}</Link> [{statInfo?.projectId}]
                    </span>
                    :<span>{statInfo?.projectTitle + ' [' + statInfo?.projectId+ ']'}</span>
                }
            </span>
            ,
        },
        {
            label: 'Group',
            value: <span>{statInfo?.token} [{statInfo?.groupId}]</span>,
        },
        {
            label: 'Department',
            value: statInfo?.departmentId?<DepartmentLabel departmentId={statInfo?.departmentId} />:'--',
        },
        {
            label: 'Expired',
            value: <span>{getStatExpiredEnumDescription(statInfo?.expired)}</span>,
        },
        {
            label: 'Admins',
            value: statInfo?.admins?<UserGroup users={statInfo?.admins}/>:'--',
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
                    {!statInfo?.builtIn ?
                    <Space size={6}>
                        <Button shape={"circle"} icon={getIcon('limiting')} size={"mini"} onClick={() => callback('showLimitedRecord')}/>
                        {statInfo?.permissions.includes(PermissionEnum.ManageAble)?
                            <>
                            {/*<Button shape={"circle"} icon={<IconEdit/>} size={"mini"} onClick={() => callback('showUpdateModal')}/>*/}
                            <Button shape={"circle"} icon={<GoBug/>} size={"mini"} onClick={() => {window.open("/track/"+statInfo.id)}}/>
                            </>
                            :null}
                        {statInfo?.permissions.includes(PermissionEnum.ManageAble) && statInfo?.templateEntity.dimensArray.length > 0?
                            <Button shape={"circle"} icon={<TbFilterEdit/>} size={"mini"} onClick={() => callback('showFilterConfigModal')}/>:null}
                    </Space> : '--'}
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