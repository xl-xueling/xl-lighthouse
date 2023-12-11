import Mock from 'mockjs';
import setupMock from '@/utils/setupMock';
import {ExtendMetricSet, Project} from "@/types/insights-web";

const list  = Mock.mock([
        {
            id: 101,
            'title': '@word() @word() @word()',
            'description': '@sentence()',
            "createdTime":'@datetime',
        },
        {
            id: 102,
            'title': '@word() @word() @word()',
            'description': '@sentence()',
            "createdTime":'@datetime',
        },
        {
            id: 103,
            'title': '@word() @word() @word()',
            'description': '@sentence()',
            "createdTime":'@datetime',
        },
        {
            id: 104,
            'title': '@word() @word() @word()',
            'description': '@sentence()',
            "createdTime":'@datetime',
        },
    ]);


setupMock({

    setup: () => {
        Mock.mock(new RegExp('/api/v1/metricset/create'), (params) => {
            console.log("receive create params,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{
                    id:1,
                },
            };
        });

        Mock.mock(new RegExp('/api/v1/metricset/list'), (params) => {
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

        Mock.mock(new RegExp('/api/v1/metricset/pinList'), (params) => {
            console.log("request pinList,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:list,
            };
        });

        Mock.mock(new RegExp('/api/v1/metricset/queryExtendInfoByIds'), (params) => {
            console.log("request pinList,params:" + JSON.stringify(params));
            const record: Record<number, ExtendMetricSet> = {};
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
    }
});