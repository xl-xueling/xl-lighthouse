import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import setupMock from '@/utils/setupMock';
import {Project, Stat} from "@/types/insights-web";
import {_Mock_user1, _Mock_user2, _Mock_user3} from "@/pages/user/mock";
import {_Mock_project1} from "@/pages/project/mock";
import {_Mock_group1} from "@/pages/group/mock";

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
      title:"每分钟uv统计1",
      groupId:11,
      projectId:1,
      timeparam:'1-minute',
      template:"<stat-item title=\"每分钟_uv数据统计1\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
      expired:102323,
      createdTime: '@datetime',
      group:_Mock_group1,
      project:_Mock_project1,
      dimensArray:["province","city","behaviorType"],
      renderConfig:renderConfig,
});

const _Mock_stat2:Stat = Mock.mock({
      id:112,
      title:"每分钟uv统计2",
      groupId:11,
      projectId:1,
      timeparam:'1-minute',
      template:"<stat-item title=\"每分钟_uv数据统计2\" stat=\"bitcount(userId)\" dimens=\"province\" limit=\"top100\" />",
      expired:102323,
      createdTime: '@datetime',
      project:_Mock_project1,
      group:_Mock_group1,
      renderConfig:renderConfig,
});
