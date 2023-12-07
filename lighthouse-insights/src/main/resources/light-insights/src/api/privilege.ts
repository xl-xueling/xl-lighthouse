import {ResultData} from "@/types/insights-common";
import {GrantPrivileges, PrivilegeEnum, Project} from "@/types/insights-web";
import {request} from "@/utils/request";


export async function requestPrivilegeCheck(data): Promise<ResultData<Record<number,Array<PrivilegeEnum>>>> {
   return request({
        url:'/privilege/check',
        method:'POST',
        data,
    })
}


export async function requestGrantPrivilege(data:GrantPrivileges): Promise<ResultData> {
    return request({
        url:'/privilege/grant',
        method:'POST',
        data,
    })
}