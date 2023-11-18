import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Project} from "@/types/insights-web";

export async function requestList(data) :Promise<ResultData<{list:Array<Project>,total:number}>> {
    return request({
        url:'/project/list',
        method:'POST',
        data,
    })
}

export async function requestCreate(data:Project):Promise<ResultData> {
    return request({
        url:'/project/create',
        method:'POST',
        data,
    })
}

export async function requestUpdate(data:Project):Promise<ResultData> {
    return request({
        url:'/project/update',
        method:'POST',
        data,
    })
}

export async function requestDelete(id:number):Promise<ResultData> {
    return request({
        url:'/project/delete',
        method:'POST',
        id,
    })
}


