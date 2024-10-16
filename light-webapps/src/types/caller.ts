import {User} from "@/types/insights-web";
import {CallerStateEnum, PermissionEnum, ResourceTypeEnum} from "@/types/insights-common";

export interface Caller {
    id?:number;
    name?:string;
    desc?:string;
    state?:CallerStateEnum;
    departmentId?:number;
    adminIds?:Array<number>;
    admins?:Array<User>;
    createTime?:number;
    updateTime?:number;
    permissions?:PermissionEnum[];
}

export interface AuthRecord {
    id?:number;
    resourceId?:number;
    resourceType?:ResourceTypeEnum;
    expireTime?:number;
    createTime?:number;
    updateTime?:number;
    extend?:any;
}