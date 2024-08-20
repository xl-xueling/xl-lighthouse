import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {ArcoTreeNode, Group} from "@/types/insights-web";

export async function requestList(data) :Promise<ResultData<{list:Array<Group>,total:number}>> {
    return request({
        url:'/group/list',
        method:'POST',
        data,
    })
}

export async function requestQueryById(data) :Promise<ResultData<Group>> {
    return request({
        url:'/group/queryById',
        method:'POST',
        data,
    })
}


export async function requestQueryDimensValue(data:{groupId:number,dimensArray:string[]}):Promise<ResultData<Record<string,Array<ArcoTreeNode>>>> {
    return request({
        url:'/group/queryDimensValue',
        method:'POST',
        data,
    })
}


export async function requestDeleteById(data) :Promise<ResultData> {
    return request({
        url:'/group/deleteById',
        method:'POST',
        data,
    })
}


export async function requestCreate(data) :Promise<ResultData> {
    return request({
        url:'/group/create',
        method:'POST',
        data,
    })
}

export async function requestUpdate(data) :Promise<ResultData> {
    return request({
        url:'/group/update',
        method:'POST',
        data,
    })
}


export async function requestGetSecretKey(data) :Promise<ResultData<string>> {
    return request({
        url:'/group/getSecretKey',
        method:'POST',
        data,
    })
}




