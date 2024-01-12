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
import {ResultData} from "@/types/insights-common";
import {GlobalErrorCodes} from "@/utils/constants";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/user/list/locale";
import {formatTimeStampBackUp} from "@/utils/util";


export function RecordModal({resourceId,resourceType,recordType,onClose}){

    const t = useLocale(locale);
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
    const [loading, setLoading] = useState(true);
    const [formParams, setFormParams] = useState({});

    const columns: TableColumnProps[] = [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'StartTime',
            dataIndex: 'startTime',
            render: (value) => {return formatTimeStampBackUp(value)},
        },
        {
            title: 'EndTime',
            dataIndex: 'endTime',
            render: (value) => {return formatTimeStampBackUp(value)},
        },
        {
            title: 'Description',
            dataIndex: 'desc',
        },
    ];

    const data = [
        {
            key: '1',
            name: 'Jane Doe',
            salary: 23000,
            address: '32 Park Road, London',
            email: 'jane.doe@example.com',
        },
        {
            key: '2',
            name: 'Alisa Ross',
            salary: 25000,
            address: '35 Park Road, London',
            email: 'alisa.ross@example.com',
        },
        {
            key: '3',
            name: 'Kevin Sandra',
            salary: 22000,
            address: '31 Park Road, London',
            email: 'kevin.sandra@example.com',
        },
        {
            key: '4',
            name: 'Ed Hellen',
            salary: 17000,
            address: '42 Park Road, London',
            email: 'ed.hellen@example.com',
        },
        {
            key: '5',
            name: 'William Smith',
            salary: 27000,
            address: '62 Park Road, London',
            email: 'william.smith@example.com',
        },
    ];

    function onChangeTable({ current, pageSize }) {
        console.log("current is:" + current + ",pageSize:" + pageSize)
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const fetchData = async () => {
        const {current, pageSize} = pagination;
        await requestList({
            queryParams:formParams,
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
                    const limitRecord:LimitedRecord = {
                        id:z.id,
                        startTime:z.recordTime,
                        endTime:z.recordTime,
                    }
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
        console.log("pagination is:" + JSON.stringify(pagination));
        fetchData().then();
    },[pagination.current, pagination.pageSize, JSON.stringify(formParams)])

    return (
        <Modal
            title= {'限流记录'}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            visible={true}
            onCancel={onClose}>
            <Table rowKey={'id'} pagination={pagination} columns={columns} data={recordData} onChange={onChangeTable} />
        </Modal>
    );
}