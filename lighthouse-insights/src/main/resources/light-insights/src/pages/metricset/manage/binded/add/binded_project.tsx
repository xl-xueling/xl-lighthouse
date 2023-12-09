import {Form, Grid, Input, Message, Modal, Radio, Space, Tabs, Tree, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {Col} from "antd";
import UsersTransfer from "@/pages/components/transfer/user_transfer";
import DepartmentsTransfer from "@/pages/components/transfer/department_transfer";
import {IconCaretDown, IconCaretRight} from "@arco-design/web-react/icon";
import {GrantPrivileges, MetricSet} from "@/types/insights-web";
import {ResultData} from "@/types/insights-common";
import {requestGrantPrivilege} from "@/api/privilege";
import {requestCreate} from "@/api/metricset";
import styles from "@/pages/project/manage/style/index.module.less";
import ProjectTree from "@/pages/project/common/project-tree";
import GroupManagePanel from "@/pages/group/manage";
import GroupAddPanel from "@/pages/group/add/group_add";
import React, {useState} from "react";

export default function BindedProject({onClose = null}) {

    const TreeNode = Tree.Node;
    const TreeData = [
        {
            title: 'Trunk 0-0',
            key: '0-0',
        },
        {
            title: 'Trunk 0-1',
            key: '0-1',
        },
        {
            title: 'Trunk 0-2',
            key: '0-2',
        },
        {
            title: 'Trunk 0-3',
            key: '0-3',
        },
        {
            title: 'Trunk 0-4',
            key: '0-4',
        },
        {
            title: 'Trunk 0-5',
            key: '0-5',
        },
    ];

    const [inputValue, setInputValue] = useState('');

    return (
        <div style={{ minHeight:500 }}>
            <div className={styles.layout}>
                <div className={styles['layout-left-side']} style={{ border:'1px solid var(--color-border-1)'}}>
                        <Input.Search
                            style={{
                                marginBottom: 8,
                            }}
                        />
                        <Tree
                            blockNode
                            checkable
                            treeData={TreeData}
                            renderTitle={({title}) => {
                                const a = title.toString();
                                if (inputValue) {
                                    const index = a.toLowerCase().indexOf(inputValue.toLowerCase());

                                    if (index === -1) {
                                        return title;
                                    }

                                    const prefix = a.substr(0, index);
                                    const suffix = a.substr(index + inputValue.length);
                                    return (
                                        <span>
                                        {prefix}
                                            <span style={{color: 'var(--color-primary-light-4)'}}>
                                          {a.substr(index, inputValue.length)}
                                        </span>
                                            {suffix}
                                      </span>
                                    );
                                }

                                return a;
                            }}
                            />
                </div>
                <div>
                    sssss1
                </div>
            </div>
        </div>

    );
}