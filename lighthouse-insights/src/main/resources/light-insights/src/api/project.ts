import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Project} from "@/types/insights-web";

export async function requestQueryList(data) :Promise<ResultData<{list:Array<Project>,total:number}>> {
    return request({
        url:'/project/list',
        method:'POST',
        data,
    })
}