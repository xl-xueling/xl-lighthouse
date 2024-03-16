import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, LimitData, Project, Stat, StatData, User} from "@/types/insights-web";

export async function requestTestData(data) :Promise<ResultData<Array<StatData>>> {
    return request({
        url:'/test-data/stat',
        method:'POST',
        data,
    })
}

export async function requestStatData(data) :Promise<ResultData<Array<StatData>>> {
    return request({
        url:'/data/stat',
        method:'POST',
        data,
    })
}

export async function requestLimitData(data) :Promise<ResultData<Array<LimitData>>> {
    return request({
        url:'/data/limit',
        method:'POST',
        data,
    })
}


