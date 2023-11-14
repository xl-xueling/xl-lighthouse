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
