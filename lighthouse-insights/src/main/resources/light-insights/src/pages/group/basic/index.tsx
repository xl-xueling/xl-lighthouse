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
const { Row, Col } = Grid;
const { Text } = Typography;

export default function GroupBasicPanel({groupId}) {

    const t = useLocale(locale);
    const editTableRef= useRef(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [columnsData,setColumnsData] = useState(null);
    const [formInstance] = Form.useForm();
    const [formKey, setFormKey] = useState(0);

    useEffect(() => {
        if(groupId){
           setLoading(true);
            const promiseFetchGroupInfo:Promise<Group> = new Promise<Group>((resolve, reject) => {
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
                const columnArr:Array<EditTableColumn> = [];
                for(let i=0;i<groupInfo.columns.length;i++){
                    const columnInfo = groupInfo.columns[i];
                    columnArr.push({...columnInfo,"key":i})
                }
                setColumnsData(columnArr);
                formInstance.setFieldValue("token",groupInfo.token);
                formInstance.setFieldValue("createdTime",groupInfo.createdTime);
                formInstance.setFieldValue("desc",groupInfo.desc);
            }).catch(error => {
                console.log(error);
                Message.error(t['system.error']);
            }).finally(() => {
                setLoading(false);
            });
        }
    },[groupId])

    const columnsProps: EditTableColumnProps[]  = [
        {
            title: 'Name',
            dataIndex: 'name',
            editable: true,
            componentType:EditTableComponentEnum.INPUT,
            headerCellStyle: { width:'26%'},
        },
        {
            title: 'Type',
            dataIndex: 'type',
            editable: true,
            componentType:EditTableComponentEnum.SELECT,
            headerCellStyle: { width:'4%'},
            render:(k,v) => (
                <Select size={"mini"} placeholder='Please select' disabled={true} bordered={false}
                        onChange={editTableRef.current.cellValueChangeHandler}
                        defaultValue={k}
                        style={{ border:'1px solid var(--color-neutral-3)' }}
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
            editable: true,
        },
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
                                    <EditTable ref={editTableRef} columnProps={columnsProps} editmode={false} columnsData={columnsData}/>
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
                                    {'CreatedTime'}
                                </Typography.Title>
                                <Form.Item field="createdTime">
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
