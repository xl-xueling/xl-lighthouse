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
import {requestBinded, requestPinList} from "@/api/metricset";
import {ArcoTreeNode, Department, MetricSet} from "@/types/insights-web";
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
    const [selectedItems,setSelectedItems] = useState<MetricSet[]>([]);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const pinMetricsInfo = useSelector((state: {pinMetricsInfo:Array<MetricSet>}) => state.pinMetricsInfo);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 10,
        current: 1,
        pageSizeChangeResetCurrent: true,
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

    const removeSelected = (id) => {
        const values = selectedItems.filter(x => x.id != id);
        setSelectedItems(values);
    }

    const columns = useMemo(() => getColumns(t, selectedItems,tableCallback), [t,selectedItems]);

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
        const bindParams = {
            bindElements:[{id:bindElement.id,type:bindElement.type}],
            metricIds:selectedItems.map(z => z.id),
        }
        setLoading(true);
        requestBinded(bindParams).then((result) => {
            if(result.code === '0'){
                Message.success(t['reverseBinded.form.submit.success']);
                setTimeout(() => {
                    setLoading(false);
                    onClose();
                },1000)
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        })
    }

    const fetchData = async (): Promise<void> => {
        const {current, pageSize} = pagination;
        const combineParam = {
            ownerId:userInfo?.id,
        }
        await requestList({
            queryParams:combineParam,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((response) => {
            console.log("result is:" + JSON.stringify(response));
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
    },[])


    return (
        <Modal
            title={t['reverseBinded.modal.title']}
            visible={true}
            style={{ width:'950px',minHeight:'450px',maxHeight:'800px'}}
            onOk={handlerSubmit}
            onCancel={onClose}
        >
            <Spin loading={loading} size={20} style={{ display: 'block' }}>
                <Input.Search placeholder={'Search'} allowClear style={{width:'320px',marginLeft:'3px',marginBottom:'15px'}}/>
                <Table size={"small"} rowKey={'id'} columns={columns} data={metricSetList} />
                已选择：<Space size='large'>
                {
                    selectedItems?.map(z => {
                        return <Tag key={z.id} size='small' closable onClose={() => removeSelected(z.id)}>
                            {z.title}
                        </Tag>
                    })
                }
            </Space>
            </Spin>
        </Modal>
    );
}