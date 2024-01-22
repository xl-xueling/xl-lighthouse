import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Button,
    Collapse, DatePicker, Divider, Drawer,
    Form,
    Grid,
    Input,
    Message,
    Modal,
    Notification, PaginationProps,
    Radio, Select, Space, Table, TableColumnProps,
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
import './styles/index.module.less'
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import {IconPlus} from "@arco-design/web-react/icon";
import UsersTransfer from "@/pages/components/transfer/user_transfer";
const CollapseItem = Collapse.Item;
const TabPane = Tabs.TabPane;

export function PermissionManageModal({resourceId,resourceType,onClose}){

    const t = useLocale(locale);
    const { Col, Row } = Grid;
    const [loading,setLoading] = useState<boolean>(true);

    const [departListData,setDepartListData] = useState<Permission[]>([]);

    const [userListData,setUserListData] = useState<Permission[]>([]);

    const [adminListData,setAdminListData] = useState<Permission[]>([]);

    const [departPermissionForms,setDepartPermissionForms] = useState(null);

    const [userPermissionForms,setUserPermissionForms] = useState(null);
    const departmentTransferRef = useRef(null);
    const userTransferRef = useRef(null);

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

    const handleSubmit = (e) => {
        e.stopPropagation();
        Message.success("ssssss");
    }

    return (
        <Modal
            title= {'权限管理'}
            style={{ width:'1180px',verticalAlign:'top', maxWidth:'90%', marginTop: '130px' }}
            visible={true}
            className={"permission_table"}
            footer={null}
            onCancel={onClose}>
            <Tabs type={"card-gutter"} defaultActiveTab='1'>
                <TabPane key='1' title='部门访问权限'>
                    <Space direction={"vertical"} style={{width:'100%'}}>
                        <Table style={{maxHeight:'300px'}} size={"mini"} pagination={pagination1} columns={columns} data={departListData} />
                        <Collapse>
                            <CollapseItem header={
                                <div style={{display:"flex"}}>
                                    <span>添加权限</span>
                                    <Button style={{marginRight:'5px',marginLeft:"auto"}} type={"primary"} onClick={handleSubmit} size={"mini"}>提交修改</Button>
                                </div>
                            } name='3'>
                                <Form
                                    labelCol={{span: 4, offset: 0}}
                                >
                                    <Form.Item label={" "} field='title'>
                                        <DepartmentsTransfer ref={departmentTransferRef}/>
                                    </Form.Item>
                                </Form>
                            </CollapseItem>
                        </Collapse>
                    </Space>
                </TabPane>
                <TabPane key='2' title='用户访问权限'>
                    <Typography.Paragraph>Content of Tab Panel 2</Typography.Paragraph>
                </TabPane>
                <TabPane key='3' title='管理员权限'>
                    <Typography.Paragraph>Content of Tab Panel 3</Typography.Paragraph>
                </TabPane>
            </Tabs>
        </Modal>
    );
}