import Mock from 'mockjs';
import setupMock from '@/utils/setupMock';
import {Project} from "@/types/insights-web";

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
            console.log("receive create params,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{
                    list: list,
                    total: 100,
                },
            };
        });
    }
});