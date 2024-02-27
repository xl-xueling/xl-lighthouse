import React from 'react';
import {Button, Typography, Space, Link, Popconfirm} from '@arco-design/web-react';
const { Text } = Typography;
import { PiLinkSimple } from "react-icons/pi";
import { IoInformationCircleOutline } from "react-icons/io5";
import UserGroup from "@/pages/user/common/groups";
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import DepartmentLabel from "@/pages/department/common/depart";
import { CiLock } from "react-icons/ci";
import { LuLock } from "react-icons/lu";
import { PiLockBold } from "react-icons/pi";
import {RiAppsLine} from "react-icons/ri";
import {MetricSet} from "@/types/insights-web";
import {BindElement} from "@/types/insights-common";

export function getColumns(t: any, bindElement:BindElement,selectedItems:MetricSet[],callback: (type: string,record: Record<string, any>) => Promise<void>) {

    return [
        {
            title: t['reverseBinded.column.label.id'],
            dataIndex: 'id',
        },
        {
            title: t['reverseBinded.column.label.title'],
            dataIndex: 'title',
        },
        {
            title: t['reverseBinded.column.label.admins'],
            dataIndex: 'admins',
            render: (value, record) => {
                return <UserGroup users={value}/>
            }
        },
        {
            title: t['reverseBinded.column.label.description'],
            dataIndex: 'desc',
        },
        {
            title: t['reverseBinded.column.label.operation'],
            dataIndex: 'operation',
            render: (_, record) => {
                const bindFlag = record.bindElements?.map(x => x.resourceType + "_" + x.resourceId).includes(bindElement.resourceType + "_" + bindElement.resourceId);
                const selectedKeys = selectedItems?.map(z => z.id);
                if(bindFlag){
                    return <Button key={getRandomString()}
                                   type="secondary"
                                   disabled={true}
                                   size="mini">
                        {t['reverseBinded.column.label.operation.binded']}
                    </Button>;
                } else if(selectedKeys.includes(record.id)){
                    return <Button key={getRandomString()}
                                   type="secondary"
                                   disabled={true}
                                   size="mini">
                        {t['reverseBinded.column.label.operation.selected']}
                    </Button>;
                }else{
                    return <Button key={getRandomString()}
                                   onClick={() => callback("select",record)}
                                   type="text"
                                   size="mini">
                        {t['reverseBinded.column.label.operation.select']}
                    </Button>;
                }
            }
        },
    ];
}