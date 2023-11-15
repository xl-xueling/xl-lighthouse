import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';

setupMock({
    setup: () => {
        const { list } = Mock.mock({
            'list|100': [
                {
                    "id": /[0-9]{8}/,
                    "userName":'@name()',
                    "email":'@EMAIL()',
                    "phone":'@Phone()',
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