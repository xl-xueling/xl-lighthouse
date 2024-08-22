import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {Department, Order, Permission, Project} from "@/types/insights-web";

export async function requestQueryList(data) :Promise<ResultData<{list:Array<Permission>,total:number}>> {
    return request({
        url:'/permission/list',
        method:'POST',
        data,
    })
}

export async function requestGrantProjectPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/project/grant',
        method:'POST',
        data,
    })
}

export async function requestReleaseProjectPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/project/release',
        method:'POST',
        data,
    })
}

export async function requestGrantMetricPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/metricset/grant',
        method:'POST',
        data,
    })
}

export async function requestReleaseMetricPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/metricset/release',
        method:'POST',
        data,
    })
}


export async function requestGrantViewPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/view/grant',
        method:'POST',
        data,
    })
}

export async function requestReleaseViewPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/view/release',
        method:'POST',
        data,
    })
}
