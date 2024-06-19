import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, Message, Space, Divider, Notification, Spin, Skeleton, Popconfirm, Modal,
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
import {requestDeleteById, requestQueryById} from "@/api/group";
import {HiMiniBoltSlash} from "react-icons/hi2";
const { Row, Col } = Grid;
import { RiShieldKeyholeLine } from "react-icons/ri";
import GroupUpdatePanel from "@/pages/group/update";
import SecretKeyModal from "@/pages/group/basic/secret_key";
import {LimitingSettingsModal} from "@/pages/group/limiting/LimitingSettingsModal";
import {GroupManageContext} from "@/pages/common/context";

export default function GroupManagePanel({projectInfo,groupId,deleteCallback}) {
    const TabPane = Tabs.TabPane;
    const { Text } = Typography;
    const [showStatAddPanel, setShowsStatAddPanel] = useState(false);
    const [showGroupEditPanel, setShowGroupEditPanel] = useState(false);
    const [showLimitingManagerPanel, setShowLimitingManagerPanel] = useState(false);
    const [showSecretKeyModal, setShowSecretKeyModal] = useState(false);
    const [formParams,setFormParams] = useState(null);
    const [loading,setLoading] = useState<boolean>(true);
    const [groupInfo,setGroupInfo] = useState<Group>(null);
    const [form] = useForm();
    const t = useLocale(locale);

    const callback = async (operation,data) => {
        switch (operation){
            case "update-group":
                setGroupInfo(data);
                break;
            default:
                break;
        }
    }

    const handlerProcess = async (action):Promise<void> => {
        switch (action){
            case 'createStatistic':{
                setShowsStatAddPanel(true);
                break;
            }
            case 'updateGroup':{
                setShowGroupEditPanel(true);
                break;
            }
            case 'limitedRecord':{
                setShowLimitingManagerPanel(true);
                break;
            }
            case 'secretKey': {
                setShowSecretKeyModal(true);
                break;
            }
            case 'deleteGroup':{
                setShowDeleteGroupConfirm(true);
                break;
            }
            default:{
                return;
            }
        }
    }

    const handlerStatDeleteCallback = (statId) => {
        fetchData().then();
    }

    const handlerDeleteGroup = async () => {
        setLoading(true);
        await requestDeleteById({id:groupId}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['groupManage.form.submit.deleteSuccess']});
                deleteCallback('deleteGroup',groupId);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
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

    const [showDeleteGroupConfirm,setShowDeleteGroupConfirm] = useState<boolean>(false);

    const deleteGroupConfirm = (
        <Modal
            title={t['basic.modal.confirm.delete.title']}
            visible={showDeleteGroupConfirm}
            onOk={() => {handlerDeleteGroup();setShowDeleteGroupConfirm(false)}}
            alignCenter={false}
            style={{top:'200px'}}
            onCancel={() => setShowDeleteGroupConfirm(false)}
            okText={t['basic.form.button.yes']}
            cancelText={t['basic.form.button.no']}
        >
            {t['groupManage.operations.delete.confirm']}
        </Modal>
    );

    useEffect(() => {
        fetchData().then();
        setFormParams({"groupIds":[groupId]});
    },[groupId])

    const handlerSubmit = (input) => {
        setFormParams({"search":input,"groupIds":[groupId]});
    }

    const createStatSuccess = () => {
        setFormParams({"groupIds":[groupId],t:new Date().getTime()});
        fetchData().then();
    }

    return (
        <>
            <GroupManageContext.Provider value={handlerStatDeleteCallback}>
            <Spin loading={loading} style={{width: '100%'}}>
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
                                    {t['groupManage.operations.button.limiting.settings']}</Menu.Item>
                                <Menu.Item key={'secretKey'}>
                                    <Button type={"secondary"} shape={"circle"} size={"mini"} icon={<RiShieldKeyholeLine/>} />&nbsp;&nbsp;
                                    {t['groupManage.operations.button.secret.key']}</Menu.Item>
                                <Menu.Item key={'deleteGroup'}>
                                    <Button type={"secondary"} shape={"circle"} size={"mini"} icon={<IconDelete/>} />&nbsp;&nbsp;
                                    {t['groupManage.operations.button.delete.group']}
                                </Menu.Item>
                            </Menu>
                        }>
                        <Button size={"mini"} type={"primary"}><IconDownCircle />{t['groupManage.operations.more']}</Button>
                    </Dropdown>
                        {deleteGroupConfirm}
                    </Space>
                }>
                <TabPane
                    key='1'
                    title={
                        <span style={{display:"inline-flex",alignItems:"center"}}>
                            <IconTags style={{ marginRight: 6 }} />
                            {t['groupManage.label.statisticList']}
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
                                    <Input.Search autoComplete={"off"} placeholder={'Search Title'} allowClear onSearch={(v) => {
                                        handlerSubmit(v);
                                    }} />
                                </Form.Item>
                            </Col>
                        </Row>
                    </Form>
                    <StatisticalListPanel formParams={formParams} from={"group-manage"} parentLoading={loading}/>
                </TabPane>
                <TabPane key='3' title={
                    <span style={{display:"inline-flex",alignItems:"center"}}>
                        <CiViewTable style={{ marginRight: 6 }} />
                        {t['groupManage.label.groupInformation']}
                  </span>}>
                    <GroupBasicPanel groupInfo={groupInfo}/>
                </TabPane>
            </Tabs>
            {showStatAddPanel && <StatCreatePanel projectInfo={projectInfo} groupInfo={groupInfo} onClose={() => {
                setShowsStatAddPanel(false);
            }} onSuccess={() => {createStatSuccess()}}/>}
            {showGroupEditPanel && <GroupUpdatePanel groupInfo={groupInfo} onClose={() => setShowGroupEditPanel(false)} callback={callback}/>}
            {showLimitingManagerPanel && <LimitingSettingsModal groupInfo={groupInfo} onClose={() => setShowLimitingManagerPanel(false)}/>}
            {showSecretKeyModal && <SecretKeyModal groupId={groupInfo?.id} onClose={() => setShowSecretKeyModal(false)}/>}
            </Spin>
            </GroupManageContext.Provider>
        </>);

}