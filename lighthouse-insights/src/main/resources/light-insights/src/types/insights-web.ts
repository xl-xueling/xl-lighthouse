import {List} from "@arco-design/web-react";

export enum PrivilegeEnum {
    ADMIN = 1,
    USER = 2,
    META_TABLE_ADMIN = 3,
    META_TABLE_USER = 4,
    STAT_PROJECT_ADMIN = 5,
    STAT_PROJECT_USER = 6,
    STAT_ITEM_USER = 7,
    STAT_ITEM_ADMIN = 8,
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
    departmentName?:string;
    state?:number;
    createdTime?:number;
    avatar?:string;
    permissions?: Record<string, string[]>;
}

export interface Project {
    id?:number;
    name?:string;
    departmentId?:number;
    desc?:string;
    isPrivate?:number;
    adminIds?:Array<number>;
    createdTime?:number;
}

export interface ProjectPagination extends Project{
    permissions:PrivilegeEnum[];
    department:Department;
    admins:Array<User>;
}

export interface Column {
    key?:number|string;
    name?:string;
    type?:number;
    desc?:string;
}

export class Group {
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
    adminIds?:Array<number>;
    createdTime?:number;
}

export interface StatPagination extends Stat {
    key?:number;
    group?:Group;
    project?:Project;
    department?:Department;
    permissions:PrivilegeEnum[];
    admins:Array<User>;
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


