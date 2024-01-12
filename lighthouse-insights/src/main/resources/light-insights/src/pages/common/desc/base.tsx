import React from "react";
import {Badge} from "@arco-design/web-react";
import {ComponentTypeEnum, StatStateEnum} from "@/types/insights-common";

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
