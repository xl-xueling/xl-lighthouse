import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {AuthorizeInfo, Department, Order, Project} from "@/types/insights-web";

export async function requestAuthorizeInfo() :Promise<ResultData<AuthorizeInfo>> {
    return request({
        url:'/authorize/info',
        method:'POST',
    })
}

export async function requestAuthActivation(data) :Promise<ResultData<AuthorizeInfo>> {
    return request({
        url:'/authorize/activation',
        method:'POST',
        data,
    })
}


export async function requestAuthUnbind(data) :Promise<ResultData<AuthorizeInfo>> {
    return request({
        url:'/authorize/unbind',
        method:'POST',
        data,
    })
}