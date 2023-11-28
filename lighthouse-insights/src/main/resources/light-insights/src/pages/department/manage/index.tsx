import React from 'react';
import DepartmentManagePanel from './department_manage';
import styles from './style/index.module.less';

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
