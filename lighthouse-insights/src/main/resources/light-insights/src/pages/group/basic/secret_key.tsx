import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Link, Modal
} from '@arco-design/web-react';
import {
    IconCopy,
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen, IconPenFill,
    IconPlus,
    IconPlusCircleFill, IconSearch
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
const { Title } = Typography;
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {Column, Department, Group, Stat, User} from "@/types/insights-web";
import {requestQueryById} from "@/api/group";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/common/edittable/EditTable";
import {FormInstance} from "@arco-design/web-react/lib";
import {formatTimeStamp} from "@/utils/util";
const { Row, Col } = Grid;
const { Text } = Typography;

export default function SecretKeyModal({onClose}) {

    return (
        <Modal
            title={'Secret Key'}
            visible={true}
            style={{ width:'750px' }}
            onCancel={onClose}>
            <p>
                You can customize modal body text by the current situation. This modal will be closed
                immediately once you press the OK button.
            </p>
        </Modal>
    )
}