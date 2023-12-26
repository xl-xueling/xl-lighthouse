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
import {getRandomString} from "@/utils/util";
const { Row, Col } = Grid;

export default function GroupUpdatePanel({groupInfo,onClose}) {

    const t = useLocale(locale);
    const editTableRef= useRef(null);
    const tempalteEditTableRef= useRef(null);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [loading,setLoading] = useState<boolean>(true);
    const [formInstance] = Form.useForm();
    const [initData,setInitData] = useState(null);

    const [expandedKeys, setExpandedKeys] = useState([]);

    const columnsProps: EditTableColumnProps[]  = [
        {
            title: 'Name',
            dataIndex: 'name',
            editable: true,
            componentType:EditTableComponentEnum.INPUT,
            headerCellStyle: { width:'20%'},
        },
        {
            title: 'Type',
            dataIndex: 'type',
            editable: true,
            initValue:"number",
            componentType:EditTableComponentEnum.SELECT,
            headerCellStyle: { width:'130px'},
            render:(text, record) => {
                return (
                    <Select size={"mini"}
                            popupVisible={expandedKeys.includes(record.key)}
                            onChange={(value) => {record['type'] = value}}
                            onFocus={(e) => {
                                setExpandedKeys((keys) => [...keys, record.key]);
                            }}
                            onKeyDown={(event) => {
                                if(event.key == 'Enter'){
                                    setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                                }
                            }}
                            onBlur={() => {
                                setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                            }}
                            defaultValue={"number"}>
                        <Select.Option key={"string"}  value={"string"} onClick={() => {
                            setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                        }}>
                            String
                        </Select.Option>
                        <Select.Option key={"number"}  value={"number"} onClick={() => {
                            setExpandedKeys((keys) => keys.filter((key) => key !== record.key));
                        }}>
                            Number
                        </Select.Option>
                    </Select>)
            }
        },
        {
            title: 'Comment',
            dataIndex: 'comment',
            componentType:EditTableComponentEnum.INPUT,
            editable: true,
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            componentType:EditTableComponentEnum.BUTTON,
            headerCellStyle: { width:'12%'},
            render: (_, record) => (
                <Space size={24} direction="vertical" style={{ textAlign:"center",width:'100%'}}>
                    <IconMinusCircleFill style={{ cursor:"pointer"}} onClick={() => editTableRef.current.removeRow(record.key)}/>
                </Space>
            ),
        },
    ];

    useEffect(() => {
        console.log("groupInfo is:" + JSON.stringify(groupInfo));
        if(groupInfo != null){
            const formData = {
                "token":groupInfo.token,
            }
            const columnArr: Array<EditTableColumn> = [];
            for (let i = 0; i < groupInfo.columns.length; i++) {
                const columnInfo = groupInfo.columns[i];
                columnArr.push({...columnInfo, "key": i})
            }
            setInitData(columnArr);
            formInstance.setFieldsValue(formData);
            setLoading(false);
        }
    },[groupInfo])

    return (

        <Modal
            title='Group Update'
            visible={true}
            maskClosable={false}
            confirmLoading={confirmLoading}
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
