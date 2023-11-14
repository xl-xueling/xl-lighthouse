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
              "fullpath":"1",
            },
            {
              "id":"2",
              "name":"产品部",
              "pid":"0",
              "fullpath":"2",
            },
            {
              "id":"3",
              "name":"市场部",
              "pid":"0",
              "fullpath":"3",
            },
            {
              "id":"4",
              "name":"运维部",
              "pid":"0",
              "fullpath":"4",
            },
            {
              "id":"11",
              "name":"研发一部",
              "pid":"1",
               "fullpath":"1,11",
            },
            {
                "id":"111",
                "name":"研发一部一组",
                "pid":"11",
                "fullpath":"1,11,111",
            },
            {
              "id":"12",
              "name":"研发二部",
              "pid":"1",
              "fullpath":"1,12",
            },
            {
                "id":"121",
                "name":"研发二部一组",
                "pid":"12",
                "fullpath":"1,12,121",
            },
            {
                "id":"122",
                "name":"研发二部二组",
                "pid":"12",
                "fullpath":"1,12,122",
            },
            {
              "id":"21",
              "name":"产品一部",
              "pid":"2",
              "fullpath":"2,21",
            },
          {
            "id":"22",
            "name":"产品二部",
            "pid":"2",
            "fullpath":"2,22",
          },
          {
            "id":"31",
            "name":"市场一部",
            "pid":"3",
            "fullpath":"3,31",
          },
          {
            "id":"32",
            "name":"市场二部",
            "pid":"3",
            "fullpath":"3,32",
          },
        ],
      };
    });


      Mock.mock(new RegExp('/api/v1/department/add'), (params) => {
          console.log("add department,params:" + JSON.stringify(params));
          const {pid,title} = params;
          return {
              code: '0',
              message: 'success',
              data:{
                  "id": Math.floor(Math.random() * 1000000) + "",
                  "title":title,
              }
          }
      })

      Mock.mock(new RegExp('/api/v1/department/updateById'), (params) => {
          console.log("updateById department,params:" + JSON.stringify(params));
          const {pid,title} = params;
          return {
              code: '0',
              message: 'success',
              data:{}
          }
      })

      Mock.mock(new RegExp('/api/v1/department/dragTo'), (params) => {
          console.log("drag department,receive params:" + JSON.stringify(params));
          const {pid,title} = params;
          return {
              code: '0',
              message: 'success',
              data:{}
          }
      })

      Mock.mock(new RegExp('/api/v1/department/deleteById'), (params) => {
          console.log("deleteById department,receive params:" + JSON.stringify(params));
          const {pid,title} = params;
          return {
              code: '0',
              message: 'success',
              data:{}
          }
      })
  },
});
