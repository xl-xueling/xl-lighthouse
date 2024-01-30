import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {ArcoTreeNode, Indicator, MetricSet, Relation} from "@/types/insights-web";

export async function requestCreate(data:MetricSet) :Promise<ResultData>{
    return request({
        url:'/metricset/create',
        method:'POST',
        data,
    })
}

export async function requestUpdate(data:MetricSet) :Promise<ResultData>{
    return request({
        url:'/metricset/update',
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

export async function requestDeleteById(data):Promise<ResultData> {
    return request({
        url:'/metricset/deleteById',
        method:'POST',
        data,
    })
}

export async function requestFixedById(data):Promise<ResultData> {
    return request({
        url:'/metricset/fixedById',
        method:'POST',
        data,
    })
}

export async function requestBindList(data) :Promise<ResultData<{list:Array<Relation>,total:number}>> {
    return request({
        url:'/metricset/bindlist',
        method:'POST',
        data,
    })
}
export async function requestBindRemove(data):Promise<ResultData> {
    return request({
        url:'/metricset/bindRemove',
        method:'POST',
        data,
    })
}

export async function requestUpdateStructure(data):Promise<ResultData> {
    return request({
        url:'/metricset/updateStructure',
        method:'POST',
        data,
    })
}

export async function requestResetStructure(data):Promise<ResultData> {
    return request({
        url:'/metricset/resetStructure',
        method:'POST',
        data,
    })
}

export async function requestIndicatorList(data):Promise<ResultData<{list:Array<Indicator>,total:number}>> {
    return request({
        url:'/metricset/indicatorList',
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

export async function requestQueryById(data) :Promise<ResultData<MetricSet>> {
    return request({
        url:'/metricset/queryById',
        method:'POST',
        data,
    })
}


export async function requestBinded(data) :Promise<ResultData> {
    return request({
        url:'/metricset/binded',
        method:'POST',
        data,
    })
}






