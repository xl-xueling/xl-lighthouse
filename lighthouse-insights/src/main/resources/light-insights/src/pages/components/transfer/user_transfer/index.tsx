import React, {useCallback, useEffect, useRef} from 'react';
import { useState } from 'react';
import {Select, Transfer, Tree} from '@arco-design/web-react';
import { Tabs, Radio, Typography } from '@arco-design/web-react';
import TreeTransfer from "@/pages/components/transfer/user_transfer/tree_transfer";
import {useSelector} from "react-redux";
import {Department, User} from "@/types/insights-web";
import {translate} from "@/pages/department/common";
import debounce from "lodash/debounce";
import {requestTermList} from "@/api/user";
import {IconUser} from "@arco-design/web-react/icon";

export default function UsersTransfer() {

    const [targetKeys, setTargetKeys] = useState([]);

    const [dataSource,setDataSource] = useState([]);

    const changeCurrentDataSource = (searchDataSource) => {
        const targetDataSource:{key:string,title:string,disabled:boolean,selected:boolean}[] = [];
        targetKeys.forEach(z => {
            targetDataSource.push({"key":z+"",title:z+"",disabled:false,selected:true})
        })
        const filterDataSource = searchDataSource.filter(x => !targetKeys.includes(x.key));
        setDataSource([...filterDataSource.filter(x => !targetKeys.includes(x.key)),...targetDataSource]);
    }

    const onChange = (keys) => {
        setTargetKeys(keys);
        const updateDataSource = traverseTree(dataSource,keys);
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
            changeCurrentDataSource = {changeCurrentDataSource}
        />
        );
}