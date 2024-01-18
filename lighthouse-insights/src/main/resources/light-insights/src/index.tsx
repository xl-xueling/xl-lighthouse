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
import {Department, MetricSet} from "@/types/insights-web";
import {requestQueryAll as queryDepartmentAll} from "@/api/department";
import {requestPinList} from "@/api/metricset";
import {fetchAllDepartmentData, getDepartment} from "@/pages/department/common";
import {checkLogin} from "@/utils/checkLogin";

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

  const addTodo = (todo) => ({
    type: 'ADD_TODO',
    payload: todo,
  });

  async function fetchPinMetricsData():Promise<Array<MetricSet>> {
    return new Promise<Array<MetricSet>>((resolve,reject) => {
      requestPinList().then((response) => {
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

    // const pinMetricsInfo = await getDataWithLocalCache('cache_pin_metrics',300,fetchPinMetricsData);
    // store.dispatch({
    //   type: 'update-pinMetricsInfo',
    //   payload: {pinMetricsInfo: pinMetricsInfo,pinMetricsLoading:false},
    // })

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
              <Route path="/" component={PageLayout} />
            </Switch>
          </GlobalContext.Provider>
        </Provider>
      </ConfigProvider>
    </BrowserRouter>
  );
}

ReactDOM.render(<Index />, document.getElementById('root'));
