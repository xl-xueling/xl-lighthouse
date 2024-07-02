import React, {useContext, useEffect, useState} from 'react';
import {
  Tooltip,
  Input,
  Avatar,
  Select,
  Dropdown,
  Menu,
  Divider,
  Message,
  Button, Notification, Badge,
} from '@arco-design/web-react';
import {
  IconLanguage,
  IconNotification,
  IconSunFill,
  IconMoonFill,
  IconUser,
  IconSettings,
  IconPoweroff,
  IconExperiment,
  IconDashboard,
  IconInteraction,
  IconTag,
  IconLoading, IconCalendarClock,
} from '@arco-design/web-react/icon';
import { useSelector, useDispatch } from 'react-redux';
import { GlobalState } from '@/store';
import { GlobalContext } from '@/context';
import useLocale from '@/utils/useLocale';
import DarkLogo from '@/assets/dark-logo.svg';
import LightLogo from '@/assets/light-logo.svg';
import Logo from '@/assets/logo.svg';
import {Switch, Route, Redirect, useHistory} from 'react-router-dom';

import MessageBox from '@/components/MessageBox';
import IconButton from './IconButton';
import Settings from '../Settings';
import styles from './style/index.module.less';
import defaultLocale from '@/locale';
import useStorage from '@/utils/useStorage';
import { generatePermission } from '@/routes';
import {removeLoginStatus} from "@/utils/checkLogin";
import {requestPendCount} from "@/api/order";

function Navbar({ show }: { show: boolean }) {
  const t = useLocale();
  const { userInfo, userLoading } = useSelector((state: GlobalState) => state);
  const dispatch = useDispatch();
  const history = useHistory();

  const [_, setUserStatus] = useStorage('userStatus');
  const [role, setRole] = useStorage('userRole', 'admin');
  const [pendCount,setPendCount] = useState<number>(0);

  const { setLang, lang, theme, setTheme } = useContext(GlobalContext);

  function logout() {
    setUserStatus('logout');
    removeLoginStatus();
    window.location.href = '/login';
  }

  const fetchData = async () => {
    await requestPendCount().then((response) => {
      const {code, data ,message} = response;
      if(code == '0'){
        setPendCount(data)
      }else{
        Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
      }
    }).catch((error) => {
      console.log(error);
    })
  }

  const intervalCallback = () => {
    fetchData().then();
  };

  useEffect(() => {
    intervalCallback();
    const intervalId = setInterval(intervalCallback, 5 * 60 * 1000);
    return () => {
      clearInterval(intervalId);
    };
  },[])

  function onMenuItemClick(key) {
    if (key === 'logout') {
      logout();
    }else if(key === 'settings'){
      window.location.href = '/user/settings';
    }
  }

  if (!show) {
    return (
      <div className={styles['fixed-settings']}>
        <Settings
          trigger={
            <Button icon={<IconSettings />} type="primary" size="large" />
          }
        />
      </div>
    );
  }

  const handleChangeRole = () => {
    const newRole = role === 'admin' ? 'user' : 'admin';
    setRole(newRole);
  };

  const droplist = (
    <Menu onClickMenuItem={onMenuItemClick}>
      <Menu.Item key="settings">
        <IconSettings className={styles['dropdown-icon']} />
        {t['menu.user.setting']}
      </Menu.Item>
      <Menu.Item key="logout">
        <IconPoweroff className={styles['dropdown-icon']} />
        {t['navbar.logout']}
      </Menu.Item>
    </Menu>
  );

  const handleClick = (href) => {
    history.push(href);
  };

  return (
    <div className={styles.navbar}>
      <div className={styles.left}>
        <div className={styles.logo} onClick={() => handleClick('/')}>
          {theme == "dark" ? <DarkLogo/> : <LightLogo/>}
          <div className={styles['logo-name']}>XL-LightHouse</div>
        </div>
      </div>
      <ul className={styles.right}>
        {/*<li>*/}
        {/*  <Input.Search*/}
        {/*    className={styles.round}*/}
        {/*    placeholder={t['navbar.search.placeholder']}*/}
        {/*  />*/}
        {/*</li>*/}
        <li>
          <Select
            triggerElement={<IconButton icon={<IconLanguage />} />}
            options={[
              { label: '中文', value: 'zh-CN' },
              { label: 'English', value: 'en-US' },
            ]}
            value={lang}
            triggerProps={{
              autoAlignPopupWidth: false,
              autoAlignPopupMinWidth: true,
              position: 'br',
            }}
            trigger="hover"
            onChange={(value) => {
              setLang(value);
              const nextLang = defaultLocale[value];
              Message.info(`${nextLang['message.lang.tips']}${value}`);
            }}
          />
        </li>
        <li>
          <Badge count={pendCount} dot offset={[2, -2]}>
            <IconButton onClick={() => handleClick('/order/approve/list')} icon={<IconCalendarClock
                style={{
                  color: '#888',
                  fontSize: 18,
                  verticalAlign: -3,
                }}
            />} />
          </Badge>
        </li>
        <li>
          <Tooltip
            content={
              theme === 'light'
                ? t['settings.navbar.theme.toDark']
                : t['settings.navbar.theme.toLight']
            }
          >
            <IconButton
              icon={theme !== 'dark' ? <IconMoonFill /> : <IconSunFill />}
              onClick={() => setTheme(theme === 'light' ? 'dark' : 'light')}
            />
          </Tooltip>
        </li>
        {/*<Settings />*/}
        {userInfo && (
          <li>
            <Dropdown droplist={droplist} position="br" disabled={userLoading}>
              <Avatar size={32} style={{ cursor: 'pointer',backgroundColor: 'rgb(123 187 221)'}}>
                <IconUser/>
              </Avatar>
            </Dropdown>
          </li>
        )}
      </ul>
    </div>
  );
}

export default Navbar;
