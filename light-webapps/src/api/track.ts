import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, Project, Stat, User} from "@/types/insights-web";

export async function requestTrackStat(data) :Promise<ResultData> {
    return request({
        url:'/track/stat',
        method:'POST',
        data,
    })
}