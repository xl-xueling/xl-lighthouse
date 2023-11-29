import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { Card, Tabs } from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import InfoHeader from './header';
import InfoForm from './info';
import Security from './security';
import './mock';
import Verified from './verified';
import {User} from "@/types/insights-web";

function UserInfo() {
  const t = useLocale(locale);
  const [activeTab, setActiveTab] = useState('basic');
  return (
    <div>
      <Card style={{ padding: '14px 20px' }}>
        <InfoHeader />
      </Card>
      <Card style={{ marginTop: '16px' }}>
        <Tabs activeTab={activeTab} onChange={setActiveTab} type="rounded">
          <Tabs.TabPane key="basic" title={t['userSetting.title.basicInfo']}>
            <InfoForm/>
          </Tabs.TabPane>
          <Tabs.TabPane key="security" title={t['userSetting.title.updatePasswd']}>
            <Security />
          </Tabs.TabPane>
        </Tabs>
      </Card>
    </div>
  );
}

export default UserInfo;
