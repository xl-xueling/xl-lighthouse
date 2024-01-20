import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {Department, Order, Project} from "@/types/insights-web";

export async function requestQueryById(data) :Promise<ResultData<Order>> {
    return request({
        url:'/order/queryById',
        method:'POST',
        data,
    })
}

export async function requestProcess(data) :Promise<ResultData<{list:Array<Order>,total:number}>> {
    return request({
        url:'/order/process',
        method:'POST',
        data,
    })
}

export async function requestApproveList(data) :Promise<ResultData<{list:Array<Order>,total:number}>> {
    return request({
        url:'/approve/list',
        method:'POST',
        data,
    })
}

export async function requestApplyList(data) :Promise<ResultData<{list:Array<Order>,total:number}>> {
    return request({
        url:'/apply/list',
        method:'POST',
        data,
    })
}

export async function requestCreateApply(data) :Promise<ResultData<{list:Array<Order>,total:number}>> {
    return request({
        url:'/apply/create',
        method:'POST',
        data,
    })
}