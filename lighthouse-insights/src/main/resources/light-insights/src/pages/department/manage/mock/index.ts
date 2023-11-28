import Mock from 'mockjs';
import setupMock from '@/utils/setupMock';
import qs from "query-string";

setupMock({
  setup: () => {
    Mock.mock('/api/v1/department/queryById', (params) => {
      console.log("receive params:" + JSON.stringify(params))
    })
  },
});
