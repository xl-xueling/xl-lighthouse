import React, { useEffect, useState } from 'react';
import {
  Button,
  Avatar,
  Upload,
  Descriptions,
  Tag,
  Skeleton,
  Link,
} from '@arco-design/web-react';
import {IconCamera, IconPlus, IconUser} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/header.module.less';
import {User} from "@/types/insights-web";
import {useSelector} from "react-redux";

export default function Info() {

  const userInfo = useSelector((state: {userInfo:User}) => state.userInfo);
  const loading = useSelector((state: {userLoading:boolean}) => state.userLoading);

  const t = useLocale(locale);

  const [avatar, setAvatar] = useState('');

  function onAvatarChange(_, file) {
    setAvatar(file.originFile ? URL.createObjectURL(file.originFile) : '');
  }

  const loadingImg = (
    <Skeleton
      text={{ rows: 0 }}
      style={{ width: '100px', height: '100px' }}
      animation
    />
  );

  const loadingNode = <Skeleton text={{ rows: 1 }} animation />;
  return (
    <div className={styles['info-wrapper']}>
      <Upload showUploadList={false} onChange={onAvatarChange}>
        {loading ? (
          loadingImg
        ) : (
          <Avatar
            size={100}
            style={{ backgroundColor: 'rgb(123 187 221)' }}
          >
              <IconUser/>
          </Avatar>
        )}
      </Upload>
      <Descriptions
    className={styles['info-content']}
    column={2}
    colon="："
    labelStyle={{textAlign: 'right'}}
    data={[
        {
            label: t['userSetting.label.name'],
            value: loading ? loadingNode : userInfo.userName,
        },

        {
            label: t['userSetting.label.accountId'],
            value: loading ? loadingNode : userInfo.id,
        },
        {
            label: t['userSetting.label.phoneNumber'],
            value: loading ? (
                loadingNode
            ) : (
                <span>
                {userInfo.phone}
                    <Link role="button" className={styles['edit-btn']}>
                  {t['userSetting.btn.edit']}
                </Link>
              </span>
            ),
        },
        {
            label: 'department',
            value: loading ? (
                loadingNode
            ) : (
                <span>
                {userInfo.departmentName}
                    <Link role="button" className={styles['edit-btn']}>
                  {t['userSetting.btn.edit']}
                </Link>
              </span>
            ),
        },
        {
            label: t['userSetting.label.phoneNumber'],
            value: loading ? (
                loadingNode
            ) : (
                <span>
                {userInfo.email}
                    <Link role="button" className={styles['edit-btn']}>
                  {t['userSetting.btn.edit']}
                </Link>
              </span>
            ),
        },
        {
            label: t['userSetting.label.registrationTime'],
            value: loading ? loadingNode : userInfo.createdTime,
        },
    ]}
    />
    </div>
  );
}
