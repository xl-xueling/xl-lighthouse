import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, Project, Stat, User} from "@/types/insights-web";


export async function requestList(data) :Promise<ResultData<{list:Array<Stat>,total:number}>> {
    return request({
        url:'/stat/list',
        method:'POST',
        data,
    })
}

export async function requestCreate(data):Promise<ResultData> {
    return request({
        url:'/stat/create',
        method:'POST',
        data,
    })
}

export async function requestUpdate(data):Promise<ResultData> {
    return request({
        url:'/stat/update',
        method:'POST',
        data,
    })
}

export async function requestChangeState(data):Promise<ResultData> {
    return request({
        url:'/stat/changeState',
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

export async function requestQueryByIds(data:{ids:number[]}) :Promise<ResultData<Record<number,Stat>>> {
    return request({
        url:'/stat/queryByIds',
        method:'POST',
        data,
    })
}





