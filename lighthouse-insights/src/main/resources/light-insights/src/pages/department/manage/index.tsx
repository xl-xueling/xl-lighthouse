import React from 'react';
import TreeEditPanel from './tree-edit-panel';
import styles from './style/index.module.less';
import './mock';

export default function Monitor() {
  return (
    <div style={{ minHeight:500 }}>
      <div className={styles.layout}>
        <div className={styles['layout-left-side']}>
          <TreeEditPanel />
        </div>
      </div>
    </div>
  );
}
