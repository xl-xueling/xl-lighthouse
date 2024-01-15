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

export function getColumns(t: any, selectedItems:MetricSet[],callback: (type: string,record: Record<string, any>) => Promise<void>) {

    return [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'Description',
            dataIndex: 'desc',
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            render: (_, record) => {
                const selectedKeys = selectedItems?.map(z => z.id);
                if(selectedKeys.includes(record.id)){
                    return <Button key={getRandomString()}
                                   onClick={() => callback("select",record)}
                                   type="secondary"
                                   disabled={true}
                                   size="mini">
                        {'已选择'}
                    </Button>;
                }else{
                    return <Button key={getRandomString()}
                                   onClick={() => callback("select",record)}
                                   type="text"
                                   size="mini">
                        {'选择'}
                    </Button>;
                }
            }
        },
    ];
}