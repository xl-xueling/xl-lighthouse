import {ArcoTreeNode} from "@/types/insights-web";
import {Badge} from "@arco-design/web-react";
import React from "react";

export interface ResultData<S = any> {
    code: string;
    message?: string;
    data?:S ;
}

export enum RenderTypeEnum {
    DATEPICKER_DATE_RANGE_SELECT=1,
    DATEPICKER_DATE_SELECT = 2,
    DATEPICKER_DATE_TIME_RANGE_SELECT=3,
    FILTER_INPUT=4,
    FILTER_SELECT=5,
    FILTER_REMOTE_SEARCH_SELECT=6,
}

export enum OrderTypeEnum {
    PROJECT_ACCESS=1,
    STAT_ACCESS=2,
    GROUP_THRESHOLD_ADJUST=3,
    STAT_ITEM_APPROVE=4,
}

export enum PermissionEnum {
    readable="readable",
    editable="editable",
    approveable="approveable",
    retractable="retractable",
}

export enum UserStateEnum{
    USER_PEND=0,
    USR_NORMAL=1,
    USER_FROZEN=2,
    USER_DELETED=3,
}

export enum OrderStateEnum {
    Processing=0,
    Approved=1,
    Rejected=2,
    Retracted=3,
}

export enum ApproveStateEnum {
    Wait=0,
    Pending=1,
    Approved=2,
    Rejected=3,
    Retracted=4,
    Suspend=5,
}

export enum RoleTypeEnum {
    FULL_MANAGE_PERMISSION=1,
    FULL_ACCESS_PERMISSION=2,
    DEPARTMENT_MANAGE_PERMISSION=3,
    DEPARTMENT_ACCESS_PERMISSION=4,
    PROJECT_MANAGE_PERMISSION=5,
    PROJECT_ACCESS_PERMISSION=6,
    METRIC_MANAGE_PERMISSION=7,
    METRIC_ACCESS_PERMISSION=8,
    STAT_ACCESS_PERMISSION=9,
    OPT_MANAGE_PERMISSION=10,
}

export interface FilterConfigParam {
    renderType:number,
    label:string,
    dimens:string,
    componentId?:number,
    configData?:Array<ArcoTreeNode>,
}

export interface DatePickerConfigParam {
    renderType:number,
    label:string,
}

export interface FilterComponent {
    id?:number,
    key?:string,
    title?:string,
    renderType?:number,
    config?:any,
}

export enum StatTimeParamEnum {
    "1-minute",
    "2-minute",
    "5-minute",
    "10-minute",
    "30-minute",
    "1-hour",
    "1-day",
}


export enum StatExpiredEnum {
    Week_1=604800,
    Week_2=1209600,
    MONTH_1=2592000,
    MONTH_2=5184000,
    MONTH_3=7776000,
    MONTH_6=15552000,
    MONTH_12=31104000,
    MONTH_24=62208000,
    MONTH_36=93312000,
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

export enum StatStateEnum {
    PENDING=0,
    RUNNING=1,
    STOPPED=2,
    LIMITING=3,
    DELETED=4,
    REJECTED=5,
    FROZEN=6,
    INVALID=7,
}

