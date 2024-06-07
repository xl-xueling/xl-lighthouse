import React, { useEffect, useMemo } from 'react';
import { useRouter } from 'next/router';
import cookies from 'next-cookies';
import Head from 'next/head';
import type { AppProps } from 'next/app';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import {ConfigProvider, Message, Notification} from '@arco-design/web-react';
import zhCN from '@arco-design/web-react/es/locale/zh-CN';
import enUS from '@arco-design/web-react/es/locale/en-US';
import axios from 'axios';
import '@/style/global.less';
import NProgress from 'nprogress';
import rootReducer from '../store';
import {requestStarList as requestMetricStarList} from "@/api/metricset";
import {requestStarList as requestProjectStarList} from "@/api/project";
import { GlobalContext } from '@/context';
import changeTheme from '@/utils/changeTheme';
import useStorage from '@/utils/useStorage';
import Layout from './layout';
import {getDataWithLocalCache} from "@/utils/localCache";
import {fetchAllDepartmentData} from "@/pages/department/base";
import {MetricSet, Project} from "@/types/insights-web";
import {requestFetchUserInfo} from "@/api/user";
import {checkLogin} from "@/utils/checkLogin";

const store = createStore(rootReducer);

interface RenderConfig {
  arcoLang?: string;
  arcoTheme?: string;
}

export default function MyApp({
  pageProps,
  Component,
  renderConfig,
}: AppProps & { renderConfig: RenderConfig }) {
  const { arcoLang, arcoTheme } = renderConfig;
  const [lang, setLang] = useStorage('arco-lang', arcoLang || 'en-US');
  const [theme, setTheme] = useStorage('arco-theme', arcoTheme || 'light');
  const router = useRouter();

  const locale = useMemo(() => {
    switch (lang) {
      case 'zh-CN':
        return zhCN;
      case 'en-US':
        return enUS;
      default:
        return enUS;
    }
  }, [lang]);

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
        && window.location.pathname.replace(/\//g, '') !== 'license'
    ) {
      window.location.pathname = '/login';
    }
  }, []);

  useEffect(() => {
    const handleStart = () => {
      NProgress.set(0.4);
      NProgress.start();
    };

    const handleStop = () => {
      NProgress.done();
    };

    router.events.on('routeChangeStart', handleStart);
    router.events.on('routeChangeComplete', handleStop);
    router.events.on('routeChangeError', handleStop);

    return () => {
      router.events.off('routeChangeStart', handleStart);
      router.events.off('routeChangeComplete', handleStop);
      router.events.off('routeChangeError', handleStop);
    };
  }, [router]);

  useEffect(() => {
    document.cookie = `arco-lang=${lang}; path=/`;
    document.cookie = `arco-theme=${theme}; path=/`;
    changeTheme(theme);
  }, [lang, theme]);

  const contextValue = {
    lang,
    setLang,
    theme,
    setTheme,
  };

  return (
    <>
      <Head>
        <title>XL-LightHouse</title>
        <link
          rel="shortcut icon"
          type="image/x-icon"
          href="/logo.png"
        />
      </Head>
      <ConfigProvider
        locale={locale}
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
            {Component.displayName === 'LoginPage' || Component.displayName === 'LicensePage'
            || Component.displayName ===  'RegisterPage'? (
              <Component {...pageProps} suppressHydrationWarning />
            ) : (
              <Layout>
                <Component {...pageProps} suppressHydrationWarning />
              </Layout>
            )}
          </GlobalContext.Provider>
        </Provider>
      </ConfigProvider>
    </>
  );
}

// fix: next build ssr can't attach the localstorage
MyApp.getInitialProps = async (appContext) => {
  const { ctx } = appContext;
  const serverCookies = cookies(ctx);
  return {
    renderConfig: {
      arcoLang: serverCookies['arco-lang'],
      arcoTheme: serverCookies['arco-theme'],
    },
  };
};
