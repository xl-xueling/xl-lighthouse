import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {Department, Order, Project} from "@/types/insights-web";

export async function requestQueryByIds(ids:number[]) :Promise<ResultData<Record<number,Component>>> {
    return request({
        url:'/component/queryByIds',
        method:'POST',
        ids,
    })
}

export async function requestList(data) :Promise<ResultData<{list:Array<Component>,total:number}>> {
    return request({
        url:'/component/list',
        method:'POST',
        data,
    })
}

export async function requestVerify(data) :Promise<ResultData> {
    return request({
        url:'/component/verify',
        method:'POST',
        data,
    })
}

export async function requestCreate(data) :Promise<ResultData> {
    return request({
        url:'/component/create',
        method:'POST',
        data,
    })
}

export async function requestUpdate(data) :Promise<ResultData> {
    return request({
        url:'/component/update',
        method:'POST',
        data,
    })
}