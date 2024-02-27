import React from 'react';
import {Button, Typography, Space, Link, Popconfirm, Select} from '@arco-design/web-react';
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
import {EditTableComponentEnum} from "@/pages/common/edittable/EditTable";


export function getColumns(t: any) {

    return [
        {
            title: 'Name',
            dataIndex: 'name',
            componentType:EditTableComponentEnum.INPUT,
            headerCellStyle: { width:'20%'},
        },
        {
            title: 'Type',
            dataIndex: 'type',
            initValue:"number",
            componentType:EditTableComponentEnum.SELECT,
            headerCellStyle: { width:'130px'},
            render:(value, record) => {
                return (
                    <Select size={"mini"}
                            disabled={true}
                            value={value}
                            defaultValue={"number"}>
                        <Select.Option key={"string"}  value={"string"}>
                            String
                        </Select.Option>
                        <Select.Option key={"number"}  value={"number"}>
                            Number
                        </Select.Option>
                    </Select>)
            }
        },
        {
            title: 'Comment',
            dataIndex: 'comment',
            componentType:EditTableComponentEnum.INPUT,
            editable: true,
        }
    ];
}