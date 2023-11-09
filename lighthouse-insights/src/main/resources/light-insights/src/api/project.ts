import {request} from "@/utils/request";

export async function queryList(data)  {
    return request({
        url:'/project/list',
        method:'POST',
        data,
    })
}