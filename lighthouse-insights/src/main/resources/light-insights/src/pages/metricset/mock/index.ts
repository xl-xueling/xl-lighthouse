import Mock from 'mockjs';
import setupMock from '@/utils/setupMock';
import {Project} from "@/types/insights-web";

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
    }
});