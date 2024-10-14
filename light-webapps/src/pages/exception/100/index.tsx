import React from 'react';
import { Result, Button } from '@arco-design/web-react';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import styles from './style/index.module.less';

function Exception100({fromExternalEmbedding = false,errorMessage}) {
  const t = useLocale(locale);

  return (
    <div className={fromExternalEmbedding?styles.externalEmbeddingWrapper : styles.wrapper}>
      <Result
        className={styles.result}
        status="500"
        subTitle={errorMessage}
      />
    </div>
  );
}

export default Exception100;
