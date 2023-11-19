import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';

const { list } = Mock.mock({
  'list|100': [
    {
      id: /[0-9]{8}/,
      'name': '@word() @word() @word()',
      'departmentId|1-2': 0,
      'isPrivate|0-1': 0,
      'desc':'@sentence()',
      'admins':['1','2'],
      "createdTime":'@datetime',
    },
  ],
});


setupMock({
  setup: () => {
    Mock.mock(new RegExp('/api/v1/project/list'), (params) => {
      console.log("receive query params,params:" + JSON.stringify(params));
      return {
        code:0,
        message:'success',
        data:{
          list: list,
          total: 100,
        },
      };
    });

    Mock.mock(new RegExp('/api/v1/project/create'), (params) => {
      console.log("receive create params,params:" + JSON.stringify(params));
      return {
        code:0,
        message:'success',
        data:{},
      };
    });

    Mock.mock(new RegExp('/api/v1/project/update'), (params) => {
      console.log("receive update params,params:" + JSON.stringify(params));
      return {
        code:0,
        message:'success',
        data:{},
      };
    });

    Mock.mock(new RegExp('/api/v1/project/delete'), (params) => {
      console.log("receive delete params,params:" + JSON.stringify(params));
      return {
        code:0,
        message:'success',
        data:{},
      };
    });

  },
});
