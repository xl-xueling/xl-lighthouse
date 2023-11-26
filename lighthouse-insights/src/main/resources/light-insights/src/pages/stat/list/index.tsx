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
import GroupStatistics from "@/pages/project/manage/statistic-list";
import {Column, Department, Group, PrivilegeEnum, Project, Stat, StatPagination, User} from "@/types/insights-web";
import {requestQueryByIds as requestQueryGroupByIds} from "@/api/group";
import {requestQueryByIds as requestQueryProjectByIds} from "@/api/project";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/components/edittable/EditTable";
import StatEditPanel from "@/pages/project/manage/stat_edit";
import GroupEditPanel from "@/pages/project/manage/group_edit";
import {requestPrivilegeCheck} from "@/api/privilege";
import {ResultData} from "@/types/insights-common";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "@/pages/project/list/locale";
import StatisticalListPanelV2 from "@/pages/stat/list/stat_list";
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

    return (
        <Card>
            <SearchForm onSearch={handleSearch} onClear={handleReset} form={form}/>
            <StatisticalListPanelV2 formParams={formParams} />
        </Card>
    );
}