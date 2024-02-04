import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {Department, Order, Project} from "@/types/insights-web";

export async function requestOverView() :Promise<ResultData<Record<number,Component>>> {
    return request({
        url:'/homepage/overview',
        method:'POST',
    })
}