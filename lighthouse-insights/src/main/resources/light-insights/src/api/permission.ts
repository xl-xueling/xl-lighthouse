import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {Department, Order, Project} from "@/types/insights-web";

export async function requestQueryList(data) :Promise<ResultData<Order>> {
    return request({
        url:'/permission/list',
        method:'POST',
        data,
    })
}

export async function requestGrantPermission(data) :Promise<ResultData<Order>> {
    return request({
        url:'/permission/grant',
        method:'POST',
        data,
    })
}