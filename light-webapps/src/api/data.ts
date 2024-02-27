import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, Project, Stat, StatData, User} from "@/types/insights-web";

export async function requestTestData(data) :Promise<ResultData<Array<StatData>>> {
    return request({
        url:'/test-data/stat',
        method:'POST',
        data,
    })
}

export async function requestData(data) :Promise<ResultData<Array<StatData>>> {
    return request({
        url:'/data/stat',
        method:'POST',
        data,
    })
}


