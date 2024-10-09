import React, {useContext, useEffect, useMemo, useRef, useState} from 'react';
import {
    Button,
    Card,
    Grid,
    Notification,
    PaginationProps,
    Table,
    TableColumnProps,
    Typography
} from "@arco-design/web-react";
import {VscTools} from "react-icons/vsc";
import Text from "@arco-design/web-react/es/Typography/text";
const { Row, Col } = Grid;
import { IoAdd } from "react-icons/io5";
import AuthAdd from "@/pages/caller/manage/auth_add";
import {CallerManageContext} from "@/pages/common/context";
import {requestAuthList} from "@/api/caller";
import {Resource} from "@/types/insights-web";
import {OwnerTypeEnum} from "@/types/insights-common";
import {getRandomString} from "@/utils/util";
import {getColumns} from "@/pages/caller/manage/Constants";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/caller/manage/locale";
import {AuthRecord} from "@/types/caller";
import AuthExtension from "@/pages/caller/manage/auth_extension";

export default function AuthList({}){

    const {callerInfo} = useContext(CallerManageContext);
    const t = useLocale(locale);

    const [showAddAuthModal,setShowAddAuthModal] = useState<boolean>(false);
    const [listData,setListData] = useState<AuthRecord[]>([]);
    const [loading,setLoading] = useState<boolean>(false);
    const [showExtensionModal,setShowExtensionModal] = useState<boolean>(false);
    const [currentItem,setCurrentItem] = useState<AuthRecord>(null);
    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [15,30],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const fetchData = async (): Promise<void> => {
        const {current, pageSize} = pagination;
        const combineParams:any = {}
        combineParams.ownerId = callerInfo?.id;
        combineParams.ownerType = OwnerTypeEnum.CALLER;
        await requestAuthList({
            queryParams:combineParams,
            pagination:{
                pageSize:10,
                pageNum:1,
            }
        }).then((response) => {
            const {code, data ,message} = response;
            if (code === '0') {
                setListData(data.list);
                setPagination({
                    ...pagination,
                    current,
                    pageSize,
                    total: data.total});
                setLoading(false);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    useEffect(() => {
        fetchData().then();
    },[callerInfo])

    const tableCallback = async (record, type) => {
        if(type == 'extension'){
            setCurrentItem(record);
            setShowExtensionModal(true);
        }else if(type == 'remove'){

        }
    }

    const columns = useMemo(() => getColumns(t,tableCallback), [t]);

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    return (
        <Card>
            <Row style={{marginBottom:'10px'}}>
                <Grid.Col span={15}>
                </Grid.Col>
                <Grid.Col span={9} style={{textAlign:"right" }}>
                    <Button type={'primary'} size={"mini"} onClick={() => setShowAddAuthModal(true)}>{t['callerAuthList.button.addAuth']}</Button>
                </Grid.Col>
            </Row>
            <Table rowKey={(v) => {
                return v.resourceId + '-' + v.resourceType;
            }} size={"small"} columns={columns} data={listData} pagination={pagination} onChange={onChangeTable}/>
            {showAddAuthModal && <AuthAdd onClose={() => {setShowAddAuthModal(false)}} />}
            {showExtensionModal && <AuthExtension authRecord={currentItem} onClose={() => setShowExtensionModal(false)} />}
        </Card>
    );
}