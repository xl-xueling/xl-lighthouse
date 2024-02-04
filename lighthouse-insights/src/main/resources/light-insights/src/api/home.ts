import {request} from "@/utils/request";
import {Component, ResultData} from "@/types/insights-common";
import {Department, HomeData, Order, Project} from "@/types/insights-web";

export async function requestOverView() :Promise<ResultData<HomeData>> {
    return request({
        url:'/homepage/overview',
        method:'POST',
    })
}