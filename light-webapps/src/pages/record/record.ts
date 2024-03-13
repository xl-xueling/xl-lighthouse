import React from "react";
import {Badge} from "@arco-design/web-react";
import {
    OrderTypeEnum, RecordTypeEnum,
} from "@/types/insights-common";
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
    const descObject = JSON.parse(record.extend);
    if(type === RecordTypeEnum.GROUP_MESSAGE_LIMITING){
        {
            const result:LimitedRecord = {
                id:record.id,
                desc:
                    t['recordType.groupLimited.strategy'] + ": " + descObject.strategy + " , " +
                    t['recordType.groupLimited.startTime'] + ": " + formatTimeStamp(Number(descObject.startTime),DateTimeFormat) + " , "
                + t['recordType.groupLimited.endTime'] + ": " + formatTimeStamp(Number(descObject.endTime),DateTimeFormat)
                ,
            }
            return result;
        }
    }
    return {};
}