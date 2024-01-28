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
import StatPreviewPanel from "@/pages/stat/display/preview";
import StructurePanel from "@/pages/metricset/structure/structure";
import {MetricSetPreviewContext} from "@/pages/metricset/preview";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import { MdOutlineNewLabel } from "react-icons/md";
import { RiDeleteBin3Line } from "react-icons/ri";
import {requestDeleteById} from "@/api/project";
import {requestResetStructure, requestStructurePendList, requestUpdateStructure} from "@/api/metricset";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Resource} from "@/types/insights-web";
import {getColumns} from "./constants";

export default function MetricSetPendAddModal({id,onClose}) {

    const t = useLocale(locale);

    const [listData,setListData] = useState<Resource[]>([]);
    const [loading,setLoading] = useState<boolean>(false);

    const tableCallback = async (record, type) => {
        console.log("record:" + record + ",type:" + type);
    }

    const columns = useMemo(() => getColumns(t, tableCallback), [t,listData]);

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
        await requestStructurePendList(requestParam).then((response) => {
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
            title= {'待添加元素'}
            style={{ width:'1200px',maxWidth:'90%'}}
            visible={true}
            onCancel={() => onClose()}
        >
            <Table
                style={{minHeight:'200px'}}
                rowKey={'resourceId' + 'resourceType'}
                size={"small"}
                loading={loading}
                pagination={pagination}
                columns={columns}
                data={listData}
            />
        </Modal>
    );
}