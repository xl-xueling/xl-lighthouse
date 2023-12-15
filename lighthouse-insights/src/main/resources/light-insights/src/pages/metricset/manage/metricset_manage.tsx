import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card
} from '@arco-design/web-react';
import {
    IconDashboard, IconDownCircle, IconTag, IconThunderbolt
} from '@arco-design/web-react/icon';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import GroupBasicPanel from "@/pages/group/basic";
import useForm from "@arco-design/web-react/es/Form/useForm";
import StatAddPanel from "@/pages/stat/add/stat_add";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/update";
import BindedList from "@/pages/metricset/binded/list/binded";
const { Row, Col } = Grid;

export default function MetricManagePanel({groupId}) {
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
            <Tabs
                tabPosition={"left"}
                type="line">
                <TabPane
                    key='1'
                    title={
                        <span>
                            <IconDashboard style={{ marginRight: 6 }} />
                            Data View
                        </span>
                    }>
                </TabPane>
                <TabPane
                    key='2'
                    title={
                        <span>
                            <IconThunderbolt style={{ marginRight: 6 }} />
                            Binded Items
                        </span>
                    }>
                   <BindedList metricId={0}/>
                </TabPane>
                <TabPane key='3' title={
                    <span>
                        <IconTag style={{ marginRight: 6 }} />
                        Authority Info
                  </span>}>
                    <GroupBasicPanel groupId={groupId}/>
                </TabPane>
            </Tabs>
    );

}