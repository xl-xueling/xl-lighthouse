import React from "react";
import {Badge} from "@arco-design/web-react";
import {
    ApproveStateEnum,
    ComponentTypeEnum,
    OrderStateEnum,
    OrderTypeEnum,
    PermissionEnum,
    PrivateTypeEnum,
    RoleTypeEnum,
    StatExpiredEnum,
    StatStateEnum,
    UserStateEnum
} from "@/types/insights-common";
import {PiDiamondsFour} from "react-icons/pi";
import {CiLock, CiViewTable} from "react-icons/ci";
import {IconTag} from "@arco-design/web-react/icon";
import {LuLayers} from "react-icons/lu";
import { CiUnlock } from "react-icons/ci";
import { PiLockKeyThin } from "react-icons/pi";
import { PiLockKeyOpenThin } from "react-icons/pi";


export function getStatStateDescriptionWithBadge (t: any, value:StatStateEnum) {
    if(value === StatStateEnum.PENDING){
        return <Badge color={'gold'} text={t['basic.columns.state.pending']}/>;
    }else if (value === StatStateEnum.RUNNING) {
        return <Badge color={'arcoblue'} text={t['basic.columns.state.running']}/>;
    }else if(value === StatStateEnum.STOPPED){
        return <Badge color={'gray'} text={t['basic.columns.state.stopped']}/>;
    }else if(value === StatStateEnum.LIMITING){
        return <Badge color={'pinkpurple'} text={t['basic.columns.state.limiting']}/>;
    }else if(value === StatStateEnum.DELETED){
        return <Badge color={'cyan'} text={t['basic.columns.state.deleted']}/>;
    }else if(value === StatStateEnum.REJECTED){
        return <Badge color={'cyan'} text={t['basic.columns.state.rejected']}/>;
    }else if(value === StatStateEnum.FROZEN){
        return <Badge color={'red'} text={t['basic.columns.state.frozen']}/>;
    }else if(value === StatStateEnum.INVALID){
        return <Badge color={'red'} text={t['basic.columns.state.invalid']}/>;
    }
}
export function getStatStateDescription (t: any, value:StatStateEnum) {
    if(value === StatStateEnum.PENDING){
        return t['basic.columns.state.pending'];
    }else if (value === StatStateEnum.RUNNING) {
        return t['basic.columns.state.running'];
    }else if(value === StatStateEnum.STOPPED){
        return t['basic.columns.state.stopped'];
    }else if(value === StatStateEnum.LIMITING){
        return t['basic.columns.state.limiting'];
    }else if(value === StatStateEnum.DELETED){
        return t['basic.columns.state.deleted'];
    }else if(value === StatStateEnum.REJECTED){
        return t['basic.columns.state.rejected'];
    }else if(value === StatStateEnum.FROZEN){
        return t['basic.columns.state.frozen'];
    }else if(value === StatStateEnum.INVALID){
        return t['basic.columns.state.invalid'];
    }
}

export function getOrderApproveRoleTypeDescription(t:any,value:RoleTypeEnum){
    let title = "";
    if(value == RoleTypeEnum.OPT_MANAGE_PERMISSION){
        title = t['orderApprove.roleType.description.operationManager'];
    }else if(value == RoleTypeEnum.FULL_MANAGE_PERMISSION){
        title = t['orderApprove.roleType.description.systemManager'];
    }else if(value == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION){
        title = t['orderApprove.roleType.description.departmentManager'];
    }else if(value == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
        title = t['orderApprove.roleType.description.projectManager'];
    }else if(value == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
        title = t['orderApprove.roleType.description.metricManager'];
    }
    return title;
}


export const getStatExpiredEnumDescription = (expired) => {
    if(expired == StatExpiredEnum.Week_1){
        return "1 Week";
    }else if(expired == StatExpiredEnum.Week_2){
        return "2 Week";
    }else if(expired == StatExpiredEnum.MONTH_1){
        return "1 Month";
    }else if(expired == StatExpiredEnum.MONTH_2){
        return "2 Month";
    }else if(expired == StatExpiredEnum.MONTH_3){
        return "3 Month";
    }else if(expired == StatExpiredEnum.MONTH_6){
        return "6 Month";
    }else if(expired == StatExpiredEnum.MONTH_12){
        return "1 Year";
    }else if(expired == StatExpiredEnum.MONTH_24){
        return "2 Year";
    }else if(expired == StatExpiredEnum.MONTH_36){
        return "3 Year";
    }else{
        return "--";
    }
}


export function getComponentTypeDescription(t:any,value:ComponentTypeEnum) {
    if(value == ComponentTypeEnum.DATEPICKER_DATE_SELECT){
        return t['basic.componentsType.dateSelect'];
    }else if(value == ComponentTypeEnum.DATEPICKER_DATE_RANGE_SELECT){
        return t['basic.componentsType.dateRangeSelect'];
    }else if(value == ComponentTypeEnum.FILTER_INPUT){
        return t['basic.componentsType.filterInput'];
    }else if(value == ComponentTypeEnum.FILTER_SELECT){
        return t['basic.componentsType.filterSelect'];
    }
}


export function getSystemComponentTypeDescription(t:any,value:ComponentTypeEnum) {
    if(value == ComponentTypeEnum.FILTER_INPUT){
        return t['basic.componentsType.system.filterInput'];
    }else if(value == ComponentTypeEnum.FILTER_SELECT){
        return t['basic.componentsType.system.filterSelect'];
    }
}

export function getIcon(type:string){
    if(type == 'project'){
        return <PiDiamondsFour style={{marginRight:'10px'}}/>
    }else if(type == 'group'){
        return <CiViewTable style={{marginRight:'10px'}}/>
    }else if(type == 'stat'){
        return <IconTag/>
    }else if(type == 'metric'){
        return <LuLayers style={{marginRight:'10px'}}/>
    }else if(type == 'permission'){
        return <LuLayers style={{marginRight:'10px'}}/>
    }
}


