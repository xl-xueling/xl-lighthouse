import {Caller} from "@/types/caller";
import {ResultData} from "@/types/insights-common";
import {request} from "@/utils/request";
import {Resource} from "@/types/insights-web";

export async function requestCreate(data:Caller):Promise<ResultData> {
    return request({
        url:'/caller/create',
        method:'POST',
        data,
    })
}

export async function requestList(data) :Promise<ResultData<{list:Array<Caller>,total:number}>> {
    return request({
        url:'/caller/list',
        method:'POST',
        data,
    })
}

export async function requestAuthList(data) :Promise<ResultData<{list:Array<Resource>,total:number}>> {
    return request({
        url:'/permission/ownerAuthList',
        method:'POST',
        data,
    })
}

export async function requestUpdate(data:Caller) :Promise<ResultData>{
    return request({
        url:'/caller/update',
        method:'POST',
        data,
    })
}

export async function requestQueryById(data):Promise<ResultData<Caller>> {
    return request({
        url:'/caller/queryById',
        method:'POST',
        data,
    })
}

export async function requestGetSecretKey(data):Promise<ResultData<string>> {
    return request({
        url:'/caller/getSecretKey',
        method:'POST',
        data,
    })
}

export async function requestDeleteById(data):Promise<ResultData> {
    return request({
        url:'/caller/deleteById',
        method:'POST',
        data,
    })
}