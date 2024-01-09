import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {User} from "@/types/insights-web";

export async function requestList(data) :Promise<ResultData<{list:Array<User>,total:number}>>  {
    return request({
        url:'/record/list',
        method:'POST',
        data,
    })
}



