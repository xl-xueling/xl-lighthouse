import React, {useEffect} from 'react';
import { useState } from 'react';
import { Transfer, Tree } from '@arco-design/web-react';
import {IconDelete} from "@arco-design/web-react/icon";
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

    const transferDataSource = generateTransferData(dataSource);
    const treeData = generateTreeData(dataSource, targetKeys);

    const styleHeader = {
        fontSize:'12px',
        height:'20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
    };

    return (
        <Transfer
            className={styles.transfer_panel}
            oneWay
            simple={false}
            targetKeys={targetKeys}
            dataSource={transferDataSource}
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
                                padding: '0 14px',
                            }}
                            blockNode
                            checkable
                            selectable={true}
                            treeData={treeData}
                            checkedKeys={checkedKeys}
                            onCheck={(_, { node: { key } }) => {
                                onItemSelect(key, checkedKeys.indexOf(key) === -1);
                            }}
                            onSelect={(_, { node: { key } }) => {
                                onItemSelect(key, checkedKeys.indexOf(key) === -1);
                            }}
                        />
                    );
                }
            }}
        </Transfer>
    );
};

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

// const App = () => {
//     const [targetKeys, setTargetKeys] = useState(['2-1', '2-2', '2-3', '4']);
//
//     const onChange = (keys) => {
//         setTargetKeys(keys);
//     };
//
//     return (
//         <TreeTransfer
//             dataSource={treeData}
//             defaultSelectedKeys={['1-1-1']}
//             targetKeys={targetKeys}
//             onChange={onChange}
//         />
//     );
// };

export default TreeTransfer;