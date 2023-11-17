import {List} from "@arco-design/web-react";

export class Department {
    id: number;
    name: string;
    pid: number;
    fullpath?:string;
}

export class DepartmentArcoTreeNode {
    key: string;
    title: string;
    children?:Array<DepartmentArcoTreeNode>;
}


export class DepartmentArcoFlatNode {
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