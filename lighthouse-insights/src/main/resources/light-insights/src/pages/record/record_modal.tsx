import React, {useEffect, useRef, useState} from 'react';
import {
    Form,
    Grid,
    Input,
    Message,
    Modal,
    Notification, PaginationProps,
    Radio,
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

    const [loading, setLoading] = useState(true);
    const [formParams, setFormParams] = useState({});

    const fetchData = async () => {
        const promiseOfFetchUserData:Promise<Array<User>> = new Promise((resolve,reject) => {
            setLoading(true);
            const proc = async () => {
                let result = [];
                const {current, pageSize} = pagination;
                await requestList({
                    queryParams:formParams,
                    pagination:{
                        pageSize:pageSize,
                        pageNum:current,
                    }
                }).then((response:ResultData) => {
                    console.log("record response is:" + JSON.stringify(response))
                    const {code, data ,message} = response;
                    if (code === '0') {
                        result = data.list;
                        const {current, pageSize} = pagination;
                        setPagination({
                            ...pagination,
                            current,
                            pageSize,
                            total: data.total,
                        })
                        resolve(result);
                    }else{
                        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                    }
                }).catch(error => {
                    console.log(error);
                }).finally(() => {
                    setLoading(false);
                })
            }
            proc().then();
        })

        const promiseAll:Promise<[Array<User>]> = Promise.all([
            promiseOfFetchUserData
        ]);

        promiseAll.then(([result]) => {
            setRecordData(result);
        })
    }

    useEffect(() => {
        console.log("Limited Record.......")
        fetchData().then();
    },[])

    return (
        <Modal
            title= {'限流记录'}
            style={{ width:'960px',verticalAlign:'top', marginTop: '130px' }}
            visible={true}
            onCancel={onClose}>
            ssssssss
        </Modal>
    );
}