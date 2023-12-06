import React, {useEffect} from 'react';
import { useState } from 'react';
import TreeTransfer from "@/pages/components/transfer/department_transfer/tree_transfer";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import {translate} from "@/pages/department/common";

export default function DepartmentsTransfer() {

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const [targetKeys, setTargetKeys] = useState([]);

    const [dataSource,setDataSource] = useState([]);

    useEffect(() => {
        setDataSource(translate(allDepartInfo));
    },[])

    const onChange = (keys) => {
        setTargetKeys(keys);
        const updateDataSource = traverseTree(dataSource,keys);
        console.log("updateDataSource is:" + JSON.stringify(updateDataSource))
        setDataSource(updateDataSource);
    };

    function traverseTree(tree,keys) {
        return tree.map(node => {
            const newNode = { ...node, selected: !!keys.includes(node.key)};
            if (newNode.children && Array.isArray(newNode.children)) {
                newNode.children = traverseTree(newNode.children,keys);
            }
            return newNode;
        });
    }

    return (
        <TreeTransfer
            dataSource={dataSource}
            targetKeys={targetKeys}
            onChange={onChange}
        />
    );
}