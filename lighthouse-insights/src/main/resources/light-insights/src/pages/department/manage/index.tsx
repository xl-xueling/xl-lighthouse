import React from 'react';

import styles from './style/index.module.less';
import ManagePanel from "./manage";


export default function DepartmentManageIndex() {
  return (
    <div>
      <div className={styles.layout}>
        <div className={styles['layout-left-side']}>
          <ManagePanel />
        </div>
      </div>
    </div>
  );
}
