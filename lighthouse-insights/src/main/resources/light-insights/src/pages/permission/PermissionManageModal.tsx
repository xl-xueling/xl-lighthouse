import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Collapse, Divider,
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
import {Permission, Record, User} from "@/types/insights-web";
import {requestList} from "@/api/record";
import {OwnerTypeEnum, RecordTypeEnum, ResourceTypeEnum, ResultData, RoleTypeEnum} from "@/types/insights-common";
import {GlobalErrorCodes} from "@/utils/constants";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {formatTimeStampBackUp} from "@/utils/util";
import {LimitedRecord, translateRecord} from "@/pages/record/record";
import {requestQueryList} from "@/api/permission";
import {getColumns} from "./constants";
const CollapseItem = Collapse.Item;

export function PermissionManageModal({resourceId,resourceType,onClose}){

    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(true);

    const [departListData,setDepartListData] = useState<Permission[]>([]);

    const [userListData,setUserListData] = useState<Permission[]>([]);

    const [adminListData,setAdminListData] = useState<Permission[]>([]);

    const [departPermissionForms,setDepartPermissionForms] = useState(null);

    const [userPermissionForms,setUserPermissionForms] = useState(null);

    const tableCallback = async (record, type) => {
        console.log("tableCallBack,record:" + record + ",type:" + type);
    }
    const columns = useMemo(() => getColumns(t,tableCallback), [t]);

    const [pagination1, setPagination1] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });

    const [pagination2, setPagination2] = useState<PaginationProps>({
        sizeOptions: [15,20,30,50],
        sizeCanChange: true,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    });


    const fetchDepartListData = async () => {
        const {current, pageSize} = pagination1;
        let roleType;
        if(resourceType == ResourceTypeEnum.Project){
            roleType = RoleTypeEnum.PROJECT_ACCESS_PERMISSION;
        }else if(resourceType == ResourceTypeEnum.Metric){
            roleType = RoleTypeEnum.METRIC_ACCESS_PERMISSION;
        }else{
            throw new Error();
        }
        const requestParam = {
            resourceId:resourceId,
            roleType:roleType,
            ownerType:OwnerTypeEnum.DEPARTMENT,
        }
        console.log("requestParam is:" + JSON.stringify(requestParam))
        await requestQueryList({
            queryParams:requestParam,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
            }
        }).then((result) => {
            console.log("result:" + JSON.stringify(result));
        }).catch((error) => {
            console.log(error)
        }).finally(() => {
            console.log("sss")
        })
    }

    // const fetchUserListData = async () => {
    //
    // }
    //
    // const fetchAdminListData = async () => {
    //
    // }

    useEffect(() => {
        console.log("resourceId:" + resourceId + ",resourceType:" + resourceType)
        fetchDepartListData().then();
    },[])


    return (
        <Modal
            title= {'权限管理'}
            style={{ width:'1180px',verticalAlign:'top', maxWidth:'90%', marginTop: '130px' }}
            visible={true}
            onCancel={onClose}>

            <Collapse
                defaultActiveKey={['1', '2']}
            >
                <CollapseItem header='部门访问权限' name='1'>
                    <Table size={"small"} pagination={pagination1} columns={columns} data={departListData} />
                </CollapseItem>
                <CollapseItem header='用户访问权限' name='2'>
                    <Table size={"small"} pagination={pagination1} columns={columns} data={userListData} />
                </CollapseItem>

                <CollapseItem header='管理员权限' name='3'>
                    ssasg
                </CollapseItem>
            </Collapse>
        </Modal>
    );
}