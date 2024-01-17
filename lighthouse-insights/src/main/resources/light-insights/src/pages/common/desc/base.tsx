import React from "react";
import {Badge} from "@arco-design/web-react";
import {
    ComponentTypeEnum,
    ResourceTypeEnum,
    RoleTypeEnum,
    StatExpiredEnum,
    StatStateEnum
} from "@/types/insights-common";
import {PiDiamondsFour} from "react-icons/pi";
import {CiViewTable} from "react-icons/ci";
import {IconTag} from "@arco-design/web-react/icon";
import {LuLayers} from "react-icons/lu";

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
        return t['components.type.dateSelect'];
    }else if(value == ComponentTypeEnum.DATEPICKER_DATE_RANGE_SELECT){
        return t['components.type.dateRangeSelect'];
    }else if(value == ComponentTypeEnum.FILTER_INPUT){
        return t['components.type.filterInput'];
    }else if(value == ComponentTypeEnum.FILTER_SELECT){
        return t['components.type.filterSelect'];
    }
}


export function getSystemComponentTypeDescription(t:any,value:ComponentTypeEnum) {
    if(value == ComponentTypeEnum.FILTER_INPUT){
        return t['components.type.system.filterInput'];
    }else if(value == ComponentTypeEnum.FILTER_SELECT){
        return t['components.type.system.filterSelect'];
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
    }else if(type == 'order'){

    }
}