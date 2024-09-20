import {ArcoTreeNode, TreeNode} from "@/types/insights-web";
import {Badge} from "@arco-design/web-react";
import React from "react";

export interface ResultData<S = any> {
    code: string;
    message?: string;
    data?:S ;
}

export enum OwnerTypeEnum {
    USER=1,
    DEPARTMENT=2,
}

export enum OrderTypeEnum {
    PROJECT_ACCESS=1,
    STAT_ACCESS=2,
    METRIC_ACCESS=3,
    LIMITING_SETTINGS=4,
    STAT_PEND_APPROVE=5,
    USER_PEND_APPROVE=6,
    VIEW_ACCESS=7,
}

export enum PermissionEnum {
    AccessAble="AccessAble",
    ManageAble="ManageAble",
    OperationManageAble="OperationManageAble",
    SystemManageAble="SystemManageAble",
}

export interface BindElement {
    resourceId?:number,
    resourceType?:ResourceTypeEnum,
    title?:string,
}

export enum RecordTypeEnum {
    CREATE_DEPARTMENT=1,
    UPDATE_DEPARTMENT=2,
    DELETE_DEPARTMENT=3,
    CREATE_PROJECT=4,
    UPDATE_PROJECT=5,
    DELETE_PROJECT=6,
    CRATE_GROUP=7,
    UPDATE_GROUP=8,
    DELETE_GROUP=9,
    CREATE_STAT=10,
    UPDATE_STAT=11,
    DELETE_STAT=12,
    CREATE_METRIC=13,
    UPDATE_METRIC=14,
    DELETE_METRIC=15,
    GRANT_PERMISSION=16,
    RELEASE_PERMISSION=17,
    GROUP_MESSAGE_LIMITING=18,
    STAT_RESULT_LIMITING=19,
}

export enum UserStateEnum{
    USER_PEND=0,
    USER_DELETED=1,
    USR_NORMAL=2,
    USER_FROZEN=3,
    USER_REJECT=4,
}

export enum OrderStateEnum {
    Processing=0,
    Deleted=1,
    Approved=2,
    Rejected=3,
    Retracted=4,
}

export enum ApproveStateEnum {
    Wait=0,
    Pending=1,
    Approved=2,
    Rejected=3,
    Retracted=4,
    Suspend=5,
}

export enum ResourceTypeEnum {
    System=1,
    Domain=2,
    Department=3,
    Project=4,
    Group=5,
    Stat=6,
    Metric=7,
    View=8,
}

export enum RoleTypeEnum {
    FULL_MANAGE_PERMISSION=1,
    FULL_ACCESS_PERMISSION=2,
    OPT_MANAGE_PERMISSION=3,
    OPT_ACCESS_PERMISSION=4,
    DOMAIN_MANAGE_PERMISSION=5,
    DOMAIN_ACCESS_PERMISSION=6,
    DEPARTMENT_MANAGE_PERMISSION=7,
    DEPARTMENT_ACCESS_PERMISSION=8,
    PROJECT_MANAGE_PERMISSION=9,
    PROJECT_ACCESS_PERMISSION=10,
    GROUP_MANAGE_PERMISSION=11,
    GROUP_ACCESS_PERMISSION=12,
    STAT_MANAGE_PERMISSION=13,
    STAT_ACCESS_PERMISSION=14,
    METRIC_MANAGE_PERMISSION=15,
    METRIC_ACCESS_PERMISSION=16,
    VIEW_MANAGE_PERMISSION=17,
    VIEW_ACCESS_PERMISSION=18,
}

export interface RenderFilterConfig {
    key?:string,
    componentType?:ComponentTypeEnum,
    title?:string,
    label?:string,
    dimens?:string,
    componentId?:number,
    configData?:Array<TreeNode>,
}

export interface RenderChartConfig {
    functionIndex?:number,
    title?:string,
    chartType?:ChartTypeEnum,
}

export interface RenderDateConfig {
    componentType?:ComponentTypeEnum,
    label?:string,
}

export interface Component {
    id?:number,
    title?:string,
    componentType?:ComponentTypeEnum,
    privateType?:PrivateTypeEnum,
    configuration?:Array<TreeNode>,
    userId?:number,
    createTime?:number,
    updateTime?:number,
}

export enum PrivateTypeEnum {
    Private= 0,
    Public =1,
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
    // Week_1=604800,
    Week_2=1209600,
    MONTH_1=2592000,
    // MONTH_2=5184000,
    MONTH_3=7776000,
    MONTH_6=15552000,
    MONTH_12=31104000,
    MONTH_24=62208000,
    MONTH_36=93312000,
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

export interface LabelValue{
    label?:string,
    value?:any,
    state?:number,
}

export enum DebugModeEnum {
    CLOSE=0,
    OPEN=1,
}

export enum ComponentTypeEnum {
    DATEPICKER_DATE_SELECT=1,
    DATEPICKER_DATE_RANGE_SELECT=2,
    DATEPICKER_DATE_TIME_RANGE_SELECT=3,
    FILTER_INPUT=4,
    FILTER_SELECT=5,
    FILTER_REMOTE_SEARCH_SELECT=6,
    DATEPICKER_DATE_WEEK_SELECT=7,
    DATEPICKER_DATE_MONTH_SELECT=8,
}

export enum LimitingStrategyEnum {
    GROUP_MESSAGE_SIZE_LIMITING = 'GROUP_MESSAGE_SIZE_LIMITING',
    STAT_RESULT_SIZE_LIMITING = 'STAT_RESULT_SIZE_LIMITING'
}

export enum ChartTypeEnum {
    LINE_CHART=1,
    LINE_AREA_CHART=2,
    BAR_CHART=3,
    PIE_CHART=4,
    DASHBOARD_CHART=5,
    FUNNEL_CHART=6,
    CALENDAR_CHART=7,
    TABLE_CHART=8,
    SORTED_BAR_CHART=9,
}


export enum AssociateStateEnum {
    UnAssociate=0,
    Associated=1,
}

export enum SelectedStateEnum {
    UnSelect=0,
    Selected=1,
}
