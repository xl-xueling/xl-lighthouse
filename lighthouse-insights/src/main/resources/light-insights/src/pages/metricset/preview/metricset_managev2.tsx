import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card
} from '@arco-design/web-react';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import useForm from "@arco-design/web-react/es/Form/useForm";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/update";
const { Row, Col } = Grid;

export default function MetricManagePanel2({groupId}) {
    const TabPane = Tabs.TabPane;
    const { Text } = Typography;
    const [showStatAddPanel, setShowsStatAddPanel] = useState(false);
    const [showGroupEditPanel, setShowGroupEditPanel] = useState(false);
    const [formParams,setFormParams] = useState(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [form] = useForm();
    const t = useLocale(locale);
    const TreeNode = TreeSelect.Node;

    const handlerProcess = (action):void => {
        switch (action){
            case '1':{
                setShowsStatAddPanel(true);
                break;
            }
            case '2':{
                setShowGroupEditPanel(true);
                break;
            }
            default:{
                return;
            }
        }
    }

    useEffect(() => {
        setFormParams({"groupId":groupId});
    },[groupId])

    const handlerSubmit = (v) => {
        setFormParams({"title":v,"groupId":groupId});
    }

    return (

        <div className={styles['layout-content']}>
            <Card>
            <div className={styles['manage-panel']}>
                <div className={styles['search-form-wrapper']} style={{ marginTop:'10px',marginLeft:'20px'}}>
                    <Form
                        className={styles['search-form']}
                        labelAlign="left"
                        labelCol={{ span: 5 }}
                        wrapperCol={{ span: 19 }}
                    >
                        <Row gutter={24}>
                            <Col span={10}>
                                <Form.Item field="Title">
                                    <Input.Search  placeholder={'Search Title'} allowClear onSearch={(v) => {
                                        handlerSubmit(v);
                                    }} />
                                </Form.Item>
                            </Col>
                        </Row>
                    </Form>
                </div>
                <StatisticalListPanel formParams={formParams} from={"group-manage"} />
            </div>
            </Card>
        </div>

    );

}