import {request} from "@/utils/request";
import {CustomComponent, ResultData} from "@/types/insights-common";
import {Department, Project} from "@/types/insights-web";

export async function requestQueryByIds(ids:number[]) :Promise<ResultData<Record<number,CustomComponent>>> {
    return request({
        url:'/component/queryByIds',
        method:'POST',
        ids,
    })
}