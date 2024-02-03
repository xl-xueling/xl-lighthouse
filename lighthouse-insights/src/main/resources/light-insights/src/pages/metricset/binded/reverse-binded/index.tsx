import {
    Button,
    Form,
    Grid, Input,
    Message,
    Modal,
    Notification,
    PaginationProps, Space,
    Spin, Table, TableColumnProps,
    Tabs, Tag,
    TreeSelect
} from '@arco-design/web-react';
import React, {useEffect, useMemo, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import {GoGitMerge, GoStack} from "react-icons/go";
import {useSelector} from "react-redux";
import {GlobalState} from "@/store";
import {requestBinded, requestStarList} from "@/api/metricset";
import {ArcoTreeNode, Department, MetricSet, TreeNode} from "@/types/insights-web";
import locale from "./locale";
import {requestList} from "@/api/metricset";
import {getColumns} from "./constants";
import {BindElement} from "@/types/insights-common";

const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

export default function ReverseBindedPanel({bindElement,onClose}) {

    const t = useLocale(locale);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [metricSetList, setMetricSetList] = useState([]);
    const [loading,setLoading] = useState(true);
    const [confirmLoading,setConfirmLoading] = useState(false);
    const formRef = useRef(null);
    const [formParams, setFormParams] = useState<any>({});
    const [selectedItems,setSelectedItems] = useState<MetricSet[]>([]);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: false,
        showTotal: true,
        pageSize: 10,
        current: 1,
        pageSizeChangeResetCurrent: false,
    });

    const tableCallback = async (type,record) => {
        if(type == "select"){
            const selectedKeys = selectedItems.map(z => z.id);
            if(!selectedKeys.includes(record.id)){
                const values = selectedItems.concat(record);
                setSelectedItems(values);
            }
        }
    }

    const handlerSearch = (search) => {
        setPagination({ ...pagination, current: 1 });
        setFormParams({search});
    }

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const removeSelected = (id) => {
        const values = selectedItems.filter(x => x.id != id);
        setSelectedItems(values);
    }

    const columns = useMemo(() => getColumns(t, bindElement,selectedItems,tableCallback), [t,bindElement,selectedItems]);

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
        if(selectedItems.length == 0){
            Notification.warning({style: { width: 420 }, title: 'Warning', content: t['reverseBinded.form.submit.selectAtLeastOne']});
            return;
        }
        const bindParams = {
            bindElements:[{resourceId:bindElement.resourceId,resourceType:bindElement.resourceType}],
            metricIds:selectedItems.map(z => z.id),
        }
        setConfirmLoading(true);
        requestBinded(bindParams).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['reverseBinded.form.submit.success']});
                onClose();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        }).finally(() => {
            setConfirmLoading(false);
        })
    }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const {current, pageSize} = pagination;
        const requestParams = {
            ownerId:userInfo?.id,
            search:formParams.search,
        }
        await requestList({
            queryParams:requestParams,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setMetricSetList(data.list);
                setPagination({
                    ...pagination,
                    current,
                    pageSize,
                    total: data.total});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    };

    useEffect(() => {
        fetchData().then();
    },[pagination.current, pagination.pageSize,JSON.stringify(formParams)])


    return (
        <Modal
            title={t['reverseBinded.modal.title']}
            visible={true}
            confirmLoading={confirmLoading}
            style={{ width:'1200px',maxWidth:'90%',minHeight:'600px',maxHeight:'800px'}}
            onOk={handlerSubmit}
            onCancel={onClose}>
                <Input.Search placeholder={'Search'} allowClear style={{width:'320px',marginLeft:'3px',marginBottom:'15px'}} onSearch={handlerSearch}/>
                <Table size={"mini"} style={{height:'300px'}}
                       loading={loading} rowKey={'id'}
                       onChange={onChangeTable} columns={columns} data={metricSetList} pagination={pagination} />
            {t['reverseBinded.currently.selected']}{'：'}<Space size='large'>
                {
                    selectedItems?.map(z => {
                        return <Tag key={z.id} size='small' closable onClose={() => removeSelected(z.id)}>
                            {z.title}
                        </Tag>
                    })
                }
            </Space>
        </Modal>
    );
}