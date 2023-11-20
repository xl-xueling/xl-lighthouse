import Mock from 'mockjs';
import { isSSR } from '@/utils/is';

import './user';
import './message-box';
import '../pages/register/mock'
import '../pages/department/mock'
import '../pages/user/mock'
import '../pages/privilege/mock'
import '../pages/project/mock'

if (!isSSR) {
  Mock.setup({
    timeout: '500-1500',
  });
}
