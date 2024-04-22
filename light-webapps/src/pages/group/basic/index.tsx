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
import React, {useEffect, useMemo, useRef, useState} from 'react';
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
import {getColumns} from "./constants";
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
            for (let i = 0; i < groupInfo?.columns.length; i++) {
                const columnInfo = groupInfo?.columns[i];
                columnArr.push({...columnInfo, "key": i})
            }
            setColumnsData(columnArr);
            formInstance.setFieldValue("token", groupInfo?.token+" ["+groupInfo?.id+"]");
            formInstance.setFieldValue("createTime", formatTimeStampBackUp(groupInfo?.createTime));
            formInstance.setFieldValue("desc", groupInfo?.desc);
            setLoading(false);
        }
    },[groupInfo])

    const [expandedKeys, setExpandedKeys] = useState([]);

    const columnsProps = useMemo(() => getColumns(t), [t]);

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
                                    {t['group.basic.label.token']}
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
                                                {t['group.basic.label.column']}
                                            </Typography.Title>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Table pagination={false} size={"mini"} columns={columnsProps} data={columnsData}  border={true} />
                                    {/*<EditTable ref={editTableRef} columnsProps={columnsProps} columnsData={columnsData}/>*/}
                                </Form.Item>
                                <Typography.Title
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {t['group.basic.label.description']}
                                </Typography.Title>
                                <Form.Item field="desc">
                                    <Input.TextArea disabled={true}
                                        style={{ minHeight: 18, width: '100%' }}
                                    />
                                </Form.Item>
                                <Typography.Title
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {t['group.basic.label.createTime']}
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
