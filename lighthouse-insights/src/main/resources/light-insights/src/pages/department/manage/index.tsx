import React from 'react';

import styles from './style/index.module.less';
import DepartmentManagePanel from "./department_manage";


export default function DepartmentManageIndex() {
  return (
    <div>
      <div className={styles.layout}>
        <div className={styles['layout-left-side']}>
          <DepartmentManagePanel />
        </div>
      </div>
    </div>
  );
}
