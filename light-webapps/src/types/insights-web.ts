import {List} from "@arco-design/web-react";
import {
    ApproveStateEnum,
    RenderDateConfig,
    RenderFilterConfig, OrderStateEnum,
    OrderTypeEnum, PermissionEnum, RecordTypeEnum,
    ResourceTypeEnum, RoleTypeEnum, StatStateEnum, UserStateEnum, PrivateTypeEnum
} from "@/types/insights-common";
import Decimal from "decimal.js";

export interface HomeData {
    projectCount?:number;
    ytdProjectCount?:number;
    statCount?:number;
    ytdStatCount?:number;
    metricCount?:number;
    ytdMetricCount?:number;
    userCount?:number;
    departmentStatCount?:Array<StatData>;
}

export interface Department {
    id: number;
    name: string;
    children?:Array<Department>;
    pid:number;
}

export interface GrantPrivileges {
    id:number;
    departments?:Array<number>;
    users?:Array<number>;
    teams?:Array<number>;
}

export interface TreeNode {
    key?:string;
    label?:string;
    value?:string;
    type?:any;
    children?:Array<TreeNode>;
}

export interface ArcoTreeNode {
    key: string;
    title: string;
    disabled?:boolean;
    icon?:any;
    children?:Array<ArcoTreeNode>;
}

export interface ArcoFlatNode {
    key: string;
    title: string;
}

export interface User {
    id?: number;
    username?: string;
    password?:string;
    email?: string;
    phone?: string;
    departmentId?: number;
    state?:UserStateEnum;
    createTime?:number;
    avatar?:string;
    permissions?: any;
}

export interface Project {
    id?:number;
    title?:string;
    desc?:string;
    privateType?:number;
    adminIds?:Array<number>;
    admins?:Array<User>;
    departmentId?:number;
    structure?:TreeNode;
    createTime?:number;
    permissions?:PermissionEnum[];
    usersPermission?:number[];
    departmentsPermission?:number[];
    builtIn?:boolean;
}

export interface Relation {
    id?:number;
    subjectId?:number;
    relationType?:RelationTypeEnum;
    resourceId?:number;
    resourceType?:ResourceTypeEnum;
    createTime?:number;
    extend?:any;
}

export enum RelationTypeEnum {
    MetricSetBindRelation=1,
    UserPickUpMetricSetRelation=2,
    StatPrecedingIndicator=3,
    StatParallelIndicator=4,
    StatSubsequentIndicator=5,
}


export interface Column {
    key?:number|string;
    name?:string;
    type?:number;
    comment?:string;
}

export interface Group {
    id?:number;
    token?:string;
    projectId?:number;
    project?:Project;
    desc?:string;
    createTime?:number;
    columns?:Array<Column>;
    relatedColumns?:Array<String>;
    builtIn?:boolean;
}

export interface Stat {
    id?:number;
    title?:string;
    groupId?:number;
    projectId?:number;
    timeparam?:string;
    template?:string;
    expired?:number;
    createdTime?:number;
    projectTitle?:string;
    token?:string;
    desc?:string;
    templateEntity?:any,
    state?:StatStateEnum;
    renderConfig?:{datepicker:RenderDateConfig,filters:Array<RenderFilterConfig>}
    permissions?:PermissionEnum[];
    builtIn?:boolean;
}

export interface MetricSet {
    id?:number;
    title?:string;
    adminIds?:Array<number>;
    admins?:Array<User>;
    privateType?:PrivateTypeEnum;
    desc?:string;
    createTime?:number;
    updateTime?:number;
    createUser?:User;
    lastVisitTime?:number;
    bindElements?:Resource[];
    permissions?:PermissionEnum[];
    initUsersPermission?:number[];
    initDepartmentsPermission?:number[];
    customStructure?:boolean;
}

export interface Indicator{
    resourceId?:number;
    resourceType?:ResourceTypeEnum;
    title?:string;
    fullPath?:string;
    fullTitle?:string;
    createTime?;number;
}

export interface Resource {
    resourceId?:number;
    resourceType?:ResourceTypeEnum;
    title?:string;
    extend?:any;
}


export interface Order {
    id?:number;
    userId?:number;
    user?:User;
    title?:string;
    createTime?:number;
    updateTime?:number;
    reason?:string;
    adminsMap?:any;
    state?:OrderStateEnum;
    currentNode?:number;
    orderType?:OrderTypeEnum;
    orderDetails?:OrderDetail[];
    extendConfig?:any;
    extend?:any,
    permissions?:PermissionEnum[];
}

export interface OrderDetail {
    id?:number;
    orderId?:number;
    state?:ApproveStateEnum;
    roleType?:RoleTypeEnum;
    roleId?:number;
    user:User;
    createTime?:number;
    approveTime?:number;
}

export interface LoginParam {
    username?:string;
    password?:string;
}

export interface Record {
    id?:number;
    userId?:number;
    resourceId?:number;
    resourceType?:ResourceTypeEnum;
    recordType?:RecordTypeEnum;
    extend?:string;
    recordTime?:number;
}

export interface StatData{
    statId?:number;
    dimens?:string;
    dimensValue?:string;
    displayDimensValue?:string;
    valuesList?:StatValue[];
}

export interface LimitData {
    batchTime?:string;
    displayBatchTime?:string;
    values?:Array<LimitDataElement>;
}

export interface LimitDataElement{
    dimensValue?:string|number;
    score?:string|number;
}

export interface LineChartData {
    dataMap:Map<string,(number|string)[]>;
    xAxis?:Array<string>;
}

export interface TimeLineBarChartData {
    data?:Map<number,Map<string,number>>;
}

export interface EChartChartValue {
    name?:string;
    type?:string;
    data?:Array<Decimal>;
}

export interface StatValue {
    batchTime?:number;
    displayBatchTime?:string;
    value?:Decimal;
    statesValue?:number[];
    updateTime?:number;
}

export interface Permission {
    id?:number;
    ownerId?:number;
    ownerType?:number;
    roleId?:number;
    roleType?:number;
    extend?:any;
    createTime?:number;
    updateTime?:number;
}

export interface ExportData {
    filename?:string;
    content?:string;
}
