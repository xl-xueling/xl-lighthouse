import {
    Typography,
    Grid,
    Message, Form, Input, Select, Spin, Modal, Space, Button
} from '@arco-design/web-react';
import {IconMinusCircleFill, IconPenFill} from '@arco-design/web-react/icon';
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
const { Row, Col } = Grid;

export default function GroupUpdatePanel({groupId,onClose}) {

    const t = useLocale(locale);
    const editTableRef= useRef(null);
    const tempalteEditTableRef= useRef(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [groupInfo,setGroupInfo] = useState<Group>(null);
    const [formInstance] = Form.useForm();

    useEffect(() => {
        setLoading(true);
        const promiseFetchGroupInfo:Promise<Group> = new Promise<Group>((resolve, reject) => {
            console.log("start to Fetch Group Info with id:" + groupId);
            let result:Group;
            const proc = async () => {
                const response = await requestQueryById(groupId);
                if(response.code != '0'){
                    reject(new Error(response.message));
                }
                result = response.data;
                resolve(result);
            }
            proc().then();
        })
        const promiseAll:Promise<[Group]> = Promise.all([
            promiseFetchGroupInfo,
        ])

        promiseAll.then((results) => {
            const groupInfo = results[0];
            setGroupInfo(groupInfo);
            const columnArr:Array<EditTableColumn> = [];
            for(let i=0;i<groupInfo.columns.length;i++){
                const columnInfo = groupInfo.columns[i];
                columnArr.push({...columnInfo,"key":i,"editable":false})
            }
            setInitData(columnArr);
        }).catch(error => {
            console.log(error);
            Message.error(t['system.error']);
        }).finally(() => {
            setLoading(false);
        });
    },[groupId])

    const [initData,setInitData] = useState(null);

    const columnsProps: EditTableColumnProps[]  = [
        {
            title: 'Name',
            dataIndex: 'name',
            componentType:EditTableComponentEnum.INPUT,
            headerCellStyle: { width:'26%'},
        },
        {
            title: 'Type',
            dataIndex: 'type',
            initValue:1,
            componentType:EditTableComponentEnum.SELECT,
            headerCellStyle: { width:'120px'},
            render:(k,record) => (
                <Select size={"mini"} placeholder='Please select' disabled={record.editable != undefined && !record.editable}
                        defaultValue={k}
                >
                    <Select.Option key={1}  value={1}>
                        String
                    </Select.Option>
                    <Select.Option key={2}  value={2}>
                        Number
                    </Select.Option>
                </Select>
            )
        },
        {
            title: 'Description',
            dataIndex: 'desc',
            componentType:EditTableComponentEnum.INPUT,
        },
        {
            title: 'Operate',
            dataIndex: 'operate',
            componentType:EditTableComponentEnum.BUTTON,
            headerCellStyle: { width:'12%'},
            render: (_, record) => (
                <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%',paddingTop:'5px' }}>
                    <IconMinusCircleFill style={{ cursor:"pointer"}} onClick={() => editTableRef.current.removeRow(record.key)}/>
                </Space>
            ),
        },
    ];

    useEffect(() => {
        if(groupInfo != null){
            const formData = {
                "token":groupInfo.token,
            }
            formInstance.setFieldsValue(formData);
        }
    },[groupInfo])

    return (

        <Modal
            title='Group Update'
            visible={true}
            onCancel={onClose}
            style={{ width:'50%',top:'20px' }}
        >
            <Spin loading={loading} size={20} style={{ display: 'block' }}>
                <Form
                    form={formInstance}
                    className={styles['search-form']}
                    layout={"vertical"}
                >
                    <Typography.Title
                        style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                    >
                        {'Token'}
                    </Typography.Title>
                    <Form.Item field="token">
                        <Input
                            allowClear
                            placeholder={'Please Input Token'}
                        />
                    </Form.Item>
                    <Form.Item>
                        <Grid.Row>
                            <Grid.Col span={16}>
                                <Typography.Title
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {'Columns'}
                                </Typography.Title>
                            </Grid.Col>
                            <Grid.Col span={8} style={{ textAlign: 'right' }}>
                                <Button type={"secondary"} size={"mini"} onClick={() => editTableRef.current.addRow()}>添加</Button>
                            </Grid.Col>
                        </Grid.Row>

                        <EditTable ref={editTableRef} columnsProps={columnsProps} columnsData={initData}/>
                    </Form.Item>
                    <Typography.Title
                        style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                    >
                        {'Description'}
                    </Typography.Title>
                    <Form.Item field="desc">
                        <Input.TextArea
                            style={{ minHeight: 18, width: '100%' }}
                        />
                    </Form.Item>
                </Form>
            </Spin>
        </Modal>

    );
}
