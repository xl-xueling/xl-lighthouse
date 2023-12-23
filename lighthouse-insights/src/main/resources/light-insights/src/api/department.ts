import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Department} from "@/types/insights-web";

export async function requestQueryAll() :Promise<ResultData<Array<Department>>>{
    return request({
        url:'/department/all',
        method:'POST',
    })
}

export async function requestCreate(data) :Promise<ResultData>{
    return request({
        url:'/department/create',
        method:'POST',
        data,
    })
}


export async function requestDragTo(data) :Promise<ResultData>{
    return request({
        url:'/department/dragTo',
        method:'POST',
        data,
    })
}

export async function requestUpdateById(data):Promise<ResultData> {
    return request({
        url:'/department/updateById',
        method:'POST',
        data,
    })
}

export async function requestDelete(data) :Promise<ResultData>{
    return request({
        url:'/department/delete',
        method:'POST',
        data,
    })
}