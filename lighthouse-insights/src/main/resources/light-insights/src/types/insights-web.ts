import {List} from "@arco-design/web-react";
import {
    ApproveStateEnum,
    DatePickerConfigParam,
    FilterConfigParam, OrderStateEnum,
    OrderTypeEnum, PermissionEnum,
    RenderTypeEnum, RoleTypeEnum
} from "@/types/insights-common";


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

export interface ArcoTreeNode {
    key: string;
    title: string;
    disabled?:boolean;
    icon?:any;
    children?:Array<ArcoTreeNode>;
}

export interface ArcoSelectNode {
    label: string;
    value: string;
    children?:Array<ArcoSelectNode>;
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
    department?:Department;
    state?:number;
    createTime?:number;
    avatar?:string;
    permissions?: Record<string, string[]>;
}

export interface Project {
    id?:number;
    title?:string;
    desc?:string;
    privateType?:number;
    adminIds?:Array<number>;
    admins?:Array<User>;
    departmentId?:number;
    department?:Department;
    structure?:Array<ArcoTreeNode>;
    createTime?:number;
    permissions?:PermissionEnum[];
}

export interface Column {
    key?:number|string;
    name?:string;
    type?:number;
    desc?:string;
}

export interface Group {
    id?:number;
    token?:string;
    projectId?:number;
    project?:Project;
    desc?:string;
    createTime?:number;
    columns?:Array<Column>;
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
    group?:Group;
    project?:Project;
    dimensArray?:Array<string>;
    desc?:string;
    renderConfig?:{datepicker:DatePickerConfigParam,filters:Array<FilterConfigParam>}
    permissions?:PermissionEnum[];
}


export interface MetricSet {
    id?:number;
    title?:string;
    createdTime?:number;
    description?:string;
    adminIds?:Array<number>;
    structure?:Array<ArcoTreeNode>;
}

export interface MetricSetPagination extends MetricSet{
    admins?:Array<User>;
}

export interface Order {
    id?:number;
    userId?:number;
    user?:User;
    title?:string;
    createTime?:number;
    updateTime?:number;
    desc?:string;
    adminsMap?:any;
    state?:OrderStateEnum;
    currentNode?:number;
    orderType?:OrderTypeEnum;
    orderDetails?:OrderDetail[];
    extendConfig?:any;
    permissions?:PermissionEnum;
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


