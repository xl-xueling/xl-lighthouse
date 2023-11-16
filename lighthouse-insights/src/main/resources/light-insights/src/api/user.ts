import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";

export async function queryList(data) :Promise<ResultData>  {
    return request({
        url:'/user/list',
        method:'POST',
        data,
    })
}


export async function requestUpdateById(data) :Promise<ResultData>  {
    return request({
        url:'/user/updateById',
        method:'POST',
        data,
    })
}


export async function requestDeleteById(data) :Promise<ResultData>  {
    return request({
        url:'/user/deleteById',
        method:'POST',
        data,
    })
}

export async function requestResetPasswd(data):Promise<ResultData> {
    return request({
        url:'/user/resetPasswd',
        method:'POST',
        data,
    })
}


export async function requestChangeUState(data) :Promise<ResultData>  {
    return request({
        url:'/user/changeState',
        method:'POST',
        data,
    })
}


export async function requestUserInfo():Promise<ResultData>  {
    return request({
        url:'/user/userInfo',
        method:'POST',
    })
}

