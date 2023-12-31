import {List} from "@arco-design/web-react";
import {
    ApproveStateEnum,
    DatePickerConfigParam,
    FilterConfigParam, OrderStateEnum,
    OrderTypeEnum,
    RenderTypeEnum
} from "@/types/insights-common";

export enum PermissionsEnum {
    NONE = 0,
    USER = 1,
    ADMIN = 2,
}

export interface Department {
    id: number;
    name: string;
    children?:Array<Department>;
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
    permissions?:PermissionsEnum[];
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
    permissions?:PermissionsEnum[];
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
    permissions:PermissionsEnum[];
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
    orderType?:OrderTypeEnum;
    orderDetails?:OrderDetail[];
    extendConfig?:any;
}

export interface OrderDetail {
    id?:number;
    orderId?:number;
    state?:ApproveStateEnum;
    roleId?:number;
    user:User;
}

export interface LoginParam {
    username?:string;
    password?:string;
}


