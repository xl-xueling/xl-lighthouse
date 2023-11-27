import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Link
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
import {requestQueryById} from "@/api/stat";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/common/edittable/EditTable";
import {Line} from "@ant-design/charts";
const { Row, Col } = Grid;

const { Text } = Typography;


export default function StatDisplayPanel({statId}:{statId?:number}) {

    const [statInfo,setStatInfo] = useState<Stat>(null);

    const t = useLocale(locale);

    const fetchStatInfo:() => Promise<Stat> = async () => {
        try{
            const result = await requestQueryById(statId);
            return result.data;
        }catch (error){
            console.log(error)
            Message.error('System Error!');
        }
    }

    useEffect(() => {
        const proc = async () => {
            const statInfo = await fetchStatInfo();
            setStatInfo(statInfo);
        }
        proc().then();
    },[statId])

    const data = [
        { year: '1991', value: 3 },
        { year: '1992', value: 4 },
        { year: '1993', value: 3.5 },
        { year: '1994', value: 5 },
        { year: '1995', value: 4.9 },
        { year: '1996', value: 6 },
        { year: '1997', value: 7 },
        { year: '1998', value: 9 },
        { year: '1999', value: 13 },
    ];

    const config = {
        data,
        height: 400,
        xField: 'year',
        yField: 'value',
        point: {
            size: 5,
            shape: 'diamond',
        },
        theme:'light',
    };

    return (
    <div className={styles['manage-panel']}>
        <Line {...config} />
    </div> );
}