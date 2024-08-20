import {request} from "@/utils/request";
import {RenderFilterConfig, ResultData} from "@/types/insights-common";
import {ArcoTreeNode, Group, Stat} from "@/types/insights-web";

export async function requestDefaultList(data) :Promise<ResultData<{list:Array<RenderFilterConfig>,total:number}>> {
    return request({
        url:'/filter/default/list',
        method:'POST',
        data,
    })
}

export async function requestCustomList(data) :Promise<ResultData<{list:Array<RenderFilterConfig>,total:number}>> {
    return request({
        url:'/filter/custom/list',
        method:'POST',
        data,
    })
}