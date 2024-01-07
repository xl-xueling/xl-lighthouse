import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, Message, Space, Divider, Notification, Spin, Skeleton,
} from '@arco-design/web-react';
import {
    IconDelete,
    IconDownCircle, IconTag, IconTags,
} from '@arco-design/web-react/icon';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import GroupBasicPanel from "@/pages/group/basic";
import useForm from "@arco-design/web-react/es/Form/useForm";
import StatCreatePanel from "@/pages/stat/create/stat_create";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/update";
import {CiViewTable} from "react-icons/ci";
import {RiAppsLine} from "react-icons/ri";
import {Group} from "@/types/insights-web";
import {requestQueryById} from "@/api/group";
import {EditTableColumn} from "@/pages/common/edittable/EditTable";
import {getRandomString} from "@/utils/util";
import {HiMiniBoltSlash} from "react-icons/hi2";
const { Row, Col } = Grid;
import { RiShieldKeyholeLine } from "react-icons/ri";
import {GlobalErrorCodes} from "@/utils/constants";


export default function GroupManagePanel({projectInfo,groupId}) {
    const TabPane = Tabs.TabPane;
    const { Text } = Typography;
    const [showStatAddPanel, setShowsStatAddPanel] = useState(false);
    const [showGroupEditPanel, setShowGroupEditPanel] = useState(false);
    const [formParams,setFormParams] = useState(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [groupInfo,setGroupInfo] = useState<Group>(null);
    const [form] = useForm();
    const t = useLocale(locale);

    const handlerProcess = (action):void => {
        switch (action){
            case 'createStatistic':{
                setShowsStatAddPanel(true);
                break;
            }
            case 'updateGroup':{
                setShowGroupEditPanel(true);
                break;
            }
            default:{
                return;
            }
        }
    }

    const fetchData = async () => {
        setLoading(true);
        await requestQueryById({id:groupId}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setGroupInfo(data)
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }


    useEffect(() => {
        fetchData().then();
    },[groupId])

    const handlerSubmit = (input) => {
        setFormParams({"title":input,"groupId":groupId});
    }

    return (
        <>
            <Spin loading={loading} style={{ width: '100%' }}>
                <Row style={{marginBottom:'15px'}}>
                    <Typography.Text style={{fontSize:'14px',fontWeight:500}}>
                        <Button icon={<CiViewTable/>} shape={"circle"} size={"small"} style={{marginRight:'10px'}}/>
                        {t['groupManage.group']}：<Text copyable>{groupInfo?.token}</Text>
                    </Typography.Text>
                </Row>
                <Tabs
                    type="line"
                    tabPosition={"top"}
                    extra={
                        <Space size={5}>
                        <Button type={"primary"} onClick={() => handlerProcess('createStatistic')} size={"mini"} icon={<IconTag/>}>{t['groupManage.operations.button.create.stat']}</Button>
                        <Button type={"primary"} onClick={() => handlerProcess('updateGroup')} size={"mini"} icon={<CiViewTable/>} >{t['groupManage.operations.button.update.group']}</Button>
                        <Dropdown
                            position={"br"}
                            trigger={"click"}
                            droplist={
                                <Menu onClickMenuItem={handlerProcess} style={{ maxHeight:'280px' }}>
                                    <Menu.Item key={'limitedRecord'}>
                                        <Button type={"secondary"} shape={"circle"} size={"mini"} icon={<HiMiniBoltSlash/>} />&nbsp;&nbsp;
                                        {t['groupManage.operations.button.limited.records']}</Menu.Item>
                                    <Menu.Item key={'secretKey'}>
                                        <Button type={"secondary"} shape={"circle"} size={"mini"} icon={<RiShieldKeyholeLine/>} />&nbsp;&nbsp;
                                        {t['groupManage.operations.button.secret.key']}</Menu.Item>
                                    <Menu.Item key={'deleteGroup'}>
                                        <Button type={"secondary"} shape={"circle"} size={"mini"} icon={<IconDelete/>} />&nbsp;&nbsp;
                                        {t['groupManage.operations.button.delete.group']}</Menu.Item>
                                </Menu>
                            }>
                            <Button size={"mini"} type={"primary"}><IconDownCircle />More</Button>
                        </Dropdown>
                        </Space>
                    }>
                    <TabPane
                        key='1'
                        title={
                            <span style={{display:"inline-flex",alignItems:"center"}}>
                                <IconTags style={{ marginRight: 6 }} />
                                Statistic Items
                            </span>
                        }>
                        <Form
                            className={styles['search-form']}
                            labelAlign="left"
                            style={{marginTop:'10px'}}
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
                        <StatisticalListPanel formParams={formParams} from={"group-manage"}/>
                    </TabPane>
                    <TabPane key='3' title={
                        <span style={{display:"inline-flex",alignItems:"center"}}>
                            <CiViewTable style={{ marginRight: 6 }} />
                            Group Information
                      </span>}>
                        <GroupBasicPanel groupInfo={groupInfo}/>
                    </TabPane>
                </Tabs>
                {showStatAddPanel && <StatCreatePanel projectInfo={projectInfo} groupInfo={groupInfo} onClose={() => setShowsStatAddPanel(false)}/>}
                {showGroupEditPanel && <GroupEditPanel groupInfo={groupInfo} onClose={() => setShowGroupEditPanel(false)}/>}
            </Spin>
        </>);

}