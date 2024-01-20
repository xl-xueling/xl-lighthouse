import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {_Mock_user1, _Mock_user2, _Mock_user3} from "@/pages/user/mock";
import {OrderTypeEnum} from "@/types/insights-common";
import {_Mock_project1} from "@/pages/project/mock";

export const _Mock_application_order1  =  Mock.mock({
    id: 1,
    'title': '@word() @word() @word()',
    "createdTime":'@datetime',
    'desc':'@sentence()',
    'orderType':OrderTypeEnum.PROJECT_ACCESS,
    'config':null,
    'extend':{
        "project":_Mock_project1,
    },
});

export const _Mock_application_order2  = Mock.mock({
    id: 2,
    'title': '@word() @word() @word()',
    "createdTime":'@datetime',
    'desc':'@sentence()',
    'orderType':OrderTypeEnum.PROJECT_ACCESS,
    'config':null,
    'extend':{
        "project":_Mock_project1,
    },
});

setupMock({

    setup: () => {
        Mock.mock(new RegExp('/order/application/list'), (params) => {
            const list = [_Mock_application_order1,_Mock_application_order2];
            return {
                code:'0',
                message:'success',
                data:{
                    list:list,
                    total:100,
                }
            };
        });
    },
});
