import {request} from "@/utils/request";

export async function queryList(data)  {
    return request({
        url:'/user/list',
        method:'POST',
        data,
    })
}