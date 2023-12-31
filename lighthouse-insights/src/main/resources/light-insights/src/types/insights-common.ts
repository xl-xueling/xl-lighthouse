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
    PENDING=0,
    APPROVED=1,
    REJECTED=2,
    RETRACTED=3,
}

export enum ApproveStateEnum {
    PENDING=0,
    APPROVED=1,
    REJECTED=2,
}

export enum RoleTypeEnum {
    SYSTEM_ADMIN=1,
    PROJECT_ADMIN=2,
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