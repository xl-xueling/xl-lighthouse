import auth, { AuthParams } from '@/utils/authentication';
import { useEffect, useMemo, useState } from 'react';

export type IRoute = AuthParams & {
  name: string;
  key: string;
  // 当前页是否展示面包屑
  breadcrumb?: boolean;
  children?: IRoute[];
  // 当前路由是否渲染菜单项，为 true 的话不会在菜单中显示，但可通过路由地址访问。
  ignore?: boolean;
};

export const routes: IRoute[] = [
  {
    name: 'menu.dashboard',
    key: 'dashboard',
    children: [
      {
        name: 'menu.dashboard.workplace',
        key: 'dashboard/workplace',
      },
      {
        name: 'monitor',
        key: 'dashboard/monitor',
      },
      {
        name: 'menu.dashboard.workplace',
        key: 'metricset/list',
      },
      {
        name: '指标集管理',
        key: 'metricset/manage',
        breadcrumb:false,
      },
    ],
  },

  {
    name: 'menu.visualization',
    key: 'visualization',
    children: [
      {
        name: 'menu.visualization.dataAnalysis',
        key: 'visualization/data-analysis',
        requiredPermissions: [
          { resource: 'menu.visualization.dataAnalysis', actions: ['read'] },
        ],
      },
      {
        name: 'menu.visualization.multiDimensionDataAnalysis',
        key: 'visualization/multi-dimension-data-analysis',
        requiredPermissions: [
          {
            resource: 'menu.visualization.dataAnalysis',
            actions: ['read', 'write'],
          },
          {
            resource: 'menu.visualization.multiDimensionDataAnalysis',
            actions: ['write'],
          },
        ],
        oneOfPerm: true,
      },
    ],
  },
  // {
  //   name: 'menu.list',
  //   key: 'list',
  //   children: [
  //     {
  //       name: 'menu.list.searchTable',
  //       key: 'list/search-table',
  //     },
  //     {
  //       name: 'menu.list.cardList',
  //       key: 'list/card',
  //     },
  //   ],
  // },
  // {
  //   name: '我的关注',
  //   key: 'favorite',
  //   children: [
  //     {
  //       name: '统计项',
  //       key: 'list/search-table',
  //     },
  //     {
  //       name: '统计工程',
  //       key: 'list/card',
  //     },
  //   ],
  // },

  {
    name: 'menu.favorites',
    key: 'favorites',
    children: [
      {
        name: 'menu.favorites.project',
        key: 'favorites/project/list',
      },
      {
        name: 'menu.favorites.stat',
        key: 'favorites/stat/list',
      },
    ],
  },
  {
    name: 'menu.statistics',
    key: 'statistics',
    children: [
      {
        name: 'menu.statistics.project',
        key: 'project/list',
      },
      {
        name: 'menu.statistics.stat',
        key: 'stat/list',
      },
    ],
  },
  {
    name: 'menu.order',
    key: 'order',
    children: [
      {
        name: 'menu.order.application',
        key: 'application/list',
      },
      {
        name: 'menu.order.approve',
        key: 'approve/list',
      },
    ],
  },
  {
    name: 'menu.system',
    key: 'system',
    children: [
      {
        name: 'menu.system.department',
        key: 'department/manage',
      },
      {
        name: 'menu.system.users',
        key: 'user/list',
      },
      {
        name: 'menu.system.user.settings',
        key: 'user/settings',
        ignore:true,
      },
      {
        name: 'menu.system.filterComponents',
        key: 'components/filter/list',
      },
      {
        name: 'menu.system.document',
        key: 'document/list',
      },
    ],
  },

  //
  // {
  //   name: 'menu.form',
  //   key: 'form',
  //   children: [
  //     {
  //       name: 'menu.form.group',
  //       key: 'form/group',
  //       requiredPermissions: [
  //         { resource: 'menu.form.group', actions: ['read', 'write'] },
  //       ],
  //     },
  //     {
  //       name: 'menu.form.step',
  //       key: 'form/step',
  //       requiredPermissions: [
  //         { resource: 'menu.form.step', actions: ['read'] },
  //       ],
  //     },
  //   ],
  // },
  // {
  //   name: 'menu.profile',
  //   key: 'profile',
  //   children: [
  //     {
  //       name: 'menu.profile.basic',
  //       key: 'profile/basic',
  //     },
  //   ],
  // },
  //
  // {
  //   name: 'menu.result',
  //   key: 'result',
  //   children: [
  //     {
  //       name: 'menu.result.success',
  //       key: 'result/success',
  //       breadcrumb: false,
  //     },
  //     {
  //       name: 'menu.result.error',
  //       key: 'result/error',
  //       breadcrumb: false,
  //     },
  //   ],
  // },
  // {
  //   name: 'menu.exception',
  //   key: 'exception',
  //   children: [
  //     {
  //       name: 'menu.exception.403',
  //       key: 'exception/403',
  //     },
  //     {
  //       name: 'menu.exception.404',
  //       key: 'exception/404',
  //     },
  //     {
  //       name: 'menu.exception.500',
  //       key: 'exception/500',
  //     },
  //   ],
  // },
  // {
  //   name: 'menu.user',
  //   key: 'user',
  //   children: [
  //     {
  //       name: 'menu.user.info',
  //       key: 'user/info',
  //     },
  //   ],
  // },
  // {
  //   name: 'menu.stat',
  //   key: 'stat',
  //   children: [
  //     {
  //       name: 'menu.stat.projectList',
  //       key: 'project/list',
  //     },
  //     {
  //       name: 'menu.stat.statList',
  //       key: 'stat/list',
  //     },
  //   ],
  // },

];

export const getName = (path: string, routes) => {
  return routes.find((item) => {
    const itemPath = `/${item.key}`;
    if (path === itemPath) {
      return item.name;
    } else if (item.children) {
      return getName(path, item.children);
    }
  });
};

export const generatePermission = (role: string) => {
  const actions = role === 'admin' ? ['*'] : ['read'];
  const result = {};
  routes.forEach((item) => {
    if (item.children) {
      item.children.forEach((child) => {
        result[child.name] = actions;
      });
    }
  });
  return result;
};

const useRoute = (userPermission): [IRoute[], string] => {
  const filterRoute = (routes: IRoute[], arr = []): IRoute[] => {
    if (!routes.length) {
      return [];
    }
    for (const route of routes) {
      const { requiredPermissions, oneOfPerm } = route;
      let visible = true;
      if (requiredPermissions) {
        visible = auth({ requiredPermissions, oneOfPerm }, userPermission);
      }

      if (!visible) {
        continue;
      }
      if (route.children && route.children.length) {
        const newRoute = { ...route, children: [] };
        filterRoute(route.children, newRoute.children);
        if (newRoute.children.length) {
          arr.push(newRoute);
        }
      } else {
        arr.push({ ...route });
      }
    }

    return arr;
  };

  const [permissionRoute, setPermissionRoute] = useState(routes);

  useEffect(() => {
    const newRoutes = filterRoute(routes);
    setPermissionRoute(newRoutes);
  }, [JSON.stringify(userPermission)]);

  const defaultRoute = useMemo(() => {
    const first = permissionRoute[0];
    if (first) {
      const firstRoute = first?.children?.[0]?.key || first.key;
      return firstRoute;
    }
    return '';
  }, [permissionRoute]);

  return [permissionRoute, defaultRoute];
};

export default useRoute;
