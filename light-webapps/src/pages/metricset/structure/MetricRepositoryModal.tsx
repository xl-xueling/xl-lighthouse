import React, {useContext, useEffect, useMemo, useRef, useState} from 'react';
import {
    Typography,
    Grid,
    Tabs,
    Notification, Modal, PaginationProps, Table, Button, Space
} from '@arco-design/web-react';
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
import {getRandomString} from "@/utils/util";
import {ResourceTypeEnum} from "@/types/insights-common";
import {MetricSetStructureContext} from "@/pages/metricset/structure/index";
import {treeCheckContainsNode} from "@/pages/department/common";
import {getResourceTypeDescription} from "@/desc/base";

export default function MetricSetRepositoryModal({id,onClose}) {

    const t = useLocale(locale);
    const [listData,setListData] = useState<Resource[]>([]);
    const [loading,setLoading] = useState<boolean>(false);
    const {listNodes,setListNodes} = useContext(MetricSetStructureContext);
    const {needSync,setNeedSync} = useContext(MetricSetStructureContext);
    const [refreshTime,setRefreshTime] = useState<number>(Date.now);

    const tableCallback = async (record, type) => {
        if(type == 'add'){
            const treeNode:TreeNode = {
                key:getRandomString(8),
                label:record.title,
                value:record.resourceId,
                type:record.resourceType == ResourceTypeEnum.Stat?'stat':null,
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

    const getColumns = (t: any,listNodes:TreeNode[], callback: (record: Record<string, any>, type: string) => Promise<void>) => {
        return [
            {
                title: t['repositoryModal.column.label.id'],
                dataIndex: 'resourceId',
                render: (value, record) =>
                    {value}
                ,
            },
            {
                title: t['repositoryModal.column.label.title'],
                dataIndex: 'title',
                render: (value, record) =>
                {value}
                ,
            },

            {
                title: t['repositoryModal.column.label.resourceType'],
                dataIndex: 'resourceType',
                render: (value, record) => {
                    return getResourceTypeDescription(t,value);
                },
            },
            {
                title: t['repositoryModal.column.label.relationShip'],
                dataIndex: 'fullTitle',
                render: (value, record) => {
                    if(record.resourceType == ResourceTypeEnum.Stat){
                        const array = value.split(";");
                        return array[0] +  '  >  ' + array[1];
                    }
                },
            },
            {
                title: t['repositoryModal.column.label.operations'],
                dataIndex: 'operations',
                render: (value, record) => {
                    let button;
                    let type;
                    if(record.resourceType == ResourceTypeEnum.Stat){
                        type = 'stat';
                    }else if(record.resourceType == ResourceTypeEnum.Project){
                        type = 'project';
                    }
                    if(!treeCheckContainsNode(listNodes,record.resourceId,type)){
                        button = <Button key={getRandomString()}
                                         type="text"
                                         onClick={() => callback(record,'add')}
                                         size="mini">
                            {t['repositoryModal.column.label.operations.add']}
                        </Button>;
                    }else{
                        button = <Button disabled={true} key={getRandomString()}
                                         type="secondary"
                                         size="mini">
                            {t['repositoryModal.column.label.operations.added']}
                        </Button>;
                    }
                    return  <Space key={getRandomString()} size={0} direction="horizontal">{[button]}</Space>;
                }
            }
        ];
    }

    const columns = useMemo(() => getColumns(t,listNodes, tableCallback), [t,refreshTime]);

    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

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
    },[id])

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
                loading={loading}
                pagination={pagination}
                columns={columns}
                data={listData}
            />
        </Modal>
    );
}