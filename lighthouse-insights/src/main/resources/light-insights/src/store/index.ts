import defaultSettings from '../settings.json';
import {Department, MetricSet, Project, User} from "@/types/insights-web";

export interface GlobalState {
  settings?: typeof defaultSettings;
  userInfo? : User;
  userLoading?: boolean;
  allDepartInfo? : Array<Department>;
  staredMetricInfo? :Array<MetricSet>;
  staredProjectInfo?:Array<Project>;
}

const initialState = ():GlobalState => {
  const initUser: User= {
    permissions: {}
  };
  return {
    settings: defaultSettings,
    userInfo: initUser,
    allDepartInfo:[],
    staredMetricInfo:[],
    staredProjectInfo:[],
  }
};


export default function processReducer(state = initialState(), action) {
  switch (action.type) {
    case 'update-allDepartInfo':{
      const { allDepartInfo = initialState().allDepartInfo,departLoading = true } = action.payload;
      return {
        ...state,
        departLoading,
        allDepartInfo,
      }
    }
    case 'update-staredMetricInfo':{
      const { staredMetricInfo = initialState().staredMetricInfo,staredMetricsLoading = true } = action.payload;
      return {
        ...state,
        staredMetricsLoading,
        staredMetricInfo,
      }
    }

    case 'update-staredProjectInfo':{
      const { staredProjectInfo = initialState().staredProjectInfo,staredProjectLoading = true } = action.payload;
      return {
        ...state,
        staredProjectLoading,
        staredProjectInfo,
      }
    }

    case 'update-settings': {
      const { settings } = action.payload;
      return {
        ...state,
        settings,
      };
    }
    case 'update-userInfo':{
      const { userInfo = initialState().userInfo, userLoading = true } = action.payload;
      return {
        ...state,
        userLoading,
        userInfo,
      };
    }
    default:
      return state;
  }
}
