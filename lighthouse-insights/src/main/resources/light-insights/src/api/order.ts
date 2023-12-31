import {request} from "@/utils/request";
import {FilterComponent, ResultData} from "@/types/insights-common";
import {Department, Order, Project} from "@/types/insights-web";

export async function requestApprove(data) :Promise<ResultData<{list:Array<Order>,total:number}>> {
    return request({
        url:'/approve/process',
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