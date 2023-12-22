import Mock from 'mockjs';
import setupMock from '@/utils/setupMock';
import {MetricSet, Project} from "@/types/insights-web";

const list  = Mock.mock([
        {
            id: 1,
            'title': '@word() @word() @word()',
            'description': '@sentence()',
            "createdTime":'@datetime',
            "structure":{
                "key":1,
                'title': '@word() @word() @word()',
            }
        },
        {
            id: 2,
            'title': '@word() @word() @word()',
            'description': '@sentence()',
            "createdTime":'@datetime',
            "structure":{
                "key":2,
                'title': '@word() @word() @word()',
            }
        },

    ]);


setupMock({

    setup: () => {
        Mock.mock('/api/v1/metricset/create', (params) => {
            console.log("receive create params,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{
                    id:1,
                },
            };
        });

        Mock.mock('/api/v1/metricset/list', (params) => {
            console.log("request list,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{
                    list: list,
                    total: 100,
                },
            };
        });

        Mock.mock('/api/v1/metricset/pinList', (params) => {
            console.log("request pinList,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:list,
            };
        });

        Mock.mock('/api/v1/metricset/requestByIds', (params) => {
            console.log("request pinList,params:" + JSON.stringify(params));
            const record: Record<number, MetricSet> = {};
            record[101] = Mock.mock({
                id: 101,
                'title': '@word() @word() @word()',
                'description': '@sentence()',
                "createdTime":'@datetime',
                "structure":{
                    "key":101,
                    'title': '@word() @word() @word()',
                }
            });
            return {
                code:'0',
                message:'success',
                data:record,
            };
        });

        Mock.mock('/api/v1/metricset/binded', (params) => {
            console.log("receive binded params,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{},
            };
        });
    }
});