import React, {useState} from 'react';
import {Form, Grid, Input, Modal, Radio, Tabs, Typography} from "@arco-design/web-react";
import styles from "@/pages/project/manage/style/index.module.less";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/manage/locale";
import {Col} from "antd";
import UsersTransfer from "@/pages/components/transfer/user_transfer";
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";


export default function MetricSetAddPanel({onClose}) {

    const [confirmLoading, setConfirmLoading] = useState(false);
    const [form] = Form.useForm();
    const t = useLocale(locale);
    const FormItem = Form.Item;
    const { Col, Row } = Grid;

    const [showPickUpPanel,setShowPickUpPanel] = useState<boolean>(false);

    function changeVisibleType(value){
        console.log("value is:" + value);
        if(value == '1'){
            setShowPickUpPanel(true);
        }else{
            setShowPickUpPanel(false);
        }
    }

    const treeData = [
        {
            key: '1',
            title: 'Trunk 1',
            children: [
                {
                    key: '1-1',
                    title: 'Branch',
                    children: [
                        {
                            key: '1-1-1',
                            title: 'Leaf',
                        },
                        {
                            key: '1-1-2',
                            title: 'Leaf',
                        },
                    ],
                },
            ],
        },
        {
            key: '2',
            title: 'Trunk 2',
            children: [
                {
                    key: '2-1',
                    title: 'Trunk 2-1',
                },
                {
                    key: '2-2',
                    title: 'Trunk 2-2',
                },
                {
                    key: '2-3',
                    title: 'Trunk 2-3',
                },
            ],
        },
        {
            key: '3',
            title: 'Trunk 3',
        },
        {
            key: '4',
            title: 'Trunk 4',
        },
    ];

    const [targetKeys, setTargetKeys] = useState(['2-1', '2-2', '2-3', '4']);
    const onChange = (keys) => {
        setTargetKeys(keys);
    };

    return (
        <Modal
            title='创建指标集'
            visible={true}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            confirmLoading={confirmLoading}
            onCancel={onClose}
        >
            <Form
                form={form}
                colon={true}
                style={{ minHeight:'300px' }}
                labelCol={{span: 4, offset: 0}}
                className={styles['search-form']}
                layout={"horizontal"}>
                <Form.Item field="name" label={"Name"}
                           rules={[
                               { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onSubmit'] },
                               { required: true, match: new RegExp(/^[a-z0-9_]{5,20}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onSubmit']},
                           ]}>
                    <Input
                        allowClear
                        placeholder={'Please Input Token'} />
                </Form.Item>
                <Form.Item field="desc" label={"Description"} rules={[
                    { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onSubmit'] },
                    { required: true, match: new RegExp(/^[^￥{}【】#@=^&|《》]{0,200}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onSubmit']},
                ]}>
                    <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
                </Form.Item>
                <Form.Item field="isPrivate" label={'IsPrivate'}>
                    <Radio.Group defaultValue={"0"} onChange={changeVisibleType}>
                        <Radio value={"0"}>对其他人不可见</Radio>
                        <Radio value={"2"}>对所有人员可见</Radio>
                        <Radio value={"1"}>对部分人员可见</Radio>
                    </Radio.Group>
                </Form.Item>

                {showPickUpPanel &&
                <Form.Item label={'人员圈选'}>
                    <Row>
                        <Col
                            span={24}
                            style={{ marginBottom: 12 }}
                        >
                            <Tabs key='card' tabPosition={"right"}>
                                <Tabs.TabPane key='1' title='Tab1'>
                                    <DepartmentsTransfer />
                                </Tabs.TabPane>
                                <Tabs.TabPane key='2' title='Tab2'>
                                    <UsersTransfer />
                                </Tabs.TabPane>
                            </Tabs>
                        </Col>
                    </Row>
                </Form.Item>
                }
            </Form>
        </Modal>
    );
}
