import React from "react";
import {Badge} from "@arco-design/web-react";
import {
    ApproveStateEnum,
    CallerStateEnum,
    ComponentTypeEnum,
    LimitingStrategyEnum,
    OrderStateEnum,
    OrderTypeEnum,
    PermissionEnum,
    PrivateTypeEnum,
    ResourceTypeEnum,
    RoleTypeEnum,
    StatExpiredEnum,
    StatStateEnum,
    UserStateEnum
} from "@/types/insights-common";
import {PiDiamondsFour, PiLinkSimple, PiLockKeyThin} from "react-icons/pi";
import {CiViewTable} from "react-icons/ci";
import {
    IconCalendarClock, IconFolder,
    IconMindMapping,
    IconStar,
    IconStarFill,
    IconTag
} from "@arco-design/web-react/icon";
import {LuLayers} from "react-icons/lu";
import {RxCube} from "react-icons/rx";
import {VscGistSecret, VscOutput} from "react-icons/vsc";
import {HiMiniBoltSlash} from "react-icons/hi2";
import {TbBrandVisualStudio} from "react-icons/tb";
import {formatString} from "@/utils/util";
import {AlarmStateEnum} from "@/types/alarm";

export function getStatStateDescriptionWithBadge (t: any, value:StatStateEnum) {
    if(value === StatStateEnum.PENDING){
        return <Badge color={'gold'} text={t['basic.columns.state.pending']}/>;
    }else if (value === StatStateEnum.RUNNING) {
        return <Badge color={'arcoblue'} text={t['basic.columns.state.running']}/>;
    }else if(value === StatStateEnum.STOPPED){
        return <Badge color={'gray'} text={t['basic.columns.state.stopped']}/>;
    }else if(value === StatStateEnum.LIMITING){
        return <Badge status={'warning'} text={t['basic.columns.state.limiting']}/>;
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

export const getOrderDescription = (t: any, orderInfo) => {
    if(orderInfo.orderType == OrderTypeEnum.PROJECT_ACCESS){
        return formatString(t['basic.order.description.projectAccess'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.STAT_ACCESS){
        return formatString(t['basic.order.description.statAccess'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.METRIC_ACCESS){
        return formatString(t['basic.order.description.metricAccess'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.VIEW_ACCESS){
        return formatString(t['basic.order.description.viewAccess'],orderInfo?.extend?.title)
    }else if(orderInfo.orderType == OrderTypeEnum.LIMITING_SETTINGS){
        return formatString(t['basic.order.description.updateLimitingThreshold'],orderInfo?.extend?.token)
    } else if(orderInfo.orderType == OrderTypeEnum.USER_PEND_APPROVE){
        return formatString(t['basic.order.description.userPendApprove'],orderInfo?.extend?.username)
    } else if(orderInfo.orderType == OrderTypeEnum.STAT_PEND_APPROVE){
        return formatString(t['basic.order.description.statPendApprove'],orderInfo?.extend?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS){
        return formatString(t['basic.order.description.callerProjectAccess'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.project?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_STAT_ACCESS){
        return formatString(t['basic.order.description.callerStatAccess'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.stat?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS){
        return formatString(t['basic.order.description.callerViewAccess'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.view?.title)
    }else if(orderInfo.orderType == OrderTypeEnum.CALLER_PROJECT_ACCESS_EXTENSION){
        return formatString(t['basic.order.description.callerProjectAccessExtension'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.project?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_STAT_ACCESS_EXTENSION){
        return formatString(t['basic.order.description.callerStatAccessExtension'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.stat?.title)
    } else if(orderInfo.orderType == OrderTypeEnum.CALLER_VIEW_ACCESS_EXTENSION){
        return formatString(t['basic.order.description.callerViewAccessExtension'],orderInfo?.extend?.caller?.name,orderInfo?.extend?.view?.title)
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
    }else if(value == RoleTypeEnum.VIEW_MANAGE_PERMISSION){
        title = t['orderApprove.roleType.description.viewManager'];
    }
    return title;
}

export const getStatExpiredEnumDescription = (expired) => {
    if(expired == StatExpiredEnum.Week_2){
        return "2 Week";
    }else if(expired == StatExpiredEnum.MONTH_1){
        return "1 Month";
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

export function getLimitingStrategyDescription(t:any,value:LimitingStrategyEnum){
    if(value == LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMITING){
        return t['basic.limitingStrategy.messageSizeLimiting'];
    }else if(value == LimitingStrategyEnum.STAT_RESULT_SIZE_LIMITING){
        return t['basic.limitingStrategy.resultSizeLimiting'];
    }else{
        return null;
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

export function getCallerStateDescription(t:any,value:CallerStateEnum){
    if(value == CallerStateEnum.Pend){
        return <Badge status="processing" text={t['basic.callerState.description.pending']}/>;
    }else if (value === CallerStateEnum.Deleted) {
        return <Badge status="error" text={t['basic.callerState.description.deleted']}/>;
    }else if(value === CallerStateEnum.Normal){
        return <Badge status="success" text={t['basic.callerState.description.normal']}/>;
    }else if(value === CallerStateEnum.Frozen){
        return <Badge status="error" text={t['basic.callerState.description.frozen']}/>;
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
    }else if(value == OrderTypeEnum.VIEW_ACCESS){
        return t['basic.orderType.description.viewAccess'];
    }else if(value == OrderTypeEnum.LIMITING_SETTINGS){
        return t['basic.orderType.description.updateLimitingThreshold'];
    }else if(value == OrderTypeEnum.CALLER_PROJECT_ACCESS){
        return t['basic.orderType.description.callerProjectAccess']
    }else if(value == OrderTypeEnum.CALLER_STAT_ACCESS){
        return t['basic.orderType.description.callerStatAccess']
    }else if(value == OrderTypeEnum.CALLER_VIEW_ACCESS){
        return t['basic.orderType.description.callerViewAccess']
    }else if(value == OrderTypeEnum.CALLER_PROJECT_ACCESS_EXTENSION){
        return t['basic.orderType.description.callerProjectAccessExtension']
    }else if(value == OrderTypeEnum.CALLER_STAT_ACCESS_EXTENSION){
        return t['basic.orderType.description.callerStatAccessExtension']
    }else if(value == OrderTypeEnum.CALLER_VIEW_ACCESS_EXTENSION){
        return t['basic.orderType.description.callerViewAccessExtension']
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
    }else if(value == RoleTypeEnum.VIEW_MANAGE_PERMISSION){
        return t['basic.roleType.description.viewManage'];
    }else if(value == RoleTypeEnum.VIEW_ACCESS_PERMISSION){
        return t['basic.roleType.description.viewAccess'];
    }else if(value == RoleTypeEnum.CALLER_MANAGE_PERMISSION){
        return t['basic.roleType.description.callerManage'];
    }else if(value == RoleTypeEnum.CALLER_ACCESS_PERMISSION){
        return t['basic.roleType.description.callerAccess'];
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
    }else{
        return <PiLockKeyThin style={{marginLeft:'5px'}}/>;
    }
}

export const getTreeResourceIcon= (type,level) => {
    if(type == 'stat'){
        return <IconTag style={{marginRight:'8px'}}/>
    }else if(type == 'metric'){
        return <LuLayers style={{marginRight:'8px',position: 'relative', top: '2px'}}/>
    }else if(type == 'group'){
        return <CiViewTable style={{marginRight:'8px',position: 'relative', top: '2px'}}/>
    }else if(type == 'project'){
        return <PiDiamondsFour style={{marginRight:'8px',position: 'relative', top: '2px'}}/>
    }else if(type == 'view'){
        return <TbBrandVisualStudio size={15} style={{marginRight:'8px',position: 'relative',top:'2px',opacity:0.8}}/>
    }else if(type == 'dir' && level == 1){
        // return <RxCube size={15} style={{marginRight:'8px',position: 'relative', top: '1px'}}/>
        return <IconFolder style={{marginRight:'8px',position: 'relative'}}/>
    } else if(type == 'dir' && level == 2){
        // return <IconMindMapping  style={{marginRight:'8px',position: 'relative', top: '0px'}}/>
        return <IconFolder style={{marginRight:'8px',position: 'relative'}}/>
    }
}

export const getTreeResourceIconWithColor = (type,level,color) => {
    if(type == 'stat'){
        return <IconTag style={{marginRight:'8px',color:color}}/>
    }else if(type == 'metric'){
        return <LuLayers color={color} style={{marginRight:'8px',position: 'relative', top: '2px',color:color}}/>
    }else if(type == 'group'){
        return <CiViewTable color={color} style={{marginRight:'8px',position: 'relative', top: '2px',color:color}}/>
    }else if(type == 'project'){
        return <PiDiamondsFour color={color} style={{marginRight:'8px',position: 'relative', top: '2px',color:color}}/>
    }else if(type == 'view'){
        return <TbBrandVisualStudio color={color} size={15} style={{marginRight:'8px',position: 'relative',top:'2px',color:color}}/>
    }else if(type == 'dir' && level == 1){
        return <RxCube size={15} color={color} style={{marginRight:'8px',position: 'relative', top: '1px',color:color}}/>
    } else if(type == 'dir' && level == 2){
        return <IconMindMapping color={color} style={{marginRight:'8px',position: 'relative', top: '2px',color:color}}/>
    }
}

export function getIcon(type:string,marginRight:string = null){
    if(type == 'project'){
        return <PiDiamondsFour style={{marginRight: marginRight}}/>
    }else if(type == 'group'){
        return <CiViewTable style={{marginRight: marginRight}}/>
    }else if(type == 'stat'){
        return <IconTag style={{marginRight: marginRight}}/>
    }else if(type == 'view'){
        return <TbBrandVisualStudio size={16} style={{marginRight: marginRight}}/>
    }else if(type == 'metric'){
        return <LuLayers style={{marginRight: marginRight}}/>
    }else if(type == 'permission'){
        return <VscGistSecret style={{marginRight: marginRight}}/>
    }else if(type == 'star'){
        return <IconStar style={{marginRight: marginRight}}/>
    }else if(type == 'stared'){
        return <IconStarFill style={{marginRight: marginRight}}/>
    }else if(type == 'bind'){
        return <PiLinkSimple style={{marginRight: marginRight}}/>
    }else if(type == 'order'){
        return <IconCalendarClock style={{marginRight: marginRight}}/>
    }else if(type == 'apply'){
        return <VscOutput style={{marginRight: marginRight}}/>
    }else if(type == 'approve'){
        return <IconCalendarClock style={{marginRight: marginRight}}/>
    }else if(type == 'limiting'){
        return <HiMiniBoltSlash style={{marginRight: marginRight}}/>
    }
}

export function getResourceTypeDescription(t:any,resourceType:ResourceTypeEnum){
    if(resourceType == ResourceTypeEnum.Project){
        return t['basic.resourceType.project'];
    }else if(resourceType == ResourceTypeEnum.Stat){
        return t['basic.resourceType.statistic'];
    }else if(resourceType == ResourceTypeEnum.Metric){
        return t['basic.resourceType.metricSet'];
    }else if(resourceType == ResourceTypeEnum.View){
        return t['basic.resourceType.view'];
    }else if(resourceType == ResourceTypeEnum.Department){
        return t['basic.resourceType.department'];
    }else if(resourceType == ResourceTypeEnum.Group){
        return t['basic.resourceType.group'];
    }else if(resourceType == ResourceTypeEnum.Domain){
        return t['basic.resourceType.domain'];
    }
}

export function getAlarmStateDescription(t:any,stateEnum:AlarmStateEnum){
    if(stateEnum == AlarmStateEnum.DISABLE){
        return <Badge status="default" text={t['basic.alarm.state.disable']}/>;
    }else if(stateEnum == AlarmStateEnum.ENABLE){
        return <Badge status="processing" text={t['basic.alarm.state.enable']}/>;
    }
}