export function getOrderStateDescription(t:any,value:OrderStateEnum){
    if(value == OrderStateEnum.Processing){
        return <Badge status="processing" text={t['basic.orderState.description.processing']}/>;
    }else if (value === OrderStateEnum.Approved) {
        return <Badge status="success" text={t['basic.orderState.description.approved']}/>;
    }else if(value === OrderStateEnum.Rejected){
        return <Badge status="error" text={t['basic.orderState.description.rejected']}/>;
    }else if(value === OrderStateEnum.Retracted){
        return <Badge status="error" text={t['basic.orderState.description.retracted']}/>;
    }else if(value === OrderStateEnum.Deleted){
        return <Badge status="error" text={t['basic.orderState.description.deleted']}/>;
    }
}

export function getOrderDetailStateDescription(t:any,value:ApproveStateEnum){
    if(value == ApproveStateEnum.Wait){
        return <Badge status="processing" text={t['basic.orderDetail.approveState.description.wait']}/>;
    }else if(value === ApproveStateEnum.Pending){
        return <Badge status="error" text={t['basic.orderDetail.approveState.description.pending']}/>;
    }else if (value === ApproveStateEnum.Approved) {
        return <Badge status="success" text={t['basic.orderDetail.approveState.description.approved']}/>;
    }else if(value === ApproveStateEnum.Rejected){
        return <Badge status="error" text={t['basic.orderDetail.approveState.description.rejected']}/>;
    }else if(value === ApproveStateEnum.Retracted){
        return <Badge status="error" text={t['basic.orderDetail.approveState.description.retracted']}/>;
    }else if(value === ApproveStateEnum.Suspend){
        return <Badge status="error" text={t['basic.orderDetail.approveState.description.suspend']}/>;
    }
}

export function getUserStateDescription(t:any,value:UserStateEnum){
    if(value == UserStateEnum.USER_PEND){
        return <Badge status="processing" text={t['basic.userState.description.pending']}/>;
    }else if (value == UserStateEnum.USR_NORMAL) {
        return <Badge status="success" text={t['basic.userState.description.normal']}/>;
    }else if(value == UserStateEnum.USER_FROZEN){
        return <Badge status="error" text={t['basic.userState.description.frozen']}/>;
    }else if(value == UserStateEnum.USER_REJECT){
        return <Badge status="error" text={t['basic.userState.description.rejected']}/>;
    }else if(value == UserStateEnum.USER_DELETED){
        return <Badge status="error" text={t['basic.userState.description.deleted']}/>;
    }
}

export function getOrderTypeDescription(t:any,value:OrderTypeEnum){
    if(value == OrderTypeEnum.USER_PEND_APPROVE){
        return t['basic.orderType.description.userPendApprove'];
    }else if (value == OrderTypeEnum.STAT_PEND_APPROVE) {
        return t['basic.orderType.description.statPendApprove'];
    }else if(value == OrderTypeEnum.PROJECT_ACCESS){
        return t['basic.orderType.description.projectAccess']
    }else if(value == OrderTypeEnum.METRIC_ACCESS){
        return t['basic.orderType.description.metricAccess']
    }else if(value == OrderTypeEnum.STAT_ACCESS){
        return t['basic.orderType.description.statAccess'];
    }else if(value == OrderTypeEnum.GROUP_THRESHOLD_ADJUST){
        return t['basic.orderType.description.limitedThresholdAdjust'];
    }
}

export function getRoleTypeDescription(t:any,value:RoleTypeEnum){
    if(value == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
        return t['basic.roleType.description.projectManage'];
    }else if(value == RoleTypeEnum.PROJECT_ACCESS_PERMISSION){
        return t['basic.roleType.description.projectAccess'];
    }else if(value == RoleTypeEnum.GROUP_MANAGE_PERMISSION){
        return t['basic.roleType.description.groupManage'];
    }else if(value == RoleTypeEnum.GROUP_ACCESS_PERMISSION){
        return t['basic.roleType.description.groupAccess'];
    }else if(value == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
        return t['basic.roleType.description.metricManage'];
    }else if(value == RoleTypeEnum.METRIC_ACCESS_PERMISSION){
        return t['basic.roleType.description.metricAccess'];
    }else if(value == RoleTypeEnum.STAT_MANAGE_PERMISSION){
        return t['basic.roleType.description.statManage'];
    }else if(value == RoleTypeEnum.STAT_ACCESS_PERMISSION){
        return t['basic.roleType.description.statAccess'];
    }else if(value == RoleTypeEnum.DOMAIN_MANAGE_PERMISSION){
        return t['basic.roleType.description.domainManage'];
    }else if(value == RoleTypeEnum.DOMAIN_ACCESS_PERMISSION){
        return t['basic.roleType.description.domainAccess'];
    }else if(value == RoleTypeEnum.OPT_MANAGE_PERMISSION){
        return t['basic.roleType.description.operationManage'];
    }else if(value == RoleTypeEnum.OPT_ACCESS_PERMISSION){
        return t['basic.roleType.description.operationAccess'];
    }
}

export function getLockIcon(t:any,privateType:PrivateTypeEnum,permissions:PermissionEnum[]){
    if(privateType == PrivateTypeEnum.Public){
        return null;
    }else if(permissions.includes(PermissionEnum.AccessAble) || permissions.includes(PermissionEnum.ManageAble)){
        return <PiLockKeyOpenThin style={{marginLeft:'5px'}}/>;
    }else{
        return <PiLockKeyThin style={{marginLeft:'5px'}}/>;
    }
}