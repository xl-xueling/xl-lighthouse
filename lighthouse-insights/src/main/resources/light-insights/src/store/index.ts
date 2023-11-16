import defaultSettings from '../settings.json';
import {User} from "@/types/insights-web";

export interface GlobalState {
  settings?: typeof defaultSettings;
  userInfo? : User;
  userLoading?: boolean;
}

const initialState = ():GlobalState => {
  const initUser: User= {
    permissions: {}
  };
  return {
    settings: defaultSettings,
    userInfo: initUser,
  }
};


export default function processReducer(state = initialState(), action) {
  switch (action.type) {
    case 'update-settings': {
      const { settings } = action.payload;
      return {
        ...state,
        settings,
      };
    }
    case 'update-userInfo':{
      const { userInfo = initialState().userInfo, userLoading } = action.payload;
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
