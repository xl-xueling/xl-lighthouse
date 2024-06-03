import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {generatePermission, routes} from "@/routes";
import {Project, User} from "@/types/insights-web";

export const _Mock_user1  = {
    "id": 1,
    "userName":'@name()',
    "email":'@EMAIL()',
    "phone":/[0-9]{11}/,
    "departmentId":2,
    "state|0-3":0,
    "createdTime":'@datetime',
};

export const  _Mock_user2  = {
    "id": 2,
    "userName":'@name()',
    "email":'@EMAIL()',
    "phone":/[0-9]{11}/,
    "departmentId":2,
    "state|0-3":0,
    "createdTime":'@datetime',
};

export const  _Mock_user3  = {
    "id": 2,
    "userName":'@name()',
    "email":'@EMAIL()',
    "phone":/[0-9]{11}/,
    "departmentId":2,
    "state|0-3":0,
    "createdTime":'@datetime',
};

setupMock({
    setup: () => {


        const { list } = Mock.mock(
            {
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
            }
        );

        Mock.mock('/api/v1/user/list', (params) => {
            console.log("receive param is:" + JSON.stringify(params));
            console.log("list size:" + list.length)
            return {
                code:'0',
                message:'success',
                data:{
                    'list':list,
                    'total':80,
                }
            };
        });

        Mock.mock('/api/v1/user/termList', (params) => {
            console.log("termList receive param is:" + JSON.stringify(params));
            console.log("list size:" + list.length)
            const id = Math.round(Math.random()*100);
            const list2 = Mock.mock(
                {
                    'list': [
                        {
                            "id": "111_" + id,
                            "userName":"111_" + id,
                            "email":'@EMAIL()',
                            "phone":'@Phone()',
                            "departmentId":2,
                            "state|0-3":0,
                            "createdTime":'@datetime',
                        },

                        {
                            "id": "112_" + id,
                            "userName":"112_" + id,
                            "email":'@EMAIL()',
                            "phone":'@Phone()',
                            "departmentId":2,
                            "state|0-3":0,
                            "createdTime":'@datetime',
                        },
                        {
                            "id": "113_" + id,
                            "userName":"113_" + id,
                            "email":'@EMAIL()',
                            "phone":'@Phone()',
                            "departmentId":2,
                            "state|0-3":0,
                            "createdTime":'@datetime',
                        },
                        {
                            "id": "114_" + id,
                            "userName":"114_" + id,
                            "email":'@EMAIL()',
                            "phone":'@Phone()',
                            "departmentId":2,
                            "state|0-3":0,
                            "createdTime":'@datetime',
                        },
                    ],
                }
            );
            return {
                code:'0',
                message:'success',
                data:list2,
            };
        });


        Mock.mock('/api/v1/user/queryByIds', (params) => {
            console.log("receive queryByIds params,params:" + JSON.stringify(params));
            const data = JSON.parse(params.body);
            const userData: Record<number, User> = {};
            userData[1] = Mock.mock({
                id: 1,
                "userName":'CD',
                "email":'@EMAIL()',
                "phone":'@Phone()',
                "departmentId":2,
                "state|0-3":0,
                "createdTime":'@datetime',
            });
            userData[2] = Mock.mock({
                id: 2,
                "userName":'CD',
                "email":'@EMAIL()',
                "phone":'@Phone()',
                "departmentId":2,
                "state|0-3":0,
                "createdTime":'@datetime',
            });

            return {
                code:0,
                message:'success',
                data:userData
            };
        });



        Mock.mock('/api/v1/user/updateById', (params) => {
            console.log("changeState,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });

        Mock.mock('/api/v1/user/resetPasswd', (params) => {
            console.log("resetPasswd,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });


        Mock.mock('/api/v1/user/deleteById', (params) => {
            console.log("deleteById,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });


        Mock.mock('/api/v1/user/changeState', (params) => {
            console.log("changeState,receive param is:" + JSON.stringify(params));
            return {
                code:'0',
                message:'success',
                data:{}
            };
        });



        Mock.mock('/api/v1/user/register', (params) => {
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


        Mock.mock('/api/v1/user/login', (params) => {
            const { userName, password } = JSON.parse(params.body);
            if (!userName) {
                return {
                    code:'1',
                    message:'用户名不能为空',
                    data:{
                    }
                };
            }
            if (!password) {
                return {
                    code:'2',
                    message:'密码不能为空',
                    data:{}
                };
            }
            if (userName === 'admin' && password === 'admin') {
                return {
                    code:'0',
                    message:'登录成功！',
                    data:{
                        token:"sasucessawwxoks"
                    }
                };
            }
            return {
                code:'3',
                message:'账号密码错误！',
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