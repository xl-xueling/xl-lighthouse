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
        breadcrumb:false,
      },
      {
        name: 'menu.metricset.list',
        key: 'metricset/list',
        breadcrumb:false,
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
        breadcrumb:false,
      },
      {
        name: 'menu.statistics.stat',
        key: 'stat/list',
        breadcrumb:false,
      },
    ],
  },

  {
    name: 'menu.order',
    key: 'order',
    children: [
      {
        name: 'menu.order.application',
        key: 'order/apply/list',
        breadcrumb:false,
      },
      {
        name: 'menu.order.approve',
        key: 'order/approve/list',
        breadcrumb:false,
      },
    ],
  },
  {
    name: 'menu.toolbox',
    key: 'toolbox',
    children: [
      {
        name: 'menu.toolbox.filterComponents',
        key: 'component/list',
        breadcrumb:false,
      },
      {
        name: 'menu.toolbox.apiCaller',
        key: 'caller/list',
        breadcrumb:false,
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
        breadcrumb:false,
        requiredPermissions: [
          { resource: 'ldp.resource', actions: ['OperationManageAble'] },
        ],
      },
      {
        name: 'menu.system.users',
        key: 'user/list',
        breadcrumb:false,
        requiredPermissions: [
          { resource: 'ldp.resource', actions: ['OperationManageAble'] },
        ],
      },
    ],
    requiredPermissions: [
      { resource: 'ldp.resource', actions: ['OperationManageAble'] },
    ],
  },
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
