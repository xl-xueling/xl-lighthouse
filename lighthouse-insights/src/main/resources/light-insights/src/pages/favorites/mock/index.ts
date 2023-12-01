import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {blockMainThread} from "@/utils/util";

setupMock({
    setup: () => {
        Mock.mock('/api/v1/favorites/queryProjectIds', (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:[101,102,14]
            };
        });

        function sleep (time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        }


        Mock.mock('/api/v1/favorites/queryStatIds', (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:[
                    10111,10211,10311
                ]
            };

        });

        Mock.mock('/api/v1/favorites/favoriteProject', (params) => {
            console.log("receive favoriteProject params,params:" + JSON.stringify(params));
            return {
                code:0,
                message:'success',
                data:{},
            };
        });

        Mock.mock('/api/v1/favorites/unFavoriteProject', (params) => {
            console.log("receive unFavoriteProject params,params:" + JSON.stringify(params));
            return {
                code:0,
                message:'success',
                data:{},
            };
        });

        Mock.mock('/api/v1/favorites/favoriteStat', (params) => {
            console.log("receive favoriteStat params,params:" + JSON.stringify(params));
            return {
                code:0,
                message:'success',
                data:{},
            };
        });

        Mock.mock('/api/v1/favorites/unFavoriteStat', (params) => {
            console.log("receive unFavoriteStat params,params:" + JSON.stringify(params));
            return {
                code:0,
                message:'success',
                data:{},
            };
        });
    }
})