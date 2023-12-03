import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {generatePermission, routes} from "@/routes";
import {Group, Project} from "@/types/insights-web";


setupMock({
    setup: () => {
        Mock.mock('/api/v1/group/queryById', (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            const groupInfo = Mock.mock({
                id:1023,
                token:"homepage_stat_1" + '@datetime',
                projectId:101,
                desc:"homepage user behavior stat",
                columns:[
                    {
                        name:"userId",
                        type:1,
                        desc:"用户ID_" + '@datetime'
                    },
                    {
                        name:"province",
                        type:1,
                        desc:"用户省份" + '@datetime'
                    },
                    {
                        name:"city",
                        type:1,
                        desc:"用户城市" + '@datetime'
                    },
                    {
                        name:"score",
                        type:2,
                        desc:"得分" + '@datetime'
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


        Mock.mock('/api/v1/group/queryByIds', (params) => {
            console.log("queryByIds,receive param is:" + JSON.stringify(params));
            const groupData: Record<number, Group> = {};
            groupData[1001] = Mock.mock(
                {
                    id:1001,
                    token:"homepage_behavior_stat",
                    projectId:101,
                    desc:"homepage user behavior stat",
                    columns:[
                        {
                            name:"userId",
                            type:1,
                            desc:"用户ID_" + '@datetime'
                        },
                        {
                            name:"province",
                            type:1,
                            desc:"用户省份" + '@datetime'
                        },
                        {
                            name:"city",
                            type:1,
                            desc:"用户城市" + '@datetime'
                        },
                        {
                            name:"score",
                            type:2,
                            desc:"得分" + '@datetime'
                        }
                    ],
                    createdTime:'@datetime',
                }
            );

            groupData[1002] = Mock.mock(
                {
                    id:1002,
                    token:"homepage_behavior_stat",
                    projectId:101,
                    desc:"homepage user behavior stat",
                    columns:[
                        {
                            name:"userId",
                            type:1,
                            desc:"用户ID_" + '@datetime'
                        },
                        {
                            name:"province",
                            type:1,
                            desc:"用户省份" + '@datetime'
                        },
                        {
                            name:"city",
                            type:1,
                            desc:"用户城市" + '@datetime'
                        },
                        {
                            name:"score",
                            type:2,
                            desc:"得分" + '@datetime'
                        }
                    ],
                    createdTime:'@datetime',
                }
            );

            return {
                code:'0',
                message:'success',
                data:groupData
            };
        });


        Mock.mock(new RegExp('/api/v1/group/create'), (params) => {
            console.log("group create submit,receive params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{},
            };
        });
    }
})