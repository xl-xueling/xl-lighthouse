import React, {useEffect, useState} from 'react';
import {Card, Spin, Tabs} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import InfoHeader from './header';
import InfoForm from './info';
import Security from './security';
import {useSelector} from "react-redux";
import {Department, User} from "@/types/insights-web";

export default function Index() {
  const t = useLocale(locale);
  const [activeTab, setActiveTab] = useState('basic');
  const userInfo = useSelector((state: {userInfo:User}) => state.userInfo);
  const userLoading = useSelector((state: {userLoading:boolean}) => state.userLoading);
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
  const departLoading = useSelector((state: {departLoading:boolean}) => state.departLoading);
  const [loading,setLoading] = useState<boolean>(true);

  useEffect(() => {
      if(!userLoading && !departLoading){
          setLoading(false);
      }
  },[userLoading,departLoading])

  return (
      <Spin loading={loading} style={{ display: 'block' }}>
          <Card style={{ padding: '14px 20px' }}>
            <InfoHeader userInfo={userInfo} />
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
      </Spin>
  );
}