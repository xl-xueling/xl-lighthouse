import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {ArcoTreeNode, Group} from "@/types/insights-web";

export const _Mock_group1:Group = Mock.mock({
    id:11,
    token:"homepage_stat_" + '@datetime',
    projectId:1,
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
    createTime:'@datetime',
});

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
                    token:"homepage_behavior_stat1",
                    projectId:101,
                    desc:"homepage user behavior stat",
                    columns:[
                        {
                            name:"userId",
                            type:1,
                            desc:"1用户ID_" + '@datetime'
                        },
                        {
                            name:"province",
                            type:1,
                            desc:"1用户省份" + '@datetime'
                        },
                        {
                            name:"city",
                            type:1,
                            desc:"1用户城市" + '@datetime'
                        },
                        {
                            name:"score",
                            type:2,
                            desc:"1得分" + '@datetime'
                        }
                    ],
                    createdTime:'@datetime',
                }
            );

            groupData[1002] = Mock.mock(
                {
                    id:1002,
                    token:"homepage_behavior_stat2",
                    projectId:101,
                    desc:"homepage user behavior stat",
                    columns:[
                        {
                            name:"userId",
                            type:1,
                            desc:"2用户ID_" + '@datetime'
                        },
                        {
                            name:"province",
                            type:1,
                            desc:"2用户省份" + '@datetime'
                        },
                        {
                            name:"city",
                            type:1,
                            desc:"2用户城市" + '@datetime'
                        },
                        {
                            name:"score",
                            type:2,
                            desc:"2得分" + '@datetime'
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


        Mock.mock('/api/v1/group/create', (params) => {
            console.log("group create submit,receive params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{
                    id:2,
                },
            };
        });

        Mock.mock('/api/v1/group/queryDimensValue', (params) => {
            console.log("group queryDimensValue,receive params:" + JSON.stringify(params));
            const dimensData:Record<string,Array<ArcoTreeNode>> = {};
            const bodyObject = JSON.parse(params.body);
            const array = bodyObject.dimensArray;
            for(let i=0;i<array.length;i++){
                const dimens = array[i];
                dimensData[dimens] = [
                    {
                        "key":"101",
                        "title":"101",
                    },
                    {
                        "key":"102",
                        "title":"102",
                    },
                ]
            }
            return {
                code:'0',
                message:'success',
                data:dimensData
            };
        });
    }
})