import React, { useState, useMemo, useRef, useEffect } from 'react';
import { Switch, Route, Redirect, useHistory } from 'react-router-dom';
import { Layout, Menu, Breadcrumb, Spin } from '@arco-design/web-react';
import cs from 'classnames';
import {
  IconDashboard,
  IconTag,
  IconMenuFold,
  IconMenuUnfold,
  IconList,
  IconApps,
  IconCalendarClock,
  IconSettings,
  IconUser,
  IconExclamationCircle,
  IconCheckCircle,
  IconFile, IconCodepen,
} from '@arco-design/web-react/icon';
import { useSelector } from 'react-redux';
import qs from 'query-string';
import NProgress from 'nprogress';
import Navbar from './components/NavBar';
import Footer from './components/Footer';
import useRoute, { IRoute } from '@/routes';
import { isArray } from './utils/is';
import useLocale from './utils/useLocale';
import getUrlParams from './utils/getUrlParams';
import lazyload from './utils/lazyload';
import { GlobalState } from './store';
import styles from './style/layout.module.less';
import {PiDiamondsFour} from "react-icons/pi";
import ProjectManagePage from "@/pages/project/manage";
import ProjectPreviewPage from "@/pages/project/preview";
import StatPreviewPage from "@/pages/stat/preview";
import TrackStatPage from "@/pages/track";
import Index from "@/pages/metricset/preview";
import PopMenuBox from "@/pages/common/popmenu/PopMenu";
import {arrayDistinct} from "@/utils/util";
import {AiOutlineDashboard} from "react-icons/ai";
import {TbBrandVisualStudio, TbCalendarTime} from "react-icons/tb";
import {FiSettings} from "react-icons/fi";

const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;

const Sider = Layout.Sider;
const Content = Layout.Content;

function getIconFromKey(key) {
  switch (key) {
    case 'dashboard':
      return <AiOutlineDashboard size={17} style={{marginRight: '15px',strokeWidth: '2px',opacity:0.9}} className={styles.icon}/>;
    case 'view':
      return <TbBrandVisualStudio size={17} style={{marginRight: '15px',strokeWidth: '2px',opacity:0.9}} className={styles.icon}/>;
    case 'list':
      return <IconList className={styles.icon}/>;
    case 'form':
      return <IconSettings className={styles.icon}/>;
    case 'profile':
      return <IconFile className={styles.icon}/>;
    case 'result':
      return <IconCheckCircle className={styles.icon}/>;
    case 'exception':
      return <IconExclamationCircle className={styles.icon}/>;
    case 'user':
      return <IconUser className={styles.icon}/>;
    case 'system':
      return <FiSettings size={16} style={{marginRight: '15px',strokeWidth: '2px',opacity:0.9}} className={styles.icon}/>;
    case 'order':
      return <TbCalendarTime size={17} style={{marginRight: '15px',strokeWidth: '2px',opacity:0.9}} className={styles.icon}/>;
    case 'stat':
      return <IconApps className={styles.icon}/>;
    case 'filter':
      return <IconList className={styles.icon}/>;
    case 'statistics':
      return <PiDiamondsFour size={17} style={{marginRight: '15px',strokeWidth: '2px',opacity:0.9}} className={styles.icon}/>;
    case 'favorites':
      return <IconCodepen className={styles.icon}/>
    default:
      return <div className={styles['icon-empty']}/>;
  }
}

function getFlattenRoutes(routes) {
  const mod = import.meta.glob('./pages/**/[a-z[]*.tsx');
  const res = [];
  function travel(_routes) {
    _routes.forEach((route) => {
      if (route.key && !route.children) {
        route.component = lazyload(mod[`./pages/${route.key}/index.tsx`]);
        res.push(route);
      } else if (isArray(route.children) && route.children.length) {
        travel(route.children);
      }
    });
  }
  travel(routes);
  return res;
}

