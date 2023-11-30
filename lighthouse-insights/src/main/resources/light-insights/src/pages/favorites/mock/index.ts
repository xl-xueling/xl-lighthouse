import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';

setupMock({
    setup: () => {
        Mock.mock('/api/v1/favorites/queryProjectIds', (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:[12,13,14]
            };
        });


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
    }
})