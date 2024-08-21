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
    userLoading:true,
    allDepartInfo:[],
    staredMetricInfo:[],
    staredProjectInfo:[],
  }
};

export const updateStoreUserInfo = (userInfo) => ({
  type: 'update-userInfo',
  payload: {userInfo: userInfo,userLoading:false},
});

export const updateStoreAllDepartInfo = (allDepartInfo) => ({
  type: 'update-allDepartInfo',
  payload: {allDepartInfo: allDepartInfo,departLoading:false},
});

export const updateStoreStaredMetricInfo = (staredMetricInfo) => ({
  type: 'update-staredMetricInfo',
  payload: {staredMetricInfo: staredMetricInfo,staredMetricsLoading:false},
});

export const updateStoreStaredProjectInfo = (staredProjectInfo) => ({
  type: 'update-staredProjectInfo',
  payload: {staredProjectInfo: staredProjectInfo,staredProjectLoading:false},
});



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
