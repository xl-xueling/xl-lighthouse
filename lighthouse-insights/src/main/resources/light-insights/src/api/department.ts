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


export async function dragTo(data)  {
    return request({
        url:'/department/dragTo',
        method:'POST',
        data,
    })
}

export async function updateById(data)  {
    return request({
        url:'/department/updateById',
        method:'POST',
        data,
    })
}

export async function deleteById(data)  {
    return request({
        url:'/department/deleteById',
        method:'POST',
        data,
    })
}