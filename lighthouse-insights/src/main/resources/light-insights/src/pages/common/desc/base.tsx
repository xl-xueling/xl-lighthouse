import React from "react";
import {Badge} from "@arco-design/web-react";
import {StatStateEnum} from "@/types/insights-common";

export function getStatStateDescription (t: any, value:StatStateEnum) {
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