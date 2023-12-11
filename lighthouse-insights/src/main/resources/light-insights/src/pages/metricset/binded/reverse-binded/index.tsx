import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card, Table, TableColumnProps, Space, Modal, Message, Link, Spin
} from '@arco-design/web-react';
import {
    IconCalendar, IconClockCircle,
    IconDownCircle, IconPlus, IconStar, IconTag, IconThunderbolt, IconUser
} from '@arco-design/web-react/icon';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import styles from './style/index.module.less';
import GroupBasicPanel from "@/pages/group/basic";
import useForm from "@arco-design/web-react/es/Form/useForm";
import StatAddPanel from "@/pages/stat/add/stat_add";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/edit";
import BindedProject from "@/pages/metricset/binded/binded/binded_project";
import ProjectTree from "@/pages/project/common/project-tree";
import GroupManagePanel from "@/pages/group/manage";
import GroupAddPanel from "@/pages/group/add/group_add";
import MetricTree from "@/pages/metricset/common/tree";
import ProjectDisplay from "@/pages/project/display";
import {translate} from "@/pages/department/common";
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import { GoGitMerge } from "react-icons/go";
import { GoStack } from "react-icons/go";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {requestBinded, requestExtendInfoByIds, requestPinList} from "@/api/metricset";
import {ArcoTreeNode, ExtendMetricSet, MetricSet} from "@/types/insights-web";
import locale from "./locale";
import {requestCreate} from "@/api/project";

export default function ReverseBindedPanel({projectId = 0,statId = 0,onClose}) {

    const t = useLocale(locale);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [treeData, setTreeData] = useState([]);
    const [loading,setLoading] = useState(true);
    const [confirmLoading,setConfirmLoading] = useState(false);
    const formRef = useRef(null);

    const fetchMetricPinList = async () => {
        return new Promise<Array<MetricSet>>((resolve,reject) => {
            requestPinList().then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    const fetchMetricExtendInfo = async (ids) => {
        return new Promise<Record<number, ExtendMetricSet>>(((resolve, reject) => {
            requestExtendInfoByIds(ids).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        }))
    }


    function translate(item, level = 1) {
        if (!item) {
            return null;
        }
        const newNode:ArcoTreeNode = {"key":String(item.key),"title":item.title,"icon":(level == 0)?<GoGitMerge/>:<GoStack/>};
        if (level >= 2) {
            newNode.children = [];
        } else {
            if (item.children && item.children.length > 0) {
                item.children = item.children.map((child) =>
                    translate(child, level + 1)
                );
            }
        }
        return newNode;
    }

    async function handlerSubmit(){
        const relationId = formRef.current.getFieldValue("relationId");
        let metricId;
        let nodeId;
        if (relationId.includes('-')) {
            const arr = relationId.split('-');
            metricId = Number(arr[0]);
            nodeId = Number(arr[1]);
        } else {
            metricId = Number(relationId);
            nodeId = 0;
        }
        requestBinded({"projectIds":[projectId],"metricId":metricId,"nodeId":nodeId}).then((result) => {
            if(result.code === '0'){
                Message.success(t['projectCreate.form.submit.success']);
                setTimeout(() => {
                    window.location.href = "/project/list";
                },3000)
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        }).finally(() => {
            setLoading(false);
        })
    }

    useEffect(() => {
        setLoading(true);
        fetchMetricPinList()
            .then((ids) => fetchMetricExtendInfo(ids))
            .then((response) => {
                const treeData = Object.entries(response).map(([key, value]) => {
                    return translate(value.structure);
                })
                setTreeData(treeData);
            }).catch((error) => {
                console.error(error);
            }).finally(() => {
                setLoading(false);
        })
    },[])

    return (
        <Modal
            title={t['reverseBinded.modal.title']}
            visible={true}
            style={{ width:'600px',minHeight:'350px'}}
            onOk={handlerSubmit}
            onCancel={onClose}
        >
            <Spin loading={loading} size={20} style={{ display: 'block' }}>
                <Form ref={formRef} wrapperCol={{ span: 24 }}>
                    <Form.Item field="relationId" >
                        <TreeSelect
                            renderFormat={(nodeProps, value) => {
                                return <div><IconStar />value</div>;
                            }}
                            placeholder={"Select MetricSet"}
                            multiple={false}
                            showSearch={true}
                            allowClear={true}
                            treeData={treeData}
                        />
                    </Form.Item>
                </Form>
            </Spin>
        </Modal>
    );
}