import {List} from "@arco-design/web-react";
import {DatePickerConfigParam, FilterConfigParam, OrderTypeEnum, RenderTypeEnum} from "@/types/insights-common";

export enum PrivilegeEnum {
    ADMIN = 1,
    USER = 2,
    PROJECT_ADMIN = 3,
    PROJECT_USER = 4,
    STAT_ADMIN = 5,
    STAT_USER = 6,
    METRIC_ADMIN = 7,
    METRIC_USER = 8,
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
    userName?: string;
    email?: string;
    phone?: string;
    departmentId?: number;
    department?:Department;
    state?:number;
    createdTime?:number;
    avatar?:string;
    permissions?: Record<string, string[]>;
}

export interface Project {
    id?:number;
    name?:string;
    desc?:string;
    isPrivate?:number;
    adminIds?:Array<number>;
    admins?:Array<User>;
    departmentId?:number;
    structure?:Array<ArcoTreeNode>;
    createdTime?:number;
    permissions?:PrivilegeEnum[];
}

export interface ProjectPagination extends Project{
    permissions:PrivilegeEnum[];
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
    desc?:string;
    createdTime?:number;
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
    renderConfig?:{datepicker:DatePickerConfigParam,filters:Array<FilterConfigParam>}
}

export interface StatPagination extends Stat {
    permissions:PrivilegeEnum[];
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
    permissions:PrivilegeEnum[];
    admins?:Array<User>;
}

export interface Order {
    id?:number;
    userId?:number;
    user?:User;
    title?:string;
    createdTime?:number;
    desc?:string;
    state?:number;
    approveIds?:Array<number>;
    approveUsers?:Array<User>;
    orderType?:OrderTypeEnum;
    config?:any;
    extend?:any;
}


