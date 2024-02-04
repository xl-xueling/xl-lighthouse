import React from 'react';
import {
  Link,
  Card,
  Divider,
  Message,
  Typography, Space, Avatar,
} from '@arco-design/web-react';
import {
  IconFile,
  IconStorage,
  IconSettings,
  IconMobile,
  IconFire,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/shortcuts.module.less';
import {getIcon} from "@/pages/common/desc/base";
import { VscOutput } from "react-icons/vsc";
import {useSelector} from "react-redux";
import {Project} from "@/types/insights-web";
import {getRandomString} from "@/utils/util";

function Shortcuts() {
  const t = useLocale(locale);
  const staredProjectInfo = useSelector((state: {staredProjectInfo:Array<Project>}) => state.staredProjectInfo || []);
  const shortcuts = [
    {
      title: t['workplace.shortcuts.metricManage'],
      key: 'metricManage',
      icon: getIcon('metric'),
    },
    {
      title: t['workplace.shortcuts.projectManage'],
      key: 'projectManage',
      icon: getIcon('project'),
    },
    {
      title: t['workplace.shortcuts.statManage'],
      key: 'statManage',
      icon: getIcon('stat'),
    },
    {
      title: t['workplace.shortcuts.pendApprove'],
      key: 'pendApprove',
      icon: getIcon('approve'),
    },
    {
      title: t['workplace.shortcuts.myApply'],
      key: 'myApply',
      icon: getIcon('apply'),
    },
  ];


  function onClickShortcut(key) {
    if(key == 'metricManage'){
      handleClick('/metricset/list')
    }else if(key == 'projectManage'){
      handleClick('/project/list')
    }else if(key == 'statManage'){
      handleClick('/stat/list')
    }else if(key == 'pendApprove'){
      handleClick('/order/approve/list')
    }else if(key == 'myApply'){
      handleClick('/order/apply/list')
    }
  }

  const handleClick = (href) => {
    window.open(href, '_self');
  };

  return (
    <Card>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <Typography.Title heading={6}>
          {t['workplace.shortcuts']}
        </Typography.Title>
      </div>
      <div className={styles.shortcuts}>
        {shortcuts.map((shortcut) => (
          <div
            className={styles.item}
            key={shortcut.key}
            onClick={() => onClickShortcut(shortcut.key)}
          >
            <div className={styles.icon}>{shortcut.icon}</div>
            <div className={styles.title}>{shortcut.title}</div>
          </div>
        ))}
      </div>
      <Divider />
      <div className={styles.recent}>{t['workplace.label.staredProject']}</div>
      <div>
        {
          staredProjectInfo.slice(0,4).map(z => {
            return <Card
                onClick={() => handleClick('/project/manage/' + z.id)}
                key={getRandomString()}
                bordered={true}
                hoverable
                style={{marginBottom: 20,cursor:'pointer'}}
            >
              <Space
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                  }}
              >
                <Space>
                  <Avatar
                      style={{
                        backgroundColor: '#165DFF',
                      }}
                      size={25}
                  >
                    {getIcon('project')}
                  </Avatar>
                  <Typography.Text>{z.title}</Typography.Text>
                </Space>
              </Space>
            </Card>
          })
        }
      </div>
    </Card>
  );
}

export default Shortcuts;
