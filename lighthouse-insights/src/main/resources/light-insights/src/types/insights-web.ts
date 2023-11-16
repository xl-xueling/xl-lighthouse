import {List} from "@arco-design/web-react";

export class Department {
    id: number;
    name: string;
    pid: number|string;
    fullpath?:string;
}

export class DepartmentArcoTreeNode {
    key: string;
    title: string;
    children?:Array<DepartmentArcoTreeNode>;
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