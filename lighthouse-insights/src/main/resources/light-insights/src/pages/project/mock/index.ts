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
    Mock.mock(new RegExp('/api/v1/project/list'), (params) => {
      console.log("receive params,params:" + JSON.stringify(params));
      return {
        code:0,
        message:'success',
        data:{
          list: list,
          total: 100,
        },
      };
    });
  },
});
