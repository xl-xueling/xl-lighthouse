import React, {useEffect} from 'react';
import { useState } from 'react';
import { Transfer, Tree } from '@arco-design/web-react';
import { Tabs, Radio, Typography } from '@arco-design/web-react';
import TreeTransfer from "@/pages/components/transfer/tree_transfer";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import {translate} from "@/pages/department/common";
const TabPane = Tabs.TabPane;
const style = {
    textAlign: 'center',
    marginTop: 20,
};

export default function DepartmentTransfer() {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const [targetKeys, setTargetKeys] = useState(['2-1', '2-2', '2-3', '4']);
    const onChange = (keys) => {
        setTargetKeys(keys);
    };

    return (
        <TabPane key='1' title='Tab1'>
        <TreeTransfer
            dataSource={translate(allDepartInfo)}
            defaultSelectedKeys={['1-1-1']}
            targetKeys={targetKeys}
            onChange={onChange}
        />
    </TabPane>);
}