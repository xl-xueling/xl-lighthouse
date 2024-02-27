import React, {useEffect, useState} from 'react';
import {Card, Notification, Spin, Tabs} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import InfoHeader from './header';
import InfoForm from './basic';
import Security from './security';
import {useSelector} from "react-redux";
import {Department, TreeNode, User} from "@/types/insights-web";
import {requestChangeState, requestFetchUserInfo} from "@/api/user";

export default function Index() {
  const t = useLocale(locale);
  const [activeTab, setActiveTab] = useState('basic');
  const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
  const departLoading = useSelector((state: {departLoading:boolean}) => state.departLoading);
  const [loading,setLoading] = useState<boolean>(true);
  const [userInfo,setUserInfo] = useState<User>(null);
  const [timestamp,setTimestamp] = useState<number>(Date.now);

    const fetchData = async () => {
        await requestFetchUserInfo().then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                setUserInfo(response.data);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const callback = () => {
        setTimestamp(Date.now);
    }

    useEffect(() => {
         fetchData().then();
    },[timestamp])


  return (
      <Spin loading={loading} style={{ display: 'block' }}>
          <Card style={{ padding: '14px 20px' }}>
            <InfoHeader userInfo={userInfo} />
          </Card>
          <Card style={{ marginTop: '16px' }}>
            <Tabs activeTab={activeTab} onChange={setActiveTab} type="rounded">
              <Tabs.TabPane key="basic" title={t['userSetting.title.basicInfo']}>
                  {userInfo && <InfoForm userInfo={userInfo} allDepartInfo={allDepartInfo} callback={callback}/>}
              </Tabs.TabPane>
              <Tabs.TabPane key="security" title={t['userSetting.title.updatePasswd']}>
                <Security userInfo={userInfo} />
              </Tabs.TabPane>
            </Tabs>
          </Card>
      </Spin>
  );
}