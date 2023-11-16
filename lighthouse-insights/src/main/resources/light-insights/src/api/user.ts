import {request} from "@/utils/request";
import {ResultCode} from "@/types/insights-common";

export async function queryList(data) :Promise<ResultCode>  {
    return request({
        url:'/user/list',
        method:'POST',
        data,
    })
}


export async function requestUpdateById(data) :Promise<ResultCode>  {
    return request({
        url:'/user/updateById',
        method:'POST',
        data,
    })
}


export async function requestDeleteById(data) :Promise<ResultCode>  {
    return request({
        url:'/user/deleteById',
        method:'POST',
        data,
    })
}

export async function requestResetPasswd(data):Promise<ResultCode> {
    return request({
        url:'/user/resetPasswd',
        method:'POST',
        data,
    })
}


export async function requestChangeUState(data) :Promise<ResultCode>  {
    return request({
        url:'/user/changeState',
        method:'POST',
        data,
    })
}