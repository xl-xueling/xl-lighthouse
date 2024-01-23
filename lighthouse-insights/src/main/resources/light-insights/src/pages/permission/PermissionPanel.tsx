import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Button,
    Collapse,
    Form,
    Grid,
    Input,
    Notification,
    PaginationProps,
    Space,
    Table,
    Tabs
} from "@arco-design/web-react";
import {Permission} from "@/types/insights-web";
import {OwnerTypeEnum, ResourceTypeEnum, RoleTypeEnum} from "@/types/insights-common";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {getRandomString} from "@/utils/util";
import {
    requestGrantMetricPermission,
    requestGrantProjectPermission,
    requestQueryList, requestReleaseMetricPermission,
    requestReleaseProjectPermission
} from "@/api/permission";
import {getDepartPermissionColumns, getUserPermissionColumns} from "./constants";
import './styles/index.module.less'
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import UsersTransfer from "@/pages/components/transfer/user_transfer";

const CollapseItem = Collapse.Item;
const TabPane = Tabs.TabPane;

export function PermissionPanel({type,resourceId,resourceType,onClose}){

    const t = useLocale(locale);
    const { Col, Row } = Grid;
    const [loading,setLoading] = useState<boolean>(true);
    const [submitLoading,setSubmitLoading] = useState<boolean>(false);
    const [listData,setListData] = useState<Permission[]>([]);
    const [searchForms,setSearchForms] = useState<any>({});
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const departmentTransferRef = useRef(null);
    const userTransferRef = useRef(null);
    const [activeKeys,setActiveKeys] = useState([]);
    const formRef = useRef(null);

    const tableCallback = async (record, type) => {
        if(type == 'release'){
            await releasePermission(record.id);
        }
    }

    const getColumns = () => {
        if(type == 1){
            return getDepartPermissionColumns(t,tableCallback)
        }else{
            return getUserPermissionColumns(t,tableCallback);
        }
    }

    const columns = useMemo(() => getColumns(),[t,type]);

    const [pagination, setPagination] = useState<PaginationProps>({
        sizeOptions: [10,20,30,50],
        sizeCanChange: false,
        showTotal: true,
        pageSize: 5,
        current: 1,
        pageSizeChangeResetCurrent: false,
    });

    function onChangeTable({ current, pageSize }) {
        setPagination({
            ...pagination,
            current,
            pageSize,
        });
    }

    const releasePermission = async (permissionId) => {
        let roleType;
        if(resourceType == ResourceTypeEnum.Project){
            if(type == 3){
                roleType = RoleTypeEnum.PROJECT_MANAGE_PERMISSION;
            }else{
                roleType = RoleTypeEnum.PROJECT_ACCESS_PERMISSION;
            }
        }else if(resourceType == ResourceTypeEnum.Metric){
            if(type == 3){
                roleType = RoleTypeEnum.METRIC_MANAGE_PERMISSION;
            }else{
                roleType = RoleTypeEnum.METRIC_ACCESS_PERMISSION;
            }
        }else{
            throw new Error();
        }
        const requestParam = {
            resourceId:resourceId,
            roleType:roleType,
            permissionId:permissionId,
        }
        let request;
        if(resourceType == ResourceTypeEnum.Project){
            request = requestReleaseProjectPermission(requestParam);
        }else if(resourceType == ResourceTypeEnum.Metric){
            request = requestReleaseMetricPermission(requestParam);
        }
        await request.then((response) => {
            const {code, data ,message} = response;
            if (code === '0') {
                Notification.success({style: { width: 420 }, title: 'Success', content: t['permissionManage.list.operation.remove.success']});
                setReloadTime(Date.now);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error)
        })
    }


    const fetchListData = async () => {
        const {current, pageSize} = pagination;
        let roleType;
        if(resourceType == ResourceTypeEnum.Project){
            if(type == 3){
                roleType = RoleTypeEnum.PROJECT_MANAGE_PERMISSION;
            }else{
                roleType = RoleTypeEnum.PROJECT_ACCESS_PERMISSION;
            }
        }else if(resourceType == ResourceTypeEnum.Metric){
            if(type == 3){
                roleType = RoleTypeEnum.METRIC_MANAGE_PERMISSION;
            }else{
                roleType = RoleTypeEnum.METRIC_ACCESS_PERMISSION;
            }
        }else{
            throw new Error();
        }
        const requestParam = {
            resourceId:resourceId,
            roleType:roleType,
            ownerType:type == 1 ? OwnerTypeEnum.DEPARTMENT:OwnerTypeEnum.USER,
            search:searchForms.search,
        }
        setLoading(true);
        await requestQueryList({
            queryParams:requestParam,
            pagination:{
                pageSize:pageSize,
                pageNum:current,
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
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error)
        }).finally(() => {
            console.log("sss")
        })
    }

    const handlerSearch = (search) => {
        setPagination({ ...pagination, current: 1 });
        setSearchForms({search});
    }

    useEffect(() => {
        fetchListData().then();
    },[reloadTime,pagination.current, pagination.pageSize,JSON.stringify(searchForms)])

    const handleSubmit = async (e) => {
        e.stopPropagation();
        let departmentsPermissions = [];
        let usersPermissions = [];
        const privateType = 0;
        if(privateType == 0 && departmentTransferRef?.current){
            departmentsPermissions = departmentTransferRef.current.getData();
        }
        if(privateType == 0 && userTransferRef?.current){
            usersPermissions = userTransferRef.current.getData();
        }
        setSubmitLoading(true);
        let roleType;
        if(resourceType == ResourceTypeEnum.Project){
            if(type == 3){
                roleType = RoleTypeEnum.PROJECT_MANAGE_PERMISSION;
            }else{
                roleType = RoleTypeEnum.PROJECT_ACCESS_PERMISSION;
            }
        }else if(resourceType == ResourceTypeEnum.Metric){
            if(type == 3){
                roleType = RoleTypeEnum.METRIC_MANAGE_PERMISSION;
            }else{
                roleType = RoleTypeEnum.METRIC_ACCESS_PERMISSION;
            }
        }else{
            throw new Error();
        }
        const requestParam = {
            resourceId:resourceId,
            roleType:roleType,
            usersPermissions:usersPermissions,
            departmentsPermissions:departmentsPermissions,
        }
        let request;
        if(resourceType == ResourceTypeEnum.Project){
            request = requestGrantProjectPermission(requestParam);
        }else if(resourceType == ResourceTypeEnum.Metric){
            request = requestGrantMetricPermission(requestParam);
        }
        await request.then((response) => {
                const {code, data ,message} = response;
                if (code === '0') {
                    Notification.success({style: { width: 420 }, title: 'Success', content: t['permissionManage.list.operation.grant.success']});
                    setReloadTime(Date.now);
                }else{
                    Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                }
            }).catch((error) => {
                console.log(error)
            }).finally(()=> {
                setSubmitLoading(false);
            })
    }

    const updateActiveKeys = (v) => {
        if(activeKeys.includes("1")){
            setActiveKeys([]);
        }else{
            setActiveKeys(["1"]);
        }
    }

    return (
        <Form
            autoComplete={'off'}
            labelCol={{span: 4, offset: 0}}
        >
        <Space direction={"vertical"} style={{width:'100%'}}>
            <Form.Item field={'search'} style={{marginBottom:'0px'}}>
                <Input.Search size={"small"} style={{width:'350px',marginLeft:'3px'}} allowClear={true} onSearch={handlerSearch}/>
            </Form.Item>
            <Table rowKey={'id'} loading={loading} onChange={onChangeTable} style={{height:'200px',padding:"3px 3px"}} size={"mini"} pagination={pagination}  columns={columns} data={listData} />
            <Collapse activeKey={activeKeys} style={{marginTop:'10px',borderLeft:"none",borderRight:"none"}} onChange={updateActiveKeys}>
                <CollapseItem style={{borderLeft:"none",borderRight:"none"}} header={<span>{t['permissionManage.user.grantPermission']}</span>}
                              extra={
                                  activeKeys.includes("1")?
                                      <Button key={getRandomString()} style={{marginRight:'5px',marginLeft:"auto"}} type={"secondary"} onClick={handleSubmit} loading={submitLoading} size={"mini"}>{t['permissionManage.grant.submit']}</Button>
                                      :null
                              }
                              name='1'>
                    <Form.Item label={" "}>
                        {type == 1 ? <DepartmentsTransfer ref={departmentTransferRef}/> : <UsersTransfer ref={userTransferRef}/>}
                    </Form.Item>
                </CollapseItem>
            </Collapse>
        </Space>
        </Form>
    );
}