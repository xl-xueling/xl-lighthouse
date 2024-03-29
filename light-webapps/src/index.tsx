
import './style/global.less';
import React, {useEffect} from 'react';
import ReactDOM from 'react-dom';
import {createStore} from 'redux';
import {Provider} from 'react-redux';
import {ConfigProvider, Message, Notification} from '@arco-design/web-react';
import zhCN from '@arco-design/web-react/es/locale/zh-CN';
import enUS from '@arco-design/web-react/es/locale/en-US';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import rootReducer from './store';
import PageLayout from './layout';
import {GlobalContext} from './context';
import Login from './pages/login';
import changeTheme from './utils/changeTheme';
import useStorage from './utils/useStorage';
import './mock';
import Register from "@/pages/register";
import {requestFetchUserInfo} from "@/api/user";
import {getDataWithLocalCache} from "@/utils/localCache";
import {MetricSet, Project} from "@/types/insights-web";
import {requestStarList as requestMetricStarList} from "@/api/metricset";
import {requestStarList as requestProjectStarList} from "@/api/project";
import {fetchAllDepartmentData} from "@/pages/department/common";
import {checkLogin} from "@/utils/checkLogin";
import License from "@/pages/license";

const store = createStore(rootReducer);

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

function Index() {
  const [lang, setLang] = useStorage('arco-lang', 'en-US');
  const [theme, setTheme] = useStorage('arco-theme', 'light');

  function getArcoLocale() {
    switch (lang) {
      case 'zh-CN':
        return zhCN;
      case 'en-US':
        return enUS;
      default:
        return zhCN;
    }
  }

  const addTodo = (todo) => ({
    type: 'ADD_TODO',
    payload: todo,
  });

  async function fetchPinMetricsData():Promise<Array<MetricSet>> {
    return new Promise<Array<MetricSet>>((resolve,reject) => {
      requestMetricStarList().then((response) => {
        resolve(response.data);
      }).catch((error) => {
        reject(error);
      })
    })
  }

  async function fetchPinProjectsData():Promise<Array<Project>> {
    return new Promise<Array<Project>>((resolve,reject) => {
      requestProjectStarList().then((response) => {
        resolve(response.data);
      }).catch((error) => {
        reject(error);
      })
    })
  }

  async function fetchBasicInfo() {
    const allDepartInfo = await getDataWithLocalCache('cache_all_department',300,fetchAllDepartmentData);
    store.dispatch({
      type: 'update-allDepartInfo',
      payload: {allDepartInfo: allDepartInfo,departLoading:false},
    })

    const staredMetricInfo = await getDataWithLocalCache('cache_stared_metrics',600,fetchPinMetricsData);
    store.dispatch({
      type: 'update-staredMetricInfo',
      payload: {staredMetricInfo: staredMetricInfo,staredMetricsLoading:false},
    })

    const staredProjectInfo = await getDataWithLocalCache('cache_stared_projects',600,fetchPinProjectsData);
    store.dispatch({
      type: 'update-staredProjectInfo',
      payload: {staredProjectInfo: staredProjectInfo,staredProjectLoading:false},
    })

    requestFetchUserInfo().then((resultData) => {
      const userInfo = resultData.data;
      store.dispatch({
        type: 'update-userInfo',
        payload: {userInfo: resultData.data, userLoading: false},
      });
    })
  }

  useEffect(() => {
    Message.config({ duration: 3000});
    Notification.config({duration:3000});
    if (checkLogin()) {
      fetchBasicInfo().then()
    } else if (window.location.pathname.replace(/\//g, '') !== 'login'
        && window.location.pathname.replace(/\//g, '') !== 'register'
    ) {
      window.location.pathname = '/login';
    }
  }, []);

  useEffect(() => {
    changeTheme(theme);
  }, [theme]);

  const contextValue = {
    lang,
    setLang,
    theme,
    setTheme,
  };

  return (
    <BrowserRouter>
      <ConfigProvider
        locale={getArcoLocale()}
        componentConfig={{
          Card: {
            bordered: false,
          },
          List: {
            bordered: false,
          },
          Table: {
            border: false,
          },
        }}
      >
        <Provider store={store}>
          <GlobalContext.Provider value={contextValue}>
            <Switch>
              <Route path="/login" component={Login} />
              <Route path="/register" component={Register} />
              <Route path="/license" component={License}/>
              <Route path="/" component={PageLayout} />
            </Switch>
          </GlobalContext.Provider>
        </Provider>
      </ConfigProvider>
    </BrowserRouter>
  );
}

ReactDOM.render(<Index />, document.getElementById('root'));
