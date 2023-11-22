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
          title:"每分钟uv统计",
          groupId:1001,
          projectId:101,
          timeparam:'1-minute',
          template:"\<stat-item title=\"bitcount(userId)\"\/\>",
          expire:102323,
          createdTime:'@datetime',
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


  }
})