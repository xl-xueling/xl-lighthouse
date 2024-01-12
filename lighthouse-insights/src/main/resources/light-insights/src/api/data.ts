import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, Project, Stat, User} from "@/types/insights-web";

export async function requestTestData(data) :Promise<ResultData<{list:Array<Stat>,total:number}>> {
    return request({
        url:'/test-data/stat',
        method:'POST',
        data,
    })
}

export async function requestData(data) :Promise<ResultData<{list:Array<Stat>,total:number}>> {
    return request({
        url:'/data/stat',
        method:'POST',
        data,
    })
}


