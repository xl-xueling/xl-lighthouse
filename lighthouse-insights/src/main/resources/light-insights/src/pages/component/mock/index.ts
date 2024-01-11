import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {Project, Stat} from "@/types/insights-web";
import {_Mock_project1} from "@/pages/project/mock";
import {_Mock_user1, _Mock_user2, _Mock_user3} from "@/pages/user/mock";
import {Component} from "@/types/insights-common";

const _Mock_component1:Component = Mock.mock({
    id:101,
    renderType:5,
    title:'省份选择组件1',
    config: [
        {
            label:'山东',
            value:'101',
        },
        {
            label:'北京',
            value:'102',
        }
    ]
});

const _Mock_component2:Component = Mock.mock({
    id:102,
    renderType:5,
    title:'省份选择组件2',
    config: [
        {
            label:'山东',
            value:'101',
        },
        {
            label:'北京',
            value:'102',
        }
    ]
});


setupMock({
    setup: () => {
        Mock.mock('/api/v1/component/queryByIds', (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            const componentData: Record<number, Component> = {};
            componentData[1] = _Mock_component1;
            return {
                code: '0',
                message: 'success',
                data: componentData
            };
        });

        Mock.mock('/api/v1/component/list', (params) => {
            console.log("receive query params 2,params:" + JSON.stringify(params));
            return {
                code:0,
                message:'success',
                data:{
                    list: [_Mock_component1,_Mock_component2],
                    total: 100,
                },
            };
        });
    }
})