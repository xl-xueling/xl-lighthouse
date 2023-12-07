import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';

const { list } = Mock.mock({
    'list|100': [
        {
            id: /[0-9]{8}[-][0-9]{4}/,
            name: () =>
                Mock.Random.pick([
                    '测试工程1',
                    '测试工程2',
                    '测试工程3',
                ]),
            'departmentId|0-2': 0,
            'isPrivate|0-1': 0,
            'desc':'adswwasswwwegase',
            'admins':['1','2'],
            'createdTime|1-60': 0,
        },
    ],
});


setupMock({
    setup: () => {

        type Person = {
            name: string;
            age: number;
        };

        const person: Record<string, Person> = {
            alice: {
                name: "Alice",
                age: 25,
            },
            bob: {
                name: "Bob",
                age: 30,
            },
        };

        Mock.mock(new RegExp('/api/v1/privilege/check'), (data) => {
            const {type,items} = JSON.parse(data.body);
            const personData: Record<number, Array<number>> = {};
            if(type == "project"){
               items.forEach(x => {
                   personData[parseInt(x)] = [1,2,3,4,5,6,7,8,9,10];
               })
                return {
                    code:0,
                    message:'success',
                    data:personData,
                };
            }else if(type == "stat"){
                items.forEach(x => {
                    personData[parseInt(x)] = [1,2,3,4,5,6,7,8,9,10];
                })
                return {
                    code:0,
                    message:'success',
                    data:personData,
                };
            }
            return {
                code:0,
                message:'success',
                data:[
                    {}
                ],
            };
        });

        Mock.mock(new RegExp('/api/v1/privilege/grant'), (params) => {
            console.log("receive grant params,params:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{},
            };
        });
    },
});
