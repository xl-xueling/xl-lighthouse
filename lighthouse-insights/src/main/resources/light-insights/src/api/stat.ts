import {request} from "@/utils/request";
import {ArcoTreeNode, ResultData} from "@/types/insights-common";
import {Group, Project, Stat, User} from "@/types/insights-web";


export async function requestList(data) :Promise<ResultData<{list:Array<Stat>,total:number}>> {
    return request({
        url:'/stat/list',
        method:'POST',
        data,
    })
}

export async function requestQueryById(id:number) :Promise<ResultData<Stat>> {
    return request({
        url:'/stat/queryById',
        method:'POST',
        id,
    })
}

export async function requestQueryByGroupId(id:number) :Promise<ResultData<{list:Array<Stat>}>> {
    return request({
        url:'/stat/queryByGroupId',
        method:'POST',
        id,
    })
}

export async function requestDeleteByID(id:number) :Promise<ResultData> {
    return request({
        url:'/stat/deleteById',
        method:'POST',
        id,
    })
}





