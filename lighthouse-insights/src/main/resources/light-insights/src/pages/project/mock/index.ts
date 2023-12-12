import Mock from 'mockjs';
import setupMock from '@/utils/setupMock';
import {Project} from "@/types/insights-web";
import {_Mock_user1, _Mock_user2,_Mock_user3} from "@/pages/user/mock";


export const _Mock_project1  = {
  id: 101,
  'name': '@word() @word() @word()',
  'departmentId|1-2': 0,
  'isPrivate|0-1': 0,
  'desc':'@sentence()',
  'adminIds':[1,2],
  "createdTime":'@datetime',
  "admins":[
    _Mock_user1,_Mock_user2,_Mock_user3
  ],
  structure:[
    {
      "key":"11",
      "title":"homepage_behavior_stat1",
      "children":[
        {
          "key":"111",
          "title":"每分钟_各省份_uv统计",
        },
        {
          "key":"112",
          "title":"每分钟_各省份_uv统计",
        },
        {
          "key":"113",
          "title":"每分钟_各省份_uv统计",
        },
      ]
    },
    {
      "key":"12",
      "name":"homepage_behavior_stat2",
      "children":[
        {
          "key":"121",
          "title":"每分钟_各省份_uv统计",
        },
        {
          "key":"122",
          "title":"每分钟_各省份_uv统计",
        },
        {
          "key":"123",
          "title":"每分钟_各省份_uv统计",
        },
      ]
    }
  ]
};


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
    _Mock_project1,
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
        "admins":[
          _Mock_user1,_Mock_user2,_Mock_user3
        ]
      });
      projectData[102] = Mock.mock({
        id: 102,
        'name': '@word() @word() @word()',
        'departmentId|1-2': 1,
        'isPrivate': 0,
        'desc': '@sentence()',
        'adminIds': ['1', '2'],
        "createdTime": '@datetime',
        "admins":[
          _Mock_user1,_Mock_user2,_Mock_user3
        ]
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
