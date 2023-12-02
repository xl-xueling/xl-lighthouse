import {
    Typography,
    Space,
    Grid,
    TableColumnProps,
    Popconfirm,
    Message,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, Tooltip
} from '@arco-design/web-react';
import {IconDownCircle, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import GroupBasicPanel from "@/pages/group/basic";
import useForm from "@arco-design/web-react/es/Form/useForm";
import StatAddPanel from "@/pages/stat/add/stat_add";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/edit";
const { Row, Col } = Grid;


export default function GroupManagePanel({groupId,onClose}) {

    const TabPane = Tabs.TabPane;
    const { Text } = Typography;

    const [showStatAddPanel, setShowsStatAddPanel] = useState(false);

    const [showGroupEditPanel, setShowGroupEditPanel] = useState(false);

    const [formParams,setFormParams] = useState(null);

    const [loading,setLoading] = useState<boolean>(true);

    const [form] = useForm();

    const t = useLocale(locale);

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
        setFormParams({"title":v});
    }


    return (
    <div className={styles['layout-content']}>
        <div className={styles['manage-panel']}>
            <Tabs
                type="line"
                extra={
                    <Dropdown
                        position={"br"}
                        trigger={"click"}
                        droplist={
                            <Menu onClickMenuItem={handlerProcess} style={{ maxHeight:'280px' }}>
                                <Menu.Item key='1'>创建统计项</Menu.Item>
                                <Menu.Item key='2'>修改统计组</Menu.Item>
                                <Menu.Item key='3'>修改限流阈值</Menu.Item>
                                <Menu.Item key='4'>查看限流记录</Menu.Item>
                                <Menu.Item key='5'>查看秘钥</Menu.Item>
                                <Menu.Item key='6'>删除统计组</Menu.Item>
                            </Menu>
                        }
                    >

                        <Button size={"small"} type={"secondary"}><IconDownCircle />操作</Button>
                    </Dropdown>
                }
            >
                                <TabPane
                                    key='1'
                                    title={
                                        <span>
                    <IconThunderbolt style={{ marginRight: 6 }} />
                        Statistic Items
                  </span>
                                    }
                                >

                                    <div className={styles['search-form-wrapper']} style={{ marginTop:'10px' }}>
                                        <Form
                                            className={styles['search-form']}
                                            labelAlign="left"
                                            labelCol={{ span: 5 }}
                                            wrapperCol={{ span: 19 }}
                                        >
                                            <Row gutter={24}>
                                                <Col span={9}>
                                                    <Form.Item field="Title">
                                                        <Input.Search placeholder={'Title'} allowClear />
                                                    </Form.Item>
                                                </Col>
                                            </Row>
                                        </Form>
                                    </div>
                                    <StatisticalListPanel formParams={formParams} from={"group-manage"} />
                                </TabPane>
                                <TabPane
                                    key='3'
                                    title={
                                        <span>
                    <IconTag style={{ marginRight: 6 }} />
                    Group Info
                  </span>
                                    }
                                >
                                    <GroupBasicPanel groupId={123}/>
                                </TabPane>
            </Tabs>


            {showStatAddPanel && <StatAddPanel onClose={() => setShowsStatAddPanel(false)}/>}

            {showGroupEditPanel && <GroupEditPanel groupId={'1'} onClose={() => setShowGroupEditPanel(false)}/>}

        </div>
    </div>);

}