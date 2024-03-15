import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, Project, Stat, User} from "@/types/insights-web";

export async function requestEnableDebugMode(data):Promise<ResultData> {
    return request({
        url:'/track/enableDebugMode',
        method:'POST',
        data,
    })
}

export async function requestDisableDebugMode(data):Promise<ResultData> {
    return request({
        url:'/track/disableDebugMode',
        method:'POST',
        data,
    })
}

export async function requestFetchTrackMessages(data):Promise<ResultData<Array<Map<string,object>>>> {
    return request({
        url:'/track/messages',
        method:'POST',
        data,
    })
}