import React from 'react';
import { Result, Button } from '@arco-design/web-react';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import styles from './style/index.module.less';

function Exception403() {
  const t = useLocale(locale);

  return (
      <div className={styles.wrapper}>
          <Result
              className={styles.result}
              status="403"
              subTitle={t['exception.result.403.description']}
          />
      </div>
  );
}

export default Exception403;
