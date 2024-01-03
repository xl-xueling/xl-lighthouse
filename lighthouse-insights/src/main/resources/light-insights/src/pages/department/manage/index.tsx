import React from 'react';

import styles from './style/index.module.less';
import ManagePanel from "./manage";
import {Breadcrumb} from "@arco-design/web-react";
import {IconHome} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
const BreadcrumbItem = Breadcrumb.Item;

export default function DepartmentManageIndex() {
    const t = useLocale(locale);
  return (
    <div>
        <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
            <BreadcrumbItem>
                <IconHome />
            </BreadcrumbItem>
            <BreadcrumbItem style={{fontWeight:20}}>{t['departmentManage.breadcrumb.title']}</BreadcrumbItem>
        </Breadcrumb>
      <div className={styles.layout}>
        <div className={styles['layout-left-side']}>
          <ManagePanel />
        </div>
      </div>
    </div>
  );
}
