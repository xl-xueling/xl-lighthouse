import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {MetricSet} from "@/types/insights-web";

export async function requestCreate(data:MetricSet) :Promise<ResultData>{
    return request({
        url:'/metricset/create',
        method:'POST',
        data,
    })
}