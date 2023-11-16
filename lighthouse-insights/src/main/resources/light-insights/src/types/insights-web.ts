export class Department {
    id: number|string;
    name: string;
    pid: number|string;
    fullpath?:string;
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