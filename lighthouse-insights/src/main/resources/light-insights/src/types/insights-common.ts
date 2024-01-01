import {ArcoTreeNode} from "@/types/insights-web";

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