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
import {requestQueryById} from "@/api/group";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/common/edittable/EditTable";
import {FormInstance} from "@arco-design/web-react/lib";
import {formatTimeStampBackUp} from "@/utils/util";
const { Row, Col } = Grid;
const { Text } = Typography;

export default function GroupBasicPanel({groupInfo}) {

    const t = useLocale(locale);
    const editTableRef= useRef(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [columnsData,setColumnsData] = useState(null);
    const [formInstance] = Form.useForm();
    const [formKey, setFormKey] = useState(0);

    useEffect(() => {
        if(groupInfo) {
            const columnArr: Array<EditTableColumn> = [];
            for (let i = 0; i < groupInfo.columns.length; i++) {
                const columnInfo = groupInfo.columns[i];
                columnArr.push({...columnInfo, "key": i})
            }
            setColumnsData(columnArr);
            formInstance.setFieldValue("token", groupInfo.token);
            formInstance.setFieldValue("createTime", formatTimeStampBackUp(groupInfo.createTime));
            formInstance.setFieldValue("desc", groupInfo.desc);
            setLoading(false);
        }
    },[groupInfo])

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
                            disabled={true}
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
        }
    ];

    return (
        <div>
            <Row gutter={24}>
                <Col span={24}>
                    <Card >
                        <Spin loading={loading} size={20} style={{ display: 'block' }}>
                            <Form
                                key={formKey}
                                form={formInstance}
                                autoComplete={"off"}
                                className={styles['search-form']}
                                layout={"vertical"}
                            >
                                <Typography.Title
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {'Token'}
                                </Typography.Title>
                                <Form.Item field="token">
                                    <Input disabled={true}/>
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
                                    </Grid.Row>
                                    <Table pagination={false} size={"mini"} columns={columnsProps} data={columnsData}  border={true} />
                                    {/*<EditTable ref={editTableRef} columnsProps={columnsProps} columnsData={columnsData}/>*/}
                                </Form.Item>
                                <Typography.Title
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {'Description'}
                                </Typography.Title>
                                <Form.Item field="desc">
                                    <Input.TextArea disabled={true}
                                        style={{ minHeight: 18, width: '100%' }}
                                    />
                                </Form.Item>
                                <Typography.Title
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {'CreateTime'}
                                </Typography.Title>
                                <Form.Item field="createTime">
                                    <Input disabled={true}/>
                                </Form.Item>
                            </Form>
                        </Spin>
                    </Card>
                </Col>
            </Row>
        </div>
    );
}
