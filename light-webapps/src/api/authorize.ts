import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {AuthorizeInfo, Department, Order, Project} from "@/types/insights-web";

export async function requestAuthorizeInfo() :Promise<ResultData<AuthorizeInfo>> {
    return request({
        url:'/authorize/info',
        method:'POST',
    })
}