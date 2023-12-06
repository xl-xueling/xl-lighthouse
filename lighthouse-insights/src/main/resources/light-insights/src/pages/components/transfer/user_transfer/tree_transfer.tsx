import React, {useCallback, useEffect, useRef} from 'react';
import { useState } from 'react';
import {Button, Input, Transfer, Tree} from '@arco-design/web-react';
import {IconDelete, IconDriveFile, IconPushpin, IconStar, IconUser} from "@arco-design/web-react/icon";
import styles from './style/index.module.less'
import debounce from "lodash/debounce";
import {requestTermList} from "@/api/user";
import {User} from "@/types/insights-web";

const TreeTransfer = ({ dataSource, targetKeys, changeCurrentDataSource,...restProps }) => {
    const generateTreeData = (treeNodes = [], checkedKeys = []) => {
        return treeNodes.map(({ children, ...props }) => ({
            ...props,
            // disabled: checkedKeys.includes(props.key),
            children: generateTreeData(children, checkedKeys),
        }));
    };

    const generateTransferData = (list = [], transferDataSource = []) => {
        list.forEach((item) => {
            transferDataSource.push(item);
            generateTransferData(item.children, transferDataSource);
        });
        return transferDataSource;
    };

    const transferDataSource = generateTransferData(dataSource);
    const treeData = generateTreeData(dataSource, targetKeys);

    // const [newDataSource,setNewDataSource] = useState([]);
    // const [newTargetKeys,setNewTargetKeys] = useState([]);

    const styleHeader = {
        fontSize:'12px',
        height:'20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
    };
    const [initTreeData,setInitTreeData] = useState([]);
    const [targetInitTreeData,setTargetInitTreeData] = useState([]);
    const [initTransferDataSource,setInitTransferDataSource] = useState([]);
    const [values, setValues] = useState([]);
    const [fetching, setFetching] = useState(false);
    const refFetchId = useRef(null);


    useEffect(() => {
        const transferDataSource = generateTransferData(dataSource);
        const treeData = generateTreeData(dataSource, targetKeys);
        setInitTransferDataSource(transferDataSource);
        setInitTreeData(treeData);
    },[dataSource])

    const [inputValue, setInputValue] = useState('');
    useEffect(() => {
        debouncedFetchUser();
    }, [inputValue]);

    // const debouncedFetchUser = useCallback(
    //     debounce((inputValue) => {
    //         refFetchId.current = Date.now();
    //         const fetchId = refFetchId.current;
    //         setFetching(true);
    //         requestTermList(null).then((result) => {
    //             if(result.code === '0'){
    //                 const arr:{key:string,title:string}[] = [];
    //                 if (refFetchId.current === fetchId) {
    //                     const users:Array<User> = result.data.list;
    //                     users.forEach(z => {
    //                         arr.push({"key":z.id+"","title":z.userName})
    //                     })
    //                     changeLastDataSource(arr);
    //                     const transferDataSource = generateTransferData([...dataSource,...arr]);
    //                     const treeData = generateTreeData([...dataSource,...arr],targetKeys);
    //                     setInitTransferDataSource(transferDataSource);
    //                     setInitTreeData(treeData);
    //                 }
    //             }
    //         })
    //     }, 500),[dataSource]);


    const generatorTreeNodes = (treeData) => {
        console.log("treeData is:" + JSON.stringify(treeData));
        return treeData.map((item) => {
            console.log("item：" + JSON.stringify(item));
            const { children, key,origin, ...rest } = item;
            const button = <Button shape={"circle"} type={"secondary"} size={"mini"} icon={<IconPushpin />}/>
            return (
                <Tree.Node key={key} {...rest} icon={(origin)? button : ''} dataRef={item}>
                    {children ? generatorTreeNodes(item.children) : null}
                </Tree.Node>
            );
        });
    };

    const debouncedFetchUser = debounce((inputValue) => {
        refFetchId.current = Date.now();
        const fetchId = refFetchId.current;
        setFetching(true);
        requestTermList(null).then((result) => {
            if(result.code === '0'){
                const arr:{key:string,title:string}[] = [];
                if (refFetchId.current === fetchId) {
                    const users:Array<User> = result.data.list;
                    users.forEach(z => {
                        arr.push({"key":z.id+"","title":z.userName})
                    })
                    changeCurrentDataSource(arr);
                }
            }
        })
    }, 500)

    return (
        <Transfer
            className={styles.transfer_panel}
            oneWay
            showSearch={false}
            onSearch={debouncedFetchUser}
            simple={false}
            targetKeys={targetKeys}
            dataSource={initTransferDataSource}
            titleTexts={[
                ({ countTotal, countSelected, checkbox }) => {
                    return (
                        <div style={styleHeader}>

                        </div>
                    );
                },
                ({ countTotal, countSelected, clear }) => {
                    return (
                        <div style={styleHeader}>
                            {`Currently Selected`}
                            <IconDelete
                                style={{ cursor: 'pointer', }}
                                onClick={clear}
                            />
                        </div>
                    );
                },
            ]}

            render={(item:any) => {
                return item.title;}}
            {...restProps}
        >
            {({ listType, onItemSelect, selectedKeys }) => {
                if (listType === 'source') {
                    const checkedKeys = [...selectedKeys, ...targetKeys];
                    return (
                        <div>
                            <Input.Search
                                style={{
                                    marginTop: 0,
                                    marginBottom:5,
                                }}
                                placeholder={"Search Users"}
                                onChange={setInputValue}
                            />
                        <Tree
                            style={{
                                padding: '0 1px',
                            }}
                            checkable
                            selectable={true}
                            checkedKeys={checkedKeys}
                            onCheck={(_, { node: { key } }) => {
                                onItemSelect(key, checkedKeys.indexOf(key) === -1);
                            }}
                            onSelect={(_, { node: { key } }) => {
                                onItemSelect(key, checkedKeys.indexOf(key) === -1);
                            }}
                        >
                            {generatorTreeNodes(initTreeData)}
                        </Tree>
                        </div>
                    );
                }
            }}
        </Transfer>
    );
};
export default TreeTransfer;