import {request} from "@/utils/request";
import {FilterComponent, ResultData} from "@/types/insights-common";
import {Department, Project} from "@/types/insights-web";

export async function requestQueryByIds(ids:number[]) :Promise<ResultData<Record<number,FilterComponent>>> {
    return request({
        url:'/component/queryByIds',
        method:'POST',
        ids,
    })
}

export async function requestList(data) :Promise<ResultData<{list:Array<FilterComponent>,total:number}>> {
    return request({
        url:'/component/list',
        method:'POST',
        data,
    })
}