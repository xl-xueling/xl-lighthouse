import React, {useEffect} from 'react';
import { useState } from 'react';
import {Button, Transfer, Tree} from '@arco-design/web-react';
import {IconDelete, IconPushpin} from "@arco-design/web-react/icon";
import styles from './style/index.module.less'

const TreeTransfer = ({ dataSource, targetKeys, ...restProps }) => {
    const generateTreeData = (treeNodes = [], checkedKeys = []) => {
        return treeNodes.map(({ children, ...props }) => ({
            ...props,
            disabled: checkedKeys.includes(props.key),
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

    const [initTreeData,setInitTreeData] = useState([]);
    const [initTransferDataSource,setInitTransferDataSource] = useState([]);

    useEffect(() => {
        const transferDataSource = generateTransferData(dataSource);
        const treeData = generateTreeData(dataSource, targetKeys);
        setInitTransferDataSource(transferDataSource);
        setInitTreeData(treeData);
    },[dataSource])

    const styleHeader = {
        fontSize:'12px',
        height:'20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
    };

    const generatorTreeNodes = (treeData) => {
        return treeData.map((item) => {
            const { children, key,selected, ...rest } = item;
            const button = <Button shape={"circle"} type={"secondary"} size={"mini"} icon={<IconPushpin />}/>
            return (
                <Tree.Node key={key} {...rest} icon={(selected)? button : ''} dataRef={item}>
                    {children ? generatorTreeNodes(item.children) : null}
                </Tree.Node>
            );
        });
    };

    return (
        <Transfer
            className={styles.transfer_panel}
            oneWay
            simple={false}
            targetKeys={targetKeys}
            dataSource={initTransferDataSource}
            titleTexts={[
                ({ countTotal, countSelected, checkbox }) => {
                    return (
                        <div style={styleHeader}>
                            {`Departments`}
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
                        <Tree
                            style={{
                                padding: '0 1px',
                            }}
                            blockNode
                            checkable
                            checkStrictly
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
                    );
                }
            }}
        </Transfer>
    );
};
export default TreeTransfer;