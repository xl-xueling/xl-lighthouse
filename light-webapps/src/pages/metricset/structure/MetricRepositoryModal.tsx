import React, {useContext, useEffect, useMemo, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {
    Card,
    Typography,
    Grid,
    Space,
    Tabs,
    Spin,
    Button, Notification, Modal, PaginationProps, Table
} from '@arco-design/web-react';
import styles from "./style/index.module.less";
import StatPreviewPanel from "@/pages/stat/preview/preview";
import StructurePanel from "@/pages/metricset/structure/structure";
import {MetricSetPreviewContext} from "@/pages/common/context";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import { MdOutlineNewLabel } from "react-icons/md";
import { RiDeleteBin3Line } from "react-icons/ri";
import {requestDeleteById} from "@/api/project";
import {requestResetStructure, requestIndicatorList, requestUpdateStructure} from "@/api/metricset";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Resource, TreeNode} from "@/types/insights-web";
import {getColumns} from "./constants";
import {getRandomString} from "@/utils/util";
import {ResourceTypeEnum} from "@/types/insights-common";
import {MetricSetStructureContext} from "@/pages/common/context";
import {treeCheckContainsNode} from "@/pages/department/base";

export default function MetricSetRepositoryModal({id,onClose}) {

    const t = useLocale(locale);
    const [listData,setListData] = useState<Resource[]>([]);
    const [loading,setLoading] = useState<boolean>(false);
    const {listNodes,setListNodes} = useContext(MetricSetStructureContext);
    const {needSync,setNeedSync} = useContext(MetricSetStructureContext);
    const [refreshTime,setRefreshTime] = useState<number>(Date.now);

    const tableCallback = async (record, type) => {
        if(type == 'add'){
            let itemDesc;
            if(record.resourceType == ResourceTypeEnum.Stat){
                itemDesc = 'stat';
            }else if(record.resourceType == ResourceTypeEnum.View){
                itemDesc = 'view';
            }
            const treeNode:TreeNode = {
                key:getRandomString(8),
                label:record.title,
                value:record.resourceId,
                type:itemDesc,
            }
            await addTreeNode(treeNode).then();
        }
    }

    const addTreeNode = async (treeNode) => {
        if(!treeCheckContainsNode(listNodes,treeNode.value,treeNode.type)){
            const children = listNodes[0].children;
            listNodes[0].children = [...children, treeNode];
            setListNodes(listNodes);
            setNeedSync(true);
            setRefreshTime(Date.now);
        }
    }

    const columns = useMemo(() => getColumns(t,listNodes, tableCallback), [t,refreshTime]);

    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15],
        sizeCanChange: false,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const fetchData = async () => {
        const {current, pageSize} = pagination;
        const requestParam = {
            queryParams:{
                id:id,
            },
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }
        setLoading(true);
        await requestIndicatorList(requestParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setListData(data.list);
                setPagination({
                    ...pagination,
                    current,
                    pageSize,
                    total: data.total});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        }).finally(() => {
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchData().then();
    },[id,pagination.current, pagination.pageSize])

    return (
        <Modal
            title= {t['repositoryModal.modal.title']}
            alignCenter={false}
            style={{ width:'1200px',maxWidth:'90%',top:'150px'}}
            visible={true}
            footer={null}
            onCancel={() => onClose()}
        >
            <Table
                rowKey={'resourceId'}
                style={{minHeight:'200px'}}
                size={"small"}
                onChange={onChangeTable}
                loading={loading}
                pagination={pagination}
                columns={columns}
                data={listData}
            />
        </Modal>
    );
}