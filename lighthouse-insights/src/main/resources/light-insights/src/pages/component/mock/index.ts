import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {Project, Stat} from "@/types/insights-web";
import {_Mock_project1} from "@/pages/project/mock";
import {_Mock_user1, _Mock_user2, _Mock_user3} from "@/pages/user/mock";
import {CustomComponent} from "@/types/insights-common";

const _Mock_component1:CustomComponent = Mock.mock({
    id:1,
    renderType:5,
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
        Mock.mock(new RegExp('/api/v1/component/queryByIds'), (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            const componentData: Record<number, CustomComponent> = {};
            componentData[1] = _Mock_component1;
            return {
                code: '0',
                message: 'success',
                data: componentData
            };
        });
    }
})