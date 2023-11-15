import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';

setupMock({
    setup: () => {
        const { list } = Mock.mock({
            'list|100': [
                {
                    id: /[0-9]{8}/,
                    'contentType|0-2': 0,
                    'filterType|0-1': 0,
                    'count|0-2000': 0,
                    'createdTime|1-60': 0,
                    'status|0-1': 0,
                    'createUser':0,
                    "userName":'@name()',
                    "email":'@EMAIL()',
                    "departmentId":2,
                    "state|0-3":0,
                    "createdTime":'@datetime',
                },
            ],
        });

        Mock.mock(new RegExp('/api/v1/user/list'), (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            console.log("list size:" + list.length)
            return {
                code:'0',
                message:'success',
                data:{
                    'list':list,
                     'total':100,
                }
            };
        });
    }
})