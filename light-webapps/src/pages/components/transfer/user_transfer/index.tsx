import React, {useCallback, useEffect, useRef} from 'react';
import { useState } from 'react';
import {Select, Transfer, Tree} from '@arco-design/web-react';
import { Tabs, Radio, Typography } from '@arco-design/web-react';
import TreeTransfer from "@/pages/components/transfer/user_transfer/tree_transfer";
import {useSelector} from "react-redux";
import {Department, User} from "@/types/insights-web";
import {translate} from "@/pages/department/base";
import debounce from "lodash/debounce";
import {requestTermList} from "@/api/user";
import {IconUser} from "@arco-design/web-react/icon";

const UsersTransfer = React.forwardRef((none,ref)  => {

    const [targetKeys, setTargetKeys] = useState([]);

    const [targetObjects, setTargetObjects] = useState<any>([]);

    const [dataSource,setDataSource] = useState([]);

    const changeCurrentDataSource = (searchDataSource) => {
        const targetDataSource:{key:string,title:string,disabled:boolean,selected:boolean}[] = [];
        targetObjects.forEach(z => {
            targetDataSource.push({"key":z.key,title:z.title,disabled:false,selected:true})
        })
        setDataSource([...searchDataSource.filter(x => !targetKeys.includes(x.key)),...targetDataSource]);
    }

    const onChange = (keys) => {
        setTargetKeys(keys);
        const tempTargetObjects = [];
        keys.forEach(z => {
            const obj = dataSource.filter(x => x.key == z)[0];
            tempTargetObjects.push(obj);
        })
        setTargetObjects(tempTargetObjects);
        const updateDataSource = traverseTree(dataSource,keys);
        setDataSource(updateDataSource);
    };

    function getData(){
        return targetKeys;
    }

    React.useImperativeHandle(ref,() => ({
        getData
    }));

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
})

export default UsersTransfer;
