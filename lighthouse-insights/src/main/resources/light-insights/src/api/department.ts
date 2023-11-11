import {request} from "@/utils/request";

export async function queryAll(data)  {
    return request({
        url:'/department/2all',
        method:'POST',
        data,
    })
}