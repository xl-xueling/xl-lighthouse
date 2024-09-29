import {User} from "@/types/insights-web";
import {PermissionEnum} from "@/types/insights-common";

export interface Caller {
    id?:number;
    name?:string;
    desc?:string;
    adminIds?:Array<number>;
    admins?:Array<User>;
    createTime?:number;
    updateTime?:number;
    permissions?:PermissionEnum[];
}