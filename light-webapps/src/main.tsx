import './style/global.less';
import React, {useEffect} from 'react';
import ReactDOM from 'react-dom';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import {ConfigProvider, Message,Notification} from '@arco-design/web-react';
import zhCN from '@arco-design/web-react/es/locale/zh-CN';
import enUS from '@arco-design/web-react/es/locale/en-US';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import axios from 'axios';
import {requestStarList as requestMetricStarList} from "@/api/metricset";
import {requestStarList as requestProjectStarList} from "@/api/project";
import rootReducer from './store';
import PageLayout from './layout';
import { GlobalContext } from './context';
import Login from './pages/login';
import {checkLogin} from './utils/checkLogin';
import changeTheme from './utils/changeTheme';
import useStorage from './utils/useStorage';
import {getDataWithLocalCache} from "@/utils/localCache";
import {requestFetchUserInfo} from "@/api/user";
import {fetchAllDepartmentData} from "@/pages/department/base";
import {MetricSet, Project, User} from "@/types/insights-web";
import Register from "@/pages/register";
import License from "@/pages/license";
import {AliveScope} from "react-activation";

const store = createStore(rootReducer);

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

  async function fetchUserData():Promise<User> {
    return new Promise<User>((resolve,reject) => {
      requestFetchUserInfo().then((response) => {
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

    const staredMetricInfo = await getDataWithLocalCache('cache_stared_metrics',300,fetchPinMetricsData);
    store.dispatch({
      type: 'update-staredMetricInfo',
      payload: {staredMetricInfo: staredMetricInfo,staredMetricsLoading:false},
    })

    const staredProjectInfo = await getDataWithLocalCache('cache_stared_projects',300,fetchPinProjectsData);
    store.dispatch({
      type: 'update-staredProjectInfo',
      payload: {staredProjectInfo: staredProjectInfo,staredProjectLoading:false},
    })

    const userInfo = await getDataWithLocalCache('cache_user_info',120,fetchUserData);
    store.dispatch({
      type: 'update-userInfo',
      payload: {userInfo:userInfo, userLoading: false},
    });
  }

  useEffect(() => {
    Message.config({ duration: 3000});
    Notification.config({duration:3000});
    if (checkLogin()) {
      fetchBasicInfo().then()
    } else if (window.location.pathname.replace(/\//g, '') !== 'login'
        && window.location.pathname.replace(/\//g, '') !== 'register'
        && window.location.pathname.replace(/\//g, '') !== 'license'
    ) {
      window.location.pathname = '/login';
    }
  }, []);

  useEffect(() => {
    changeTheme(theme);
  }, [theme]);

  useEffect(() => {
    document.getElementById("loading").style.display = "none";
    document.body.style.backgroundColor="";
  },[])

  useEffect(() => {
    const timer = setTimeout(() => {
      document.documentElement.classList.add('loaded');
    }, 200);
    return () => clearTimeout(timer);
  },[]);

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

ReactDOM.render(
    <AliveScope>
    <Index />
    </AliveScope>
    , document.getElementById('root'));
