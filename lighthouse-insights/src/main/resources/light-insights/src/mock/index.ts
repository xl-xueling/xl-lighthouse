import Mock from 'mockjs';
import { isSSR } from '@/utils/is';

import './message-box';
import '../pages/department/mock'
import '../pages/user/mock'
import '../pages/privilege/mock'
import '../pages/project/mock'
import '../pages/favorites/mock'
import '../pages/group/mock'
import '../pages/stat/mock'
import '../pages/metricset/mock'
import '../pages/component/mock'
import '../pages/order/application/mock'

if (!isSSR) {
  Mock.setup({
    timeout: '500-1500',
  });
}