function PageLayout() {
  const urlParams = getUrlParams();
  const history = useHistory();
  const pathname = history.location.pathname;
  const currentComponent = qs.parseUrl(pathname).url.slice(1);
  const locale = useLocale();
  const { settings, userLoading, userInfo } = useSelector(
    (state: GlobalState) => state
  );

  const [routes, defaultRoute] = useRoute(userInfo?.permissions);
  const defaultSelectedKeys = [currentComponent || defaultRoute];
  const paths = (currentComponent || defaultRoute).split('/');
  const defaultOpenKeys = ["dashboard","statistics"];
  const [breadcrumb, setBreadCrumb] = useState([]);
  const [collapsed, setCollapsed] = useState<boolean>(false);
  const [selectedKeys, setSelectedKeys] =
    useState<string[]>(defaultSelectedKeys);
  const [openKeys, setOpenKeys] = useState<string[]>(defaultOpenKeys);

  const routeMap = useRef<Map<string, React.ReactNode[]>>(new Map());
  const menuMap = useRef<
    Map<string, { menuItem?: boolean; subMenu?: boolean }>
  >(new Map());

  const navbarHeight = 60;
  const menuWidth = collapsed ? 48 : settings.menuWidth;

  const showNavbar = settings.navbar && urlParams.navbar !== false;
  const showMenu = settings.menu && urlParams.menu !== false;
  const showFooter = settings.footer && urlParams.footer !== false;

  const flattenRoutes = useMemo(() => getFlattenRoutes(routes) || [], [routes]);

  function onClickMenuItem(key) {
    const currentRoute = flattenRoutes.find((r) => r.key === key);
    const component = currentRoute.component;
    const preload = component.preload();
    NProgress.start();
    preload.then(() => {
      history.push(currentRoute.path ? currentRoute.path : `/${key}`);
      NProgress.done();
    });
  }

  function toggleCollapse() {
    setCollapsed((collapsed) => !collapsed);
  }

  const paddingLeft = showMenu ? { paddingLeft: menuWidth } : {};
  const paddingTop = showNavbar ? { paddingTop: navbarHeight } : {};
  const paddingStyle = { ...paddingLeft, ...paddingTop };

  function renderRoutes(locale) {
    routeMap.current.clear();
    return function travel(_routes: IRoute[], level, parentNode = []) {
      return _routes.map((route) => {
        const { breadcrumb = true, ignore } = route;
        const iconDom = getIconFromKey(route.key);
        const titleDom = (
          <>
            {iconDom} {locale[route.name] || route.name}
          </>
        );

        routeMap.current.set(
          `/${route.key}`,
          breadcrumb ? [...parentNode, route.name] : []
        );

        const visibleChildren = (route.children || []).filter((child) => {
          const { ignore, breadcrumb = true } = child;
          if (ignore || route.ignore) {
            routeMap.current.set(
              `/${child.key}`,
              breadcrumb ? [...parentNode, route.name, child.name] : []
            );
          }

          return !ignore;
        });

        if (ignore) {
          return '';
        }
        if (visibleChildren.length) {
          menuMap.current.set(route.key, { subMenu: true });
          return (
            <SubMenu key={route.key} title={titleDom}>
              {travel(visibleChildren, level + 1, [...parentNode, route.name])}
            </SubMenu>
          );
        }
        menuMap.current.set(route.key, { menuItem: true });
        return <MenuItem key={route.key}>{titleDom}</MenuItem>;
      });
    };
  }

  function updateMenuStatus() {
    const pathKeys = pathname.split('/');
    let newSelectedKeys: string[] = [];
    let newOpenKeys: string[] = [...openKeys];
    while (pathKeys.length > 0) {
      const currentRouteKey = pathKeys.join('/');
      const menuKey = currentRouteKey.replace(/^\//, '');
      const menuType = menuMap.current.get(menuKey);
      newSelectedKeys.push(menuKey);
      if (!openKeys.includes(menuKey)) {
        newOpenKeys.push(menuKey);
      }
      pathKeys.pop();
    }
    if(pathname.startsWith('/metricset/preview/')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"metricset/list"])
    }
    if(pathname.startsWith('/stat/preview/')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"stat/list"])
    }
    if(pathname.startsWith('/track/stat/')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"stat/list"])
    }
    if(pathname.startsWith('/view/preview/')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"view/list"])
    }
    if(pathname.startsWith('/project/preview/')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"project/list"])
    }
    if(pathname.startsWith('/project/manage/')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"project/list"])
    }
    if(pathname.startsWith('/department/manage')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"department/manage"])
      newOpenKeys = arrayDistinct([...newOpenKeys,"system"]);
    }
    if(pathname.startsWith('/component/list')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"component/list"]);
      newOpenKeys = arrayDistinct([...newOpenKeys,"system"]);
    }
    if(pathname.startsWith('/user/list')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"user/list"]);
      newOpenKeys = arrayDistinct([...newOpenKeys,"system"]);
    }
    if(pathname.startsWith('/authorize')){
      newSelectedKeys = arrayDistinct([...newSelectedKeys,"authorize"]);
      newOpenKeys = arrayDistinct([...newOpenKeys,"system"]);
    }
    setSelectedKeys(newSelectedKeys);
    setOpenKeys(newOpenKeys);
  }

  useEffect(() => {
    const routeConfig = routeMap.current.get(pathname);
    setBreadCrumb(routeConfig || []);
    updateMenuStatus();
  }, [pathname]);
  return (
    <Layout className={styles.layout}>
      <div
        className={cs(styles['layout-navbar'], {
          [styles['layout-navbar-hidden']]: !showNavbar,
        })}
      >
        <Navbar show={showNavbar} />
      </div>
      {userLoading ? (
        <Spin className={styles['spin']} />
      ) : (
        <Layout>
          {showMenu && (
            <Sider
              className={styles['layout-sider']}
              width={menuWidth}
              collapsed={collapsed}
              onCollapse={setCollapsed}
              trigger={null}
              collapsible
              breakpoint="xl"
              style={paddingTop}
            >
              <div className={styles['menu-wrapper']}>
                <Menu
                  collapse={collapsed}
                  onClickMenuItem={onClickMenuItem}
                  selectedKeys={selectedKeys}
                  openKeys={openKeys}
                  onClickSubMenu={(_, openKeys) => {
                    setOpenKeys(openKeys);
                  }}
                >
                  {renderRoutes(locale)(routes, 1)}
                </Menu>
              </div>
              <div className={styles['collapse-btn']} onClick={toggleCollapse}>
                {collapsed ? <IconMenuUnfold /> : <IconMenuFold />}
              </div>
            </Sider>
          )}
          <Layout className={styles['layout-content']} style={paddingStyle}>
            <div className={styles['layout-content-wrapper']}>
              {!!breadcrumb.length && (
                <div className={styles['layout-breadcrumb']}>
                  <Breadcrumb>
                    {breadcrumb.map((node, index) => (
                      <Breadcrumb.Item key={index}>
                        {typeof node === 'string' ? locale[node] || node : node}
                      </Breadcrumb.Item>
                    ))}
                  </Breadcrumb>
                </div>
              )}
              <Content>
                <Switch>
                  <Route path="/project/manage/:id" component={ProjectManagePage}/>
                  <Route path="/metricset/preview/:id" component={Index}/>
                  <Route path="/project/preview/:id" component={ProjectPreviewPage}/>
                  <Route path="/stat/preview/:id" component={StatPreviewPage}/>
                  <Route path="/track/stat/:id" component={TrackStatPage}/>
                  {flattenRoutes.map((route, index) => {
                    return (
                      <Route
                        key={index}
                        path={`/${route.key}`}
                        component={route.component}
                      />
                    );
                  })}
                  <Route exact path="/">
                    <Redirect to={`/${defaultRoute}`} />
                  </Route>
                  <Route
                    path="*"
                    component={lazyload(() => import('./pages/exception/403'))}
                  />
                </Switch>
              </Content>
            </div>
            <PopMenuBox />
            {showFooter && <Footer />}
          </Layout>
        </Layout>
      )}
    </Layout>
  );
}

export default PageLayout;
