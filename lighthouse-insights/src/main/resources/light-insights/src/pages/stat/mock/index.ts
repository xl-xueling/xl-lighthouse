import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {Project, Stat} from "@/types/insights-web";
import {_Mock_user1, _Mock_user2, _Mock_user3} from "@/pages/user/mock";
import {_Mock_project1} from "@/pages/project/mock";

const renderConfig = {
  datepicker:{
    renderType:1,
    label:'日期',
  },
  filters:[
    {
      renderType:5,
      label:'省份',
      dimens:'province',
      componentId:1,
      configData:[
        {
          label:'山东',
          value:'101',
        },
        {
          label:'北京',
          value:'102',
        }
      ]
    },
    {
      renderType:5,
      label:'省份',
      dimens:'city',
      configData:[
        {
          label:'山东',
          value:'101',
        },
        {
          label:'北京',
          value:'102',
        }
      ]
    },
    {
      renderType:5,
      label:'召回',
      dimens:'recallno',
      configData:[
        {
          label:'山东',
          value:'101',
        },
        {
          label:'北京',
          value:'102',
        }
      ]
    },
  ]
}

const _Mock_stat1:Stat = Mock.mock({
      id:111,
      title:"每分钟uv统计2",
      groupId:11,
      projectId:1,
      timeparam:'1-minute',
      template:"<stat-item title=\"每分钟_uv数据统计\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
      expired:102323,
      createdTime: '@datetime',
      project:_Mock_project1,
      adminIds:[1,2],
      admins:[
        _Mock_user1,_Mock_user2,_Mock_user3
      ],
      dimensArray:["province","city","behaviorType"],
      renderConfig:renderConfig,
});

const _Mock_stat2:Stat = Mock.mock({
  id:112,
  title:"每分钟uv统计2",
  groupId:11,
  projectId:1,
  timeparam:'1-minute',
  template:"<stat-item title=\"每分钟_uv数据统计\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
  expired:102323,
  createdTime: '@datetime',
  project:_Mock_project1,
  adminIds:[1,2],
  admins:[
    _Mock_user1,_Mock_user2,_Mock_user3
  ],
  renderConfig:renderConfig,
});



setupMock({
  setup: () => {
    Mock.mock(new RegExp('/api/v1/stat/queryByGroupId'), (params) => {
      console.log("receive param is:" + JSON.stringify(params));
      const list = Mock.mock([
        {
          id:10231,
          title:"每分钟uv统计1",
          groupId:1001,
          projectId:101,
          timeparam:'1-minute',
          template:"<stat-item title=\"每分钟_uv数据统计\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
          expired:102323,
          createdTime: '@datetime',
        },
        {
          id:10232,
          title:"每分钟uv统计2",
          groupId:1001,
          projectId:101,
          timeparam:'1-minute',
          template:"<stat-item title=\"每分钟_uv数据统计\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
          expired:102323,
          createdTime: '@datetime',
        },
        {
          id:10233,
          title:"每分钟uv统计3",
          groupId:1001,
          projectId:101,
          timeparam:'1-minute',
          template:"<stat-item title=\"每分钟_uv数据统计\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
          expired:102323,
          createdTime: '@datetime',
        },
      ]);

      return {
        code:'0',
        message:'success',
        data:{
          list:list
        }
      };
    });


    Mock.mock('/api/v1/stat/queryById', (params) => {
      console.log("receive param is:" + JSON.stringify(params));
      const stat = Mock.mock(
        {
          id:10231,
          title:"每分钟uv统计1",
          groupId:1001,
          projectId:101,
          timeparam:'1-minute',
          template:"<stat-item title=\"每分钟_uv数据统计\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
          expired:102323,
          createdTime: '@datetime',
        });

      return {
        code:'0',
        message:'success',
        data:stat,
      };
    });

    Mock.mock(new RegExp('/api/v1/stat/list'), (params) => {
      console.log("receive param is:" + JSON.stringify(params));
      const { list } = Mock.mock({
        'list|10': [
          {
            id: /[1-9]{7}/,
            title:"每分钟uv统计3",
            projectId:101,
            groupId:1001,
            adminIds:[1,2],
            "state|0-3":0,
            timeparam:'1-minute',
            template:"<stat-item title=\"每分钟_uv数据统计\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
            expired:102323,
          },
        ],
      });

      return {
        code:'0',
        message:'success',
        data:{
          list:list,
          total:100,
        }
      };
    });

    Mock.mock('/api/v1/stat/queryByIds', (params) => {
      console.log("receive queryByIds params,params:" + JSON.stringify(params));
      const data = JSON.parse(params.body);
      const statData: Record<number, Stat> = {};
      for(let i=0;i<data.ids.length;i++){
        const id = data.ids[i];
        statData[id] = _Mock_stat1;
      }
      return {
        code:0,
        message:'success',
        data:statData
      };
    });

  }
})