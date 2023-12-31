import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {ArcoTreeNode, MetricSet} from "@/types/insights-web";

export async function requestCreate(data:MetricSet) :Promise<ResultData>{
    return request({
        url:'/metricset/create',
        method:'POST',
        data,
    })
}

export async function requestList(data) :Promise<ResultData<{list:Array<MetricSet>,total:number}>> {
    return request({
        url:'/metricset/list',
        method:'POST',
        data,
    })
}

export async function requestPinList() :Promise<ResultData<Array<MetricSet>>> {
    return request({
        url:'/metricset/pinList',
        method:'POST',
    })
}

export async function requestByIds(data:{ids:Array<number>}) :Promise<ResultData<Record<number, MetricSet>>> {
    return request({
        url:'/metricset/requestByIds',
        method:'POST',
    })
}


export async function requestBinded(data) :Promise<ResultData> {
    return request({
        url:'/metricset/binded',
        method:'POST',
        data,
    })
}






