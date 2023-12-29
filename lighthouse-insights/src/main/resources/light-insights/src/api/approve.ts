import {request} from "@/utils/request";
import {FilterComponent, ResultData} from "@/types/insights-common";
import {Department, Order, Project} from "@/types/insights-web";

export async function requestList(data) :Promise<ResultData<{list:Array<Order>,total:number}>> {
    return request({
        url:'/order/approve/list',
        method:'POST',
        data,
    })
}