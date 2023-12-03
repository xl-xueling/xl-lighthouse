import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';


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


    Mock.mock(new RegExp('/api/v1/stat/queryById'), (params) => {
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

  }
})