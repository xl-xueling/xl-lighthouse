import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Record, User} from "@/types/insights-web";

export async function requestList(data) :Promise<ResultData<{list:Array<Record>,total:number}>>  {
    return request({
        url:'/record/list',
        method:'POST',
        data,
    })
}



