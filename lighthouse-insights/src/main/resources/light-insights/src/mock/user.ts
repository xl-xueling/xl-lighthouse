import Mock from 'mockjs';
import { isSSR } from '@/utils/is';
import setupMock from '@/utils/setupMock';
import { generatePermission } from '@/routes';

if (!isSSR) {
  Mock.XHR.prototype.withCredentials = true;

  setupMock({
    setup: () => {
      // 用户信息
      const userRole = window.localStorage.getItem('userRole') || 'admin';

      // 登录
      Mock.mock(new RegExp('/api/v1/user/login'), (params) => {
        const { userName, password } = JSON.parse(params.body);
        if (!userName) {
          return {
            code:'1',
            msg:'用户名不能为空',
            data:{
            }
          };
        }
        if (!password) {
          return {
            code:'2',
            msg:'密码不能为空',
            data:{}
          };
        }
        if (userName === 'admin' && password === 'admin') {
          return {
            code:'0',
            msg:'登录成功！',
            data:{
              token:"sasucessawwxoks"
            }
          };
        }
        return {
          code:'3',
          msg:'账号密码错误！',
          data:{}
        };
      });
    },
  });
}
