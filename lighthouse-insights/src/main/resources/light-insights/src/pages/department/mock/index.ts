import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';


setupMock({
  setup: () => {
    Mock.mock(new RegExp('/api/v1/department/all'), (params) => {
      return {
        code:'0',
        message:'success',
        token:"success-"+new Date().getTime(),
        data:
        [
            {
              "id":"1",
              "name":"研发部",
              "pid":"0",
            },
            {
              "id":"2",
              "name":"产品部",
              "pid":"0",
            },
            {
              "id":"3",
              "name":"市场部",
              "pid":"0",
            },
            {
              "id":"4",
              "name":"运维部",
              "pid":"0",
            },
            {
              "id":"11",
              "name":"研发一部",
              "pid":"1",
            },
            {
              "id":"12",
              "name":"研发二部",
              "pid":"1",
            },
            {
              "id":"21",
              "name":"产品一部",
              "pid":"2",
            },
          {
            "id":"22",
            "name":"产品二部",
            "pid":"2",
          },
          {
            "id":"31",
            "name":"市场一部",
            "pid":"3",
          },
          {
            "id":"32",
            "name":"市场二部",
            "pid":"3",
          },
        ]
        ,
      };
    });
  },
});
