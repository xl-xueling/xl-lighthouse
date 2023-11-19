import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {User} from "@/types/insights-web";

export async function requestList(data) :Promise<ResultData<{list:Array<User>,total:number}>>  {
    return request({
        url:'/user/list',
        method:'POST',
        data,
    })
}

export async function requestQueryByIds(data:{"ids":Array<number>}) :Promise<ResultData<{list:Array<User>}>>  {
    return request({
        url:'/user/queryByIds',
        method:'POST',
        data,
    })
}

export async function requestTermList(data:string) :Promise<ResultData<{list:Array<User>,total:number}>>  {
    return request({
        url:'/user/termList',
        method:'POST',
        data,
    })
}



export async function requestUpdate(data:User) :Promise<ResultData>  {
    return request({
        url:'/user/update',
        method:'POST',
        data,
    })
}


export async function requestDelete(id:number) :Promise<ResultData>  {
    return request({
        url:'/user/delete',
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


export async function requestUserInfo():Promise<ResultData>  {
    return request({
        url:'/user/userInfo',
        method:'POST',
    })
}

