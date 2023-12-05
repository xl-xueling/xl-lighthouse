import React, {useEffect} from 'react';
import { useState } from 'react';
import { Transfer, Tree } from '@arco-design/web-react';
import { Tabs, Radio, Typography } from '@arco-design/web-react';
import TreeTransfer from "@/pages/components/transfer/tree_transfer";
import DepartmentTransfer from "@/pages/components/transfer/department_transfer";
const TabPane = Tabs.TabPane;
const style = {
    textAlign: 'center',
    marginTop: 20,
};

export default function PickUpTransfer() {

    const treeData = [
        {
            key: '1',
            title: 'Trunk 1',
            children: [
                {
                    key: '1-1',
                    title: 'Branch',
                    children: [
                        {
                            key: '1-1-1',
                            title: 'Leaf',
                        },
                        {
                            key: '1-1-2',
                            title: 'Leaf',
                        },
                    ],
                },
            ],
        },
        {
            key: '2',
            title: 'Trunk 2',
            children: [
                {
                    key: '2-1',
                    title: 'Trunk 2-1',
                },
                {
                    key: '2-2',
                    title: 'Trunk 2-2',
                },
                {
                    key: '2-3',
                    title: 'Trunk 2-3',
                },
            ],
        },
        {
            key: '3',
            title: 'Trunk 3',
        },
        {
            key: '4',
            title: 'Trunk 4',
        },
    ];

    const [targetKeys, setTargetKeys] = useState(['2-1', '2-2', '2-3', '4']);
    const onChange = (keys) => {
        setTargetKeys(keys);
    };

    return (
        <div>
            <Tabs key='card' tabPosition={"right"}>
                <TabPane key='1' title='Tab1'>
                    <DepartmentTransfer />
                </TabPane>
                <TabPane key='2' title='Tab2'>
                    <TreeTransfer
                        dataSource={treeData}
                        defaultSelectedKeys={['1-1-1']}
                        targetKeys={targetKeys}
                        onChange={onChange}
                    />
                </TabPane>
                <TabPane key='3' title='Tab3'>
                    <TreeTransfer
                        dataSource={treeData}
                        defaultSelectedKeys={['1-1-1']}
                        targetKeys={targetKeys}
                        onChange={onChange}
                    />
                </TabPane>
            </Tabs>

        </div>
    );
}