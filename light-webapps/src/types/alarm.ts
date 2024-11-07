import {Department, User} from "@/types/insights-web";
import {CalculateMethod, NumberCompareType, ResourceTypeEnum} from "@/types/insights-common";

export interface Alarm {
    title?:string,
    uniqueCode?:string,
    divide?:boolean,
    resourceId?:number,
    resourceType?:ResourceTypeEnum,
    templateId?:number,
    desc?:string,
    dimens?:string[],
    delay?:number,
    recover?:boolean,
    conditions?:AlarmCondition[],
}

export interface AlarmCondition {
    key?:string,
    indicator?:number,
    last?:number,
    compare?:NumberCompareType,
    divide?:boolean,
    calc?:CalculateMethod,
    overall?:{
        threshold?:number,
        state?:boolean,
    },
    p0?:{
        threshold?:number,
        state?:boolean,
    },
    p1?:{
        threshold?:number,
        state?:boolean,
    },
    p2?:{
        threshold?:number,
        state?:boolean,
    },
    p3?:{
        threshold?:number,
        state?:boolean,
    },
}

export interface AlarmTemplate {
    id?:number,
    title?:string,
    templateConfig?:{
        weekdays?:Array<number>,
        startTime?:string,
        endTime?:string,
    }
    userIds?:Array<number>,
    departmentIds?:Array<number>,
    desc?:string,
    createTime?:number,
    updateTime?:number,
    userList?:Array<User>,
    departmentList?:Array<Department>,
}