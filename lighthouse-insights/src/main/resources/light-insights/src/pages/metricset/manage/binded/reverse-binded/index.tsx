import {
    Typography,
    Grid,
    Button,
    Form,
    Input,
    Tabs,
    Dropdown, Menu, TreeSelect, Card, Table, TableColumnProps, Space, Modal, Message, Link
} from '@arco-design/web-react';
import {
    IconCalendar, IconClockCircle,
    IconDownCircle, IconPlus, IconStar, IconTag, IconThunderbolt, IconUser
} from '@arco-design/web-react/icon';
import React, {useEffect, useState} from 'react';
import useLocale from '@/utils/useLocale';
import styles from './style/index.module.less';
import GroupBasicPanel from "@/pages/group/basic";
import useForm from "@arco-design/web-react/es/Form/useForm";
import StatAddPanel from "@/pages/stat/add/stat_add";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import GroupEditPanel from "@/pages/group/edit";
import BindedProject from "@/pages/metricset/manage/binded/add/binded_project";
import ProjectTree from "@/pages/project/common/project-tree";
import GroupManagePanel from "@/pages/group/manage";
import GroupAddPanel from "@/pages/group/add/group_add";
import MetricTree from "@/pages/metricset/common/tree";
import ProjectDisplay from "@/pages/project/display";
import {translate} from "@/pages/department/common";
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import { MdOutlineInsights } from "react-icons/md";
import { BiSolidNetworkChart } from "react-icons/bi";
import { RiGitBranchFill } from "react-icons/ri";
import { CiGrid42 } from "react-icons/ci";
import { GoGitMerge } from "react-icons/go";

import { GoStack } from "react-icons/go";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {requestDeleteById} from "@/api/project";
import {requestExtendInfoByIds, requestPinList} from "@/api/metricset";
import {ArcoTreeNode, Department, ExtendMetricSet, MetricSet} from "@/types/insights-web";

export default function BindedReversePanel({onClose}) {

    const userInfo = useSelector((state: GlobalState) => state.userInfo);

    const [treeData, setTreeData] = useState([]);

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

    useEffect(() => {
        fetchMetricPinList()
            .then((ids) => fetchMetricExtendInfo(ids))
            .then((response) => {
                const treeData = Object.entries(response).map(([key, value]) => {
                    return translate(value.structure);
                })
                setTreeData(treeData);
            }).catch((error) => {
                console.error(error);
            });
    },[])

    return (
        <Modal
            title={'绑定到指标集'}
            visible={true}
            style={{ width:'500px',height:'400x'}}
            onCancel={onClose}
        >
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
        </Modal>
    );
}