import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Tabs, PaginationProps
} from '@arco-design/web-react';
import {
    IconDashboard, IconFile,
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen, IconPenFill,
    IconPlus,
    IconPlusCircleFill, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {Column, Department, Group, PrivilegeEnum, Project, Stat, StatPagination, User} from "@/types/insights-web";
import {requestQueryByIds as requestQueryGroupByIds} from "@/api/group";
import {requestQueryByIds as requestQueryProjectByIds} from "@/api/project";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/common/edittable/EditTable";
import {requestPrivilegeCheck} from "@/api/privilege";
import {ResultData} from "@/types/insights-common";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "@/pages/project/list/locale";
import SearchForm from "@/pages/stat/list/form";
import useForm from "@arco-design/web-react/es/Form/useForm";

export default function StatisticalListIndex() {

    const t = useLocale(locale);

    const [data, setData] = useState([]);

    const [formParams, setFormParams] = useState({});

    const [form] = useForm();

    function handleSearch(params) {
        setFormParams(params);
    }

    function handleReset(){
        form.resetFields();
        handleSearch({});
    }

    function onClickRadio(p){
        handleReset();
    }

    const columns: TableColumnProps[] = [
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'Project',
            dataIndex: 'project.name',
        },
        {
            title: 'Group',
            dataIndex: 'group.token',
        },
        {
            title: 'Department',
            dataIndex: 'department.name',
        },
        {
            title: 'TimeParam',
            dataIndex: 'timeparam',
        },
        {
            title: 'Expired',
            dataIndex: 'expired',
        },
        {
            title: 'Operate',
            dataIndex: 'operate',
            headerCellStyle: {width:'200px' },
            render: (_, record) => (
                <Space size={16} direction="horizontal">
                    <Popconfirm
                        focusLock
                        position={"tr"}
                        title='Confirm'
                        content='Are you sure to reset this user password2?'
                        onCancel={() => {
                            Message.error({
                                content: 'cancel',
                            });
                        }}
                    >
                        <Button
                            type="secondary"
                            size="mini">
                            {'查看'}
                        </Button>
                    </Popconfirm>
                    <Button
                        type="secondary"
                        size="mini">
                        {'修改'}
                    </Button>
                    <Button
                        type="secondary"
                        size="mini">
                        {'停用'}
                    </Button>
                    <Popconfirm
                        position={"tr"}
                        focusLock
                        title='Confirm'
                        content='Are you sure to delete this project?'
                        onCancel={() => {
                            Message.error({
                                content: 'cancel',
                            });
                        }}
                    >
                        <Button
                            type="secondary"
                            size="mini">
                            {'冻结'}
                        </Button>
                    </Popconfirm>
                    <Popconfirm
                        position={"tr"}
                        focusLock
                        title='Confirm'
                        content='Are you sure to delete this project?'
                        onCancel={() => {
                            Message.error({
                                content: 'cancel',
                            });
                        }}
                    >
                        <Button
                            type="secondary"
                            size="mini">
                            {'删除'}
                        </Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <Card>
            <SearchForm onSearch={handleSearch} onClear={handleReset} form={form}/>
            <StatisticalListPanel formParams={formParams} columns={columns} />
        </Card>
    );
}