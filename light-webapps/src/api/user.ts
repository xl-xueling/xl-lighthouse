import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {User} from "@/types/insights-web";

export async function requestLogin(data):Promise<ResultData> {
    return request({
        url:'/user/login',
        method:'POST',
        data,
    })
}

export async function requestRegister(data)  {
    return request({
        url:'/user/register',
        method:'POST',
        data,
    })
}

export async function requestList(data) :Promise<ResultData<{list:Array<User>,total:number}>>  {
    return request({
        url:'/user/list',
        method:'POST',
        data,
    })
}

export async function requestFetchUserInfo():Promise<ResultData<User>>  {
    return request({
        url:'/user/fetchUserInfo',
        method:'POST',
    })
}


export async function requestQueryByIds(ids:Array<number>) :Promise<ResultData<Record<number,User>>>  {
    return request({
        url:'/user/queryByIds',
        method:'POST',
        ids,
    })
}

export async function requestTermList(data) :Promise<ResultData<Array<User>>>  {
    return request({
        url:'/user/termList',
        method:'POST',
        data,
    })
}

export async function requestUpdateById(data:User) :Promise<ResultData>  {
    return request({
        url:'/user/updateById',
        method:'POST',
        data,
    })
}

export async function requestChangePassword(data) :Promise<ResultData>  {
    return request({
        url:'/user/changePassword',
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


export async function requestChangeState(data) :Promise<ResultData>  {
    return request({
        url:'/user/changeState',
        method:'POST',
        data,
    })
}



