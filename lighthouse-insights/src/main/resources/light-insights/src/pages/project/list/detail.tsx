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
import DepartmentLabel from "@/pages/department/common/depart";
const { Row, Col } = Grid;
const { useForm } = Form;

export default function Detail({projectInfo,onClose}) {

    const [loading,setLoading] = useState(true);

    return <div>
        <Drawer
            width={350}
            title={<span>Project Information</span>}
            visible={true}
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
                        label: 'Name',
                        value: projectInfo?.title,
                    },
                    {
                        label: 'Department',
                        value: <DepartmentLabel departmentId={projectInfo?.departmentId}/>,
                    },
                    {
                        label: 'Description',
                        value: projectInfo?.desc,
                    },
                    {
                        label: 'Admins',
                        value: projectInfo?.adminIds,
                    },
                ]}
            />
        </Drawer>
    </div>
}