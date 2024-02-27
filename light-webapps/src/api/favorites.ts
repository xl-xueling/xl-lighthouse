import {request} from "@/utils/request";
import {ResultData} from "@/types/insights-common";
import {Group, Project, User} from "@/types/insights-web";

export async function requestQueryProjectIds() :Promise<ResultData<Array<number>>> {
    return request({
        url:'/favorites/queryProjectIds',
        method:'POST',
    })
}

export async function requestFavoriteProject(id:number):Promise<ResultData> {
    return request({
        url:'/favorites/favoriteProject',
        method:'POST',
        id,
    })
}

export async function requestUnFavoriteProject(id:number):Promise<ResultData> {
    return request({
        url:'/favorites/unFavoriteProject',
        method:'POST',
        id,
    })
}

export async function requestQueryStatIds() :Promise<ResultData<Array<number>>> {
    return request({
        url:'/favorites/queryStatIds',
        method:'POST',
    })
}


export async function requestFavoriteStat(id:number):Promise<ResultData> {
    return request({
        url:'/favorites/favoriteStat',
        method:'POST',
        id,
    })
}

export async function requestUnFavoriteStat(id:number):Promise<ResultData> {
    return request({
        url:'/favorites/unFavoriteStat',
        method:'POST',
        id,
    })
}