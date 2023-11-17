import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Department} from "@/types/insights-web";

export async function queryAll(data?) :Promise<ResultData<Array<Department>>>{
    return request({
        url:'/department/all',
        method:'POST',
        data,
    })
}

export async function add(data) :Promise<ResultData>{
    return request({
        url:'/department/add',
        method:'POST',
        data,
    })
}


export async function dragTo(data) :Promise<ResultData>{
    return request({
        url:'/department/dragTo',
        method:'POST',
        data,
    })
}

export async function updateById(data):Promise<ResultData> {
    return request({
        url:'/department/updateById',
        method:'POST',
        data,
    })
}

export async function deleteById(data) :Promise<ResultData>{
    return request({
        url:'/department/deleteById',
        method:'POST',
        data,
    })
}