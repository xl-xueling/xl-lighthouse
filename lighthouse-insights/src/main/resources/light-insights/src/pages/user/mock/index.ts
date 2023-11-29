import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {generatePermission, routes} from "@/routes";

setupMock({
    setup: () => {
        const { list } = Mock.mock(
            {
            'list|10': [
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
            }
        );

        Mock.mock(new RegExp('/api/v1/user/list'), (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            console.log("list size:" + list.length)
            return {
                code:'0',
                message:'success',
                data:{
                    'list':list,
                    'total':10,
                }
            };
        });

        Mock.mock(new RegExp('/api/v1/user/queryByIds'), (params) => {
            const list2 = Mock.mock(
                {
                    'list': [
                        {
                            "id": "1",
                            "userName":'AB',
                            "email":'@EMAIL()',
                            "phone":'@Phone()',
                            "departmentId":2,
                            "state|0-3":0,
                            "createdTime":'@datetime',
                        },

                        {
                            "id": "2",
                            "userName":'CD',
                            "email":'@EMAIL()',
                            "phone":'@Phone()',
                            "departmentId":2,
                            "state|0-3":0,
                            "createdTime":'@datetime',
                        },
                    ],
                }
            );
            console.log("receive queryByIds param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:list2,
            };
        });

        Mock.mock(new RegExp('/api/v1/user/updateById'), (params) => {
            console.log("changeState,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });

        Mock.mock(new RegExp('/api/v1/user/resetPasswd'), (params) => {
            console.log("resetPasswd,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });


        Mock.mock(new RegExp('/api/v1/user/deleteById'), (params) => {
            console.log("deleteById,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });


        Mock.mock(new RegExp('/api/v1/user/changeState'), (params) => {
            console.log("changeState,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });


        Mock.mock(new RegExp('/api/v1/user/fetchUserInfo'), () => {
            const userRole = window.localStorage.getItem('userRole') || 'admin';
            const data = Mock.mock({
                "id": /[0-9]{8}/,
                "userName":'@name()',
                "email":'@EMAIL()',
                "phone":/[1][1-3][0-9]{10}/,
                "departmentId":2,
                "state":0,
                "createdTime":'@datetime',
                "avatar":
                    'https://lf1-xgcdn-tos.pstatp.com/obj/vcloud/vadmin/start.8e0e4855ee346a46ccff8ff3e24db27b.png',
                "permissions": generatePermission(userRole),
            });
            return {
                code:'0',
                message:'success',
                data:data
            };
        });


        Mock.mock(new RegExp('/api/v1/user/register'), (params) => {
            console.log("receive params:" + JSON.stringify(params));
            const { userName, password } = JSON.parse(params.body);
            if (userName === 'admin' && password === 'admin') {
                return {
                    code:'0',
                    msg:'注册成功！',
                    data:{}
                };
            }
            return {
                code:'0',
                msg:'注册成功！',
                data:{}
            };
        });



        const generatePermission = (role: string) => {
            const actions = role === 'admin' ? ['*'] : ['read'];
            const result = {};
            routes.forEach((item) => {
                if (item.children) {
                    item.children.forEach((child) => {
                        result[child.name] = actions;
                    });
                }
            });
            return result;
        };

    }
})