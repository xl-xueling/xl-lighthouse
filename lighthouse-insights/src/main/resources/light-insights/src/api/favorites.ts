import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, Project, User} from "@/types/insights-web";

export async function requestQueryProjectIds() :Promise<ResultData<Array<number>>> {
    return request({
        url:'/favorites/queryProjectIds',
        method:'POST',
    })
}


export async function requestQueryStatIds() :Promise<ResultData<Array<number>>> {
    return request({
        url:'/favorites/queryStatIds',
        method:'POST',
    })
}