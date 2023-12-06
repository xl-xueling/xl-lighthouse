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
const TabPane = Tabs.TabPane;
const style = {
    textAlign: 'center',
    marginTop: 20,
};

export default function UsersTransfer() {

    const [targetKeys, setTargetKeys] = useState([]);

    const [dataSource,setDataSource] = useState([]);

    const changeCurrentDataSource = (newDataSource) => {
        const arr:{key:string,title:string,disabled:boolean,origin:boolean}[] = [];
        targetKeys.forEach(z => {
            arr.push({"key":z+"",title:z+"",disabled:false,origin:true})
        })
        setDataSource([...newDataSource,...arr]);
    }

    const onChange = (keys) => {
        setTargetKeys(keys);
    };

    return (
        <TreeTransfer
            dataSource={dataSource}
            targetKeys={targetKeys}
            onChange={onChange}
            changeCurrentDataSource = {changeCurrentDataSource}
        />
        );
}