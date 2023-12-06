import React from 'react';
import { useState } from 'react';
import TreeTransfer from "@/pages/components/transfer/department_transfer/tree_transfer";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import {translate} from "@/pages/department/common";

export default function DepartmentsTransfer() {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const [targetKeys, setTargetKeys] = useState([]);

    const onChange = (keys) => {
        setTargetKeys(keys);
    };

    return (
        <TreeTransfer
            dataSource={translate(allDepartInfo)}
            defaultSelectedKeys={['1-1-1']}
            targetKeys={targetKeys}
            onChange={onChange}
        />
    );
}