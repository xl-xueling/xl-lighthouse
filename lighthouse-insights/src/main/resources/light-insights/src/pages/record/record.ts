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


export interface LimitedRecord {
    id?:number,
    startTime?:number,
    endTime?:number,
    desc?:string,
}

export function translateRecord(record:Record){
    const type = record.recordType;
    if(type === RecordTypeEnum.GROUP_LIMITED){
        {

        }
    }
}