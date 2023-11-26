import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message,
    Button,
    Form,
    Input,
    InputTag,
    Select,
    Skeleton,
    Spin,
    Tag,
    Icon,
    Tabs,
    Radio,
    TreeSelect,
    DatePicker,
    Dropdown, Menu
} from '@arco-design/web-react';
import {
    IconCopy,
    IconDashboard, IconDoubleDown, IconDownCircle, IconFile,
    IconMinus,
    IconMinusCircleFill,
    IconMore,
    IconPen, IconPenFill,
    IconPlus, IconPlusCircle,
    IconPlusCircleFill, IconRefresh, IconSearch, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import GroupStatistics from "@/pages/project/manage/statistic-list";
import {
    Column,
    Department,
    Group,
    PrivilegeEnum,
    Project,
    ProjectPagination,
    Stat,
    StatPagination,
    User
} from "@/types/insights-web";
import {requestQueryById} from "@/api/group";
import {requestQueryByGroupId} from "@/api/stat";
import EditTable, {
    EditTableColumn,
    EditTableColumnProps,
    EditTableComponentEnum
} from "@/pages/components/edittable/EditTable";
import StatEditPanel from "@/pages/project/manage/stat_edit";
import GroupEditPanel from "@/pages/project/manage/group_edit";
import StatisticalListPanel from "@/pages/stat/list/stat_list_bak";
import GroupBasicPanel from "@/pages/group/basic";
import {ResultData} from "@/types/insights-common";
import {requestPrivilegeCheck} from "@/api/privilege";
import StatisticalListPanelV2 from "@/pages/stat/list/stat_list";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {stringifyObj} from "@/utils/util";
import dayjs from "dayjs";
const { Row, Col } = Grid;

export default function GroupManagePanel({groupId = 1,onClose}) {

    const TabPane = Tabs.TabPane;

    const [formParams,setFormParams] = useState(null);

    const [loading,setLoading] = useState<boolean>(true);

    const [form] = useForm();

    const t = useLocale(locale);

    useEffect(() => {
        if(groupId){
            setLoading(true);
            // const promiseFetchGroupInfo:Promise<Group> = new Promise<Group>((resolve, reject) => {
            //     console.log("start to Fetch Group Info with id:" + groupId);
            //     let result:Group;
            //     const proc = async () => {
            //         const response = await requestQueryById(groupId);
            //         if(response.code != '0'){
            //             reject(new Error(response.message));
            //         }
            //         result = response.data;
            //         resolve(result);
            //     }
            //     proc().then();
            // })


        }
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
                            <Menu>
                                <Menu.Item key='1'>创建统计项</Menu.Item>
                                <Menu.Item key='2'>修改统计组</Menu.Item>
                                <Menu.Item key='3'>修改限流阈值</Menu.Item>
                                <Menu.Item key='3'>查看限流记录</Menu.Item>
                                <Menu.Item key='3'>查看秘钥</Menu.Item>
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
                                    <StatisticalListPanelV2 formParams={formParams}  />
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

            {/*<Grid.Row justify="space-between" align="center">*/}
            {/*    <Grid.Col span={16} style={{ textAlign: 'left' }}>*/}
            {/*        <Space>*/}
            {/*            <Tabs type="line">*/}
            {/*                <TabPane*/}
            {/*                    key='1'*/}
            {/*                    title={*/}
            {/*                        <span>*/}
            {/*    <IconThunderbolt style={{ marginRight: 6 }} />*/}
            {/*        Statistic Items*/}
            {/*  </span>*/}
            {/*                    }*/}
            {/*                >*/}

            {/*                    <StatisticalListPanelV2 formParams={formParams}  />*/}
            {/*                </TabPane>*/}
            {/*                <TabPane*/}
            {/*                    key='3'*/}
            {/*                    title={*/}
            {/*                        <span>*/}
            {/*    <IconTag style={{ marginRight: 6 }} />*/}
            {/*    Group Info*/}
            {/*  </span>*/}
            {/*                    }*/}
            {/*                >*/}
            {/*                    <GroupBasicPanel groupId={123}/>*/}
            {/*                </TabPane>*/}
            {/*            </Tabs>*/}
            {/*        </Space>*/}
            {/*    </Grid.Col>*/}
            {/*    <Grid.Col span={8} style={{ textAlign: 'right' }}>*/}
            {/*        <Space>*/}
            {/*        <Button size={"small"} type={"secondary"}><IconDownCircle />操作</Button>*/}
            {/*        </Space>*/}
            {/*    </Grid.Col>*/}
            {/*</Grid.Row>*/}


        </div>
    </div>);

}