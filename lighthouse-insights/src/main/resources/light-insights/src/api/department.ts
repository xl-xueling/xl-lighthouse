import {request} from "@/utils/request";

export async function queryAll(data?)  {
    return request({
        url:'/department/all',
        method:'POST',
        data,
    })
}

export async function add(data)  {
    return request({
        url:'/department/add',
        method:'POST',
        data,
    })
}