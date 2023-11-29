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

export async function requestFetchUserInfo():Promise<ResultData>  {
    return request({
        url:'/user/fetchUserInfo',
        method:'POST',
    })
}


export async function requestQueryByIds(data:{ids:Array<number>}) :Promise<ResultData<{list:Array<User>}>>  {
    return request({
        url:'/user/queryByIds',
        method:'POST',
        data,
    })
}

export async function requestTermList(input:string) :Promise<ResultData<{list:Array<User>,total:number}>>  {
    return request({
        url:'/user/termList',
        method:'POST',
        input,
    })
}

export async function requestUpdate(data:User) :Promise<ResultData>  {
    return request({
        url:'/user/update',
        method:'POST',
        data,
    })
}


export async function requestDeleteById(id:number) :Promise<ResultData>  {
    return request({
        url:'/user/deleteById',
        method:'POST',
        id,
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



