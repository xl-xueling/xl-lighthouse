import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {generatePermission, routes} from "@/routes";


setupMock({
    setup: () => {
        Mock.mock(new RegExp('/api/v1/group/queryById'), (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            const groupInfo = Mock.mock({
                id:1023,
                token:"homepage_stat_1",
                projectId:101,
                desc:"homepage user behavior stat",
                columns:[
                    {
                        name:"userId",
                        type:1,
                        desc:"用户ID"
                    },
                    {
                        name:"province",
                        type:1,
                        desc:"用户省份"
                    },
                    {
                        name:"city",
                        type:1,
                        desc:"用户城市"
                    },
                    {
                        name:"score",
                        type:2,
                        desc:"得分"
                    }
                ],
                createdTime:'@datetime',
            });

            return {
                code:'0',
                message:'success',
                data:groupInfo
            };
        });


    }
})