import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message,
    Button,
    Form,
    Input,
    InputTag,
    Select,
    Skeleton,
    Spin,
    Tag,
    Icon,
    Tabs,
    Radio,
    TreeSelect,
    DatePicker,
    Dropdown, Menu, Tooltip
} from '@arco-design/web-react';
import {
    IconCopy,
    IconDashboard, IconDoubleDown, IconDownCircle, IconFile,
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen, IconPenFill,
    IconPlus, IconPlusCircle,
    IconPlusCircleFill, IconRefresh, IconSearch, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {
    Column,
    Department,
    Group,
    PrivilegeEnum,
    Project,
    ProjectPagination,
    Stat,
    StatPagination,
    User
} from "@/types/insights-web";
import {requestQueryById} from "@/api/group";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/common/edittable/EditTable";
import GroupBasicPanel from "@/pages/group/basic";
import {ResultData} from "@/types/insights-common";
import {requestPrivilegeCheck} from "@/api/privilege";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {stringifyObj} from "@/utils/util";
import dayjs from "dayjs";
import StatAddPanel from "@/pages/stat/add/stat_add";
import GroupAddPanel from "@/pages/group/add/group_add";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/edit";
const { Row, Col } = Grid;






export default function ProjectDisplayIndex() {

    return (
        <div>ssss</div>
        );
}