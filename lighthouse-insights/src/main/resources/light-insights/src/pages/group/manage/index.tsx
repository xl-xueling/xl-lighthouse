import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, Message,
} from '@arco-design/web-react';
import {
    IconDownCircle, IconTags,
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
const { Row, Col } = Grid;

export default function GroupManagePanel({groupId}) {
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


    useEffect(() => {
        setFormParams({"groupId":groupId});
        const promiseAll:Promise<[Group]> = Promise.all([
            promiseFetchGroupInfo,
        ])

        promiseAll.then((results) => {
            const groupInfo = results[0];
            setGroupInfo(groupInfo);
            // const columnArr:Array<EditTableColumn> = [];
            // for(let i=0;i<groupInfo.columns.length;i++){
            //     const columnInfo = groupInfo.columns[i];
            //     columnArr.push({...columnInfo,"key":getRandomString(),"editable":false})
            // }
            // setInitData(columnArr);
        }).catch(error => {
            console.log(error);
            Message.error(t['system.error']);
        }).finally(() => {
            setLoading(false);
        });
    },[groupId])

    const handlerSubmit = (input) => {
        setFormParams({"title":input,"groupId":groupId});
    }

    return (
        <>
            <Row style={{marginBottom:'15px'}}>
                <Typography.Text style={{fontSize:'14px',fontWeight:500}}>
                    <Button icon={<CiViewTable/>} shape={"circle"} size={"small"} style={{marginRight:'10px'}}/>
                    统计组：{'homepage_behavior_stat1'}
                </Typography.Text>
            </Row>
            <Tabs
                type="line"
                tabPosition={"top"}
                extra={
                    <Dropdown
                        position={"br"}
                        trigger={"click"}
                        droplist={
                            <Menu onClickMenuItem={handlerProcess} style={{ maxHeight:'280px' }}>
                                <Menu.Item key='1'>{t['groupManage.operations.button.create.stat']}</Menu.Item>
                                <Menu.Item key='2'>{t['groupManage.operations.button.update.group']}</Menu.Item>
                                <Menu.Item key='3'>{t['groupManage.operations.button.update.limited.threshold']}</Menu.Item>
                                <Menu.Item key='4'>{t['groupManage.operations.button.limited.records']}</Menu.Item>
                                <Menu.Item key='5'>{t['groupManage.operations.button.secret.key']}</Menu.Item>
                                <Menu.Item key='6'>{t['groupManage.operations.button.delete.group']}</Menu.Item>
                            </Menu>
                        }>
                        <Button size={"small"} type={"secondary"}><IconDownCircle />{t['groupManage.operations.button.title']}</Button>
                    </Dropdown>
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
                    <StatisticalListPanel formParams={formParams} from={"group-manage"} />
                </TabPane>
                <TabPane key='3' title={
                    <span style={{display:"inline-flex",alignItems:"center"}}>
                        <CiViewTable style={{ marginRight: 6 }} />
                        Group Information
                  </span>}>
                    <GroupBasicPanel groupId={groupId}/>
                </TabPane>
            </Tabs>
            {showStatAddPanel && <StatCreatePanel groupInfo={groupInfo} onClose={() => setShowsStatAddPanel(false)}/>}
            {showGroupEditPanel && <GroupEditPanel groupId={groupInfo.id} onClose={() => setShowGroupEditPanel(false)}/>}
        </>);

}