import React, { useEffect, useRef, useState } from 'react';
import {TreeSelect} from "@arco-design/web-react";
import {ArcoTreeNode} from "@/types/insights-web";
import {fetchAllDepartmentData, translate} from "@/pages/department/common";
import {getDataWithLocalCache} from "@/utils/localCache";

export default function DepartmentTreeSelect () {

    const [treeData,setTreeData] = useState([]);

    useEffect(() => {
        const proc = async () => {
            const allDepartInfo = await getDataWithLocalCache('cache_all_department',300,fetchAllDepartmentData);
            const data: ArcoTreeNode[] = translate(allDepartInfo);
            setTreeData(data);
        }
        proc().then();
    },[])

    return (
        <TreeSelect showSearch={true}
                    filterTreeNode={(inputText,node) => {
                        return node.props.title.toLowerCase().indexOf(inputText.toLowerCase()) > -1;
                    }}
                    placeholder='Select Department'
                    allowClear={true}  treeData={treeData} />
    );
}