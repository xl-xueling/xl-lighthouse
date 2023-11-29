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

export interface ArcoTreeNode {
    key: string;
    title: string;
    children?:Array<ArcoTreeNode>;
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
    departmentId?: string|number;
    departmentName?:string;
    state?:number;
    createdTime?:number;
    avatar?:string;
    permissions: Record<string, string[]>;
}

export interface Project {
    id?:number;
    name?:string;
    departmentId:number;
    admins?:Array<number>;
    isPrivate?:boolean;
    desc?:string;
    createdTime?:number;
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
    createdTime?:number;
}

export interface StatPagination extends Stat {
    key?:number;
    group?:Group;
    project?:Project;
    department?:Department;
    permissions:PrivilegeEnum[];
}




