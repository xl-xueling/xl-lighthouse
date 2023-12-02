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

export default function Detail({projectId,onClose}) {

    const [projectInfo,setProjectInfo] = useState<Project>(null);
    const [loading,setLoading] = useState(true);

    const promiseOfFetchProjectInfo:Promise<Project> = new Promise<Project>((resolve,reject) => {
        const proc = async () => {
            requestQueryById({"id":projectId}).then((result) => {
                resolve(result.data);
            }).catch(error => {
                reject(error);
            });
        }
        proc().then();
    })

    useEffect(() => {
        setLoading(true);
        const promiseAll:Promise<[Project]> = Promise.all([
            promiseOfFetchProjectInfo,
        ]);
        promiseAll.then((result) => {
            const project = result[0];
            console.log("project is:" + JSON.stringify(project));
            setLoading(false);
        }).catch(error => {
            console.log(error);
        })
    },[projectId])

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
                        value: '',
                    },
                    {
                        label: 'Date of birth',
                        value: '1995.01.01',
                    },
                    {
                        label: 'City',
                        value: 'Beijing',
                    },
                    {
                        label: 'To work',
                        value: '2017.07',
                    },
                ]}
            />
            <Divider />
            {/*<Descriptions*/}
            {/*    colon=''*/}
            {/*    title='Contact Information'*/}
            {/*    column={1}*/}
            {/*    labelStyle={{ width: 100 }}*/}
            {/*    data={[*/}
            {/*        {*/}
            {/*            label: 'Telephone',*/}
            {/*            value: '+86 136-6333-2888',*/}
            {/*        },*/}
            {/*        {*/}
            {/*            label: 'Email',*/}
            {/*            value: '123456789@163.com',*/}
            {/*        },*/}
            {/*        {*/}
            {/*            label: 'Website',*/}
            {/*            value: <Link>https://123456789/design.com/</Link>,*/}
            {/*        },*/}
            {/*    ]}*/}
            {/*/>*/}
        </Drawer>

    </div>
}