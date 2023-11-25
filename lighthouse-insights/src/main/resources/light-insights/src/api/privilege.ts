import {ResultData} from "@/types/insights-common";
import {PrivilegeEnum, Project} from "@/types/insights-web";
import {request} from "@/utils/request";


export async function requestPrivilegeCheck(data): Promise<ResultData<Record<number,Array<PrivilegeEnum>>>> {
   return request({
        url:'/privilege/check',
        method:'POST',
        data,
    })
}