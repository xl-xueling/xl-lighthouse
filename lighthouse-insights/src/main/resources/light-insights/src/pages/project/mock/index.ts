import Mock from 'mockjs';
import setupMock from '@/utils/setupMock';
import {Project} from "@/types/insights-web";

const { list } = Mock.mock({
  // 'list|100': [
  //   {
  //     id: /[0-9]{3}/,
  //     'name': '@word() @word() @word()',
  //     'departmentId|1-2': 0,
  //     'isPrivate|0-1': 0,
  //     'desc':'@sentence()',
  //     'admins':['1','2'],
  //     "createdTime":'@datetime',
  //   },

  'list': [
    {
      id: 101,
      'name': '@word() @word() @word()',
      'departmentId|1-2': 0,
      'isPrivate|0-1': 0,
      'desc':'@sentence()',
      'adminIds':['1','2'],
      "createdTime":'@datetime',
    },
    {
      id: 102,
      'name': '@word() @word() @word()',
      'departmentId|1-2': 0,
      'isPrivate|0-1': 0,
      'desc':'@sentence()',
      'adminIds':['1','2'],
      "createdTime":'@datetime',
    },
    {
      id: 103,
      'name': '@word() @word() @word()',
      'departmentId|1-2': 0,
      'isPrivate|0-1': 0,
      'desc':'@sentence()',
      'adminIds':['1','2'],
      "createdTime":'@datetime',
    },
    {
      id: 104,
      'name': '@word() @word() @word()',
      'departmentId|1-2': 0,
      'isPrivate|0-1': 0,
      'desc':'@sentence()',
      'adminIds':['1','2'],
      "createdTime":'@datetime',
    },
  ],
});


setupMock({
  setup: () => {
    Mock.mock(new RegExp('/api/v1/project/list'), (params) => {
      console.log("receive query params 2,params:" + JSON.stringify(params));
      return {
        code:0,
        message:'success',
        data:{
          list: list,
          total: 100,
        },
      };
    });

    Mock.mock('/api/v1/project/queryById', (params) => {
      console.log("receive queryById params,params:" + JSON.stringify(params));
      const data = JSON.parse(params.body);
      return {
        code:0,
        message:'success',
        data:
            Mock.mock({
            id: data.id,
          'name': '@word() @word() @word()',
          'departmentId|1-2': 1,
          'isPrivate': 0,
          'desc':'@sentence()',
          'adminIds':['1','2'],
          "createdTime":'@datetime',
        }),
      };
    });

    Mock.mock('/api/v1/project/queryByIds', (params) => {
      console.log("receive queryById params,params:" + JSON.stringify(params));
      const data = JSON.parse(params.body);
      const projectData: Record<number, Project> = {};
      projectData[101] = Mock.mock({
        id: 101,
        'name': '@word() @word() @word()',
        'departmentId|1-2': 1,
        'isPrivate': 0,
        'desc': '@sentence()',
        'adminIds': ['1', '2'],
        "createdTime": '@datetime',
      });
      projectData[102] = Mock.mock({
        id: 102,
        'name': '@word() @word() @word()',
        'departmentId|1-2': 1,
        'isPrivate': 0,
        'desc': '@sentence()',
        'adminIds': ['1', '2'],
        "createdTime": '@datetime',
      });

      return {
        code:0,
        message:'success',
        data:projectData

      };
    });


    Mock.mock(new RegExp('/api/v1/project/termList'), (params) => {
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


    Mock.mock(new RegExp('/api/v1/project/create'), (params) => {
      console.log("receive create params,params:" + JSON.stringify(params));
      return {
        code:'0',
        message:'success',
        data:{},
      };
    });

    Mock.mock(new RegExp('/api/v1/project/updateById'), (params) => {
      console.log("receive updateById params,params:" + JSON.stringify(params));
      return {
        code:'0',
        message:'success',
        data:{},
      };
    });

    Mock.mock(new RegExp('/api/v1/project/deleteById'), (params) => {
      console.log("receive deleteById params,params:" + JSON.stringify(params));
      return {
        code:'0',
        message:'success',
        data:{},
      };
    });

    Mock.mock(new RegExp('/api/v1/project/structure'), (params) => {
      console.log("receive structure params,params:" + JSON.stringify(params));
      return {
        code:'0',
        message:'success',
        data:{
          list:[
            {
              key:'1-101',
              id:101,
              title:'首页数据统计1',
              type:1,
              children:[
                    {
                      key:'2-1011',
                      id:1011,
                      title:'homepage_stat_view_1',
                      type:2,
                      children:[
                        {
                          key: '3-10111',
                          id: 10111,
                          title: 'homepage_stat_view_12',
                          type: 3,
                        }
                      ]
                    },
                  {
                    key:'2-1012',
                    id:1012,
                    title:'homepage_stat_view_2',
                    type:2,
                  },
                  {
                    key:'2-1013',
                    id:1013,
                    title:'homepage_stat_view_3',
                    type:2,
                  },
                  {
                    key:'2-1014',
                    id:1014,
                    title:'homepage_stat_view_4',
                    type:2,
                  },
                ]
            },
          ],
        },
      };
    });

  },
});
