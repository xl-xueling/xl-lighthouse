import React from "react";
import {Badge} from "@arco-design/web-react";
import {
    ApproveStateEnum,
    ComponentTypeEnum,
    OrderStateEnum,
    OrderTypeEnum, RecordTypeEnum,
    RoleTypeEnum,
    StatExpiredEnum,
    StatStateEnum,
    UserStateEnum
} from "@/types/insights-common";
import {PiDiamondsFour} from "react-icons/pi";
import {CiViewTable} from "react-icons/ci";
import {IconTag} from "@arco-design/web-react/icon";
import {LuLayers} from "react-icons/lu";
import {Record} from "@/types/insights-web";
import {DateTimeFormat, formatTimeStamp} from "@/utils/date";


export interface LimitedRecord {
    id?:number,
    startTime?:number,
    endTime?:number,
    desc?:string,
}

export function translateRecord(t: any,record:Record){
    const type = record.recordType;
    const descObject = JSON.parse(record.desc);
    if(type === RecordTypeEnum.GROUP_MESSAGE_LIMITED){
        {
            const result:LimitedRecord = {
                id:record.id,
                desc: t['recordType.groupLimited.startTime'] + ": " + formatTimeStamp(Number(descObject.startTime),DateTimeFormat) + " , "
                + t['recordType.groupLimited.endTime'] + ": " + formatTimeStamp(Number(descObject.endTime),DateTimeFormat)
                ,
            }
            return result;
        }
    }
    return {};
}