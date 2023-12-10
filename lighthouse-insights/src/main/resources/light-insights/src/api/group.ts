import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {ArcoSelectNode, ArcoTreeNode, Group, Project, User} from "@/types/insights-web";

export async function requestQueryById(id:number) :Promise<ResultData<Group>> {
    return request({
        url:'/group/queryById',
        method:'POST',
        id,
    })
}


export async function requestQueryByIds(ids:number[]) :Promise<ResultData<Record<number,Group>>> {
    return request({
        url:'/group/queryByIds',
        method:'POST',
        ids,
    })
}


export async function requestQueryDimensValue(data:{groupId:number,dimensArray:string[]}):Promise<ResultData<Record<string,Array<ArcoTreeNode>>>> {
    return request({
        url:'/group/queryDimensValue',
        method:'POST',
        data,
    })
}


export async function requestDeleteByID(id:number) :Promise<ResultData> {
    return request({
        url:'/group/deleteById',
        method:'POST',
        id,
    })
}


export async function requestCreate(data) :Promise<ResultData> {
    return request({
        url:'/group/create',
        method:'POST',
        data,
    })
}




