import React, {useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
    Form,
    Input,
    DatePicker,
    Button,
    Grid, TreeSelect, Drawer, Descriptions, Divider, Link,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import {translate} from "@/pages/department/common";
import {Project} from "@/types/insights-web";
import {requestQueryById} from "@/api/project";
const { Row, Col } = Grid;
const { useForm } = Form;

export default function Detail({statInfo,onClose}) {

    const [loading,setLoading] = useState(true);

    useEffect(() => {
        console.log("statInfo is:" + JSON.stringify(statInfo))
    },[statInfo])

    return <div>
        <Drawer
            width= {'55%'}
            title={<span>Statistic Information</span>}
            visible={true}
            placement={"right"}
            onCancel={() => {
                onClose();
            }}
            footer={null}
        >
            <Descriptions
                colon=''
                title=''
                column={1}
                labelStyle={{ width: 100 }}
                data={[
                    {
                        label: 'Title',
                        value: statInfo?.title,
                    },
                    {
                        label: 'Template',
                        value: statInfo?.template,
                    },
                    {
                        label: 'Expired',
                        value: statInfo?.expired,
                    },
                    {
                        label: 'TimeParam',
                        value: statInfo?.timeparam,
                    },
                ]}
            />
        </Drawer>
    </div>
}