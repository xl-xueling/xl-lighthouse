import React, {useEffect, useRef, useState} from 'react';
import {
    Form,
    Grid,
    Input,
    Message,
    Modal,
    Notification, PaginationProps,
    Radio, Table, TableColumnProps,
    Tabs,
    TreeSelect,
    Typography
} from "@arco-design/web-react";
import {Record, User} from "@/types/insights-web";
import {requestList} from "@/api/record";
import {RecordTypeEnum, ResultData} from "@/types/insights-common";
import {GlobalErrorCodes} from "@/utils/constants";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {formatTimeStampBackUp} from "@/utils/util";
import {translateRecord} from "@/pages/record/record";


export function LimitedRecordModal({resourceId,resourceType,recordTypes,onClose}){

    const t = useLocale(locale);
    const [loading, setLoading] = useState(true);
    const [recordData, setRecordData] = useState<Array<Record>>([]);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    interface LimitedRecord {
        id?:number,
        startTime?:number,
        endTime?:number,
        desc?:string,
    }

    const [limitedData,setLimitedData] = useState<LimitedRecord[]>(null);

    const [formParams, setFormParams] = useState({});

    const columns: TableColumnProps[] = [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Description',
            dataIndex: 'desc',
        },
    ];


    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const fetchData = async () => {
        const {current, pageSize} = pagination;
        const queryParams = {
            resourceId:resourceId,
            resourceType:resourceType,
            recordTypes:recordTypes,
        }
        console.log("queryParams is:" + JSON.stringify(queryParams));
        setLoading(true);
        await requestList({
            queryParams:queryParams,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((response:ResultData<{list:Array<Record>,total:number}>) => {
            const {code, data ,message} = response;
            if (code === '0') {
                const list = data.list;
                const records:LimitedRecord[] = [];
                list?.map(z => {
                    const limitRecord = translateRecord(t,z);
                    records.push(limitRecord);
                })
                setRecordData(records);
                setPagination({
                    ...pagination,
                    current,
                    pageSize,
                    total: data.total,
                })
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch(error => {
            console.log(error);
        }).finally(() => {
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchData().then();
    },[pagination.current, pagination.pageSize, JSON.stringify(formParams)])

    return (
        <Modal
            title= {'限流记录'}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            visible={true}
            onCancel={onClose}>
            <Table rowKey={'id'}
                   loading={loading}
                   size={"small"} pagination={pagination} columns={columns} data={recordData} onChange={onChangeTable} />
        </Modal>
    );
}