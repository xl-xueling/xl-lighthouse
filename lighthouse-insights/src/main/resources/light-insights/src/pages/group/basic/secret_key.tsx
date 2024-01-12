import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Link, Modal, Notification
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
const { Title } = Typography;
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {Column, Department, Group, Stat, User} from "@/types/insights-web";
import {requestGetSecretKey, requestQueryById} from "@/api/group";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/common/edittable/EditTable";
import {FormInstance} from "@arco-design/web-react/lib";
import {formatTimeStampBackUp} from "@/utils/util";
import useLocale from "@/utils/useLocale";
const { Row, Col } = Grid;
const { Text } = Typography;

export default function SecretKeyModal({groupId,onClose}) {

    const t = useLocale(locale);
    const [secretKey,setSecretKey] = useState<string>(null);
    const [loading,setLoading] = useState<boolean>(false);

    const fetchData = async () => {
        setLoading(true);
        await requestGetSecretKey({id:groupId}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setSecretKey(data);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }


    useEffect(()=> {
        fetchData().then();
    },[groupId])

    return (
        <Modal
            title={t['group.basic.secretKey']}
            visible={true}
            footer={null}
            style={{ width:'750px' }}
            onCancel={onClose}>
            <div>{secretKey}</div>
        </Modal>
    )
}