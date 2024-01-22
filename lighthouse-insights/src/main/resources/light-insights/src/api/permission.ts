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

export async function requestGrantMetricPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/metric/grant',
        method:'POST',
        data,
    })
}