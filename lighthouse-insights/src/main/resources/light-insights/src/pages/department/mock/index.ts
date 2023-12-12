import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {_Mock_user1, _Mock_user2, _Mock_user3} from "@/pages/user/mock";

export const _Mock_department1  = {
    id: 1,
    'name': '@word() @word() @word()',
    'departmentId|1-2': 0,
    'isPrivate|0-1': 0,
    'desc':'@sentence()',
    'adminIds':[1,2],
    "createdTime":'@datetime',
    "admins":[
        _Mock_user1,_Mock_user2,_Mock_user3
    ],
    "department":null,
};


setupMock({
  setup: () => {
      Mock.mock(new RegExp('/api/v1/department/all'), () => {
          return {
              code:'0',
              message:'success',
              data:
                  [
                      {
                          "id":"1",
                          "name":"研发部",
                          "children":[
                              {
                                  "id":"11",
                                  "name":"研发部一组",
                              },
                              {
                                  "id":"12",
                                  "name":"研发部二组",
                              },
                              {
                                  "id":"13",
                                  "name":"研发部三组",
                              },
                          ]
                      },
                      {
                          "id":"2",
                          "name":"产品部",
                          "children":[
                              {
                                  "id":"21",
                                  "name":"产品部一组",
                              },
                              {
                                  "id":"22",
                                  "name":"产品部二组",
                              },
                              {
                                  "id":"23",
                                  "name":"产品部三组",
                              },
                          ]
                      },
                      {
                          "id":"3",
                          "name":"市场部",
                          "children":[
                              {
                                  "id":"31",
                                  "name":"市场部一组",
                              },
                              {
                                  "id":"32",
                                  "name":"市场部二组",
                              },
                              {
                                  "id":"33",
                                  "name":"市场部三组",
                              },
                          ]
                      },
                  ],
          };
      });


      Mock.mock(new RegExp('/api/v1/department/create'), (params) => {
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
