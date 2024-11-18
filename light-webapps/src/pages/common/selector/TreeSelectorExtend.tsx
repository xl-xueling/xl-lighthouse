import React, { useState, useEffect } from 'react';
import {Divider, Tag, TreeSelect} from "@arco-design/web-react";
import {isSingleLevel} from "@/pages/department/base";
import {ArcoTreeNode} from "@/types/insights-web";
import {useUpdateEffect} from "ahooks";


export default function TreeSelectorExtend ({resetTime , treeData,onChange,placeholder = 'Please Select'}){

    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);

    useUpdateEffect(() => {
        setSelectedKeys([]);
    },[resetTime])

    const handleSelectChange = (value: string[]) => {
        setSelectedKeys(value);
    };

    const flattenTree = (nodes: ArcoTreeNode[]): string[] => {
        let keys: string[] = [];
        nodes.forEach(node => {
            keys.push(node.key);
            if (node.children) {
                keys = keys.concat(flattenTree(node.children));
            }
        });
        return keys;
    };

    const handleChangeAll = (checked:boolean) => {
        if(checked){
            const allKeys = flattenTree(treeData);
            setSelectedKeys(allKeys);
        }else{
            setSelectedKeys([]);
        }
    };

    const handleDeselectAll = () => {
        setSelectedKeys([]);
    };

    useUpdateEffect(() => {
        onChange(selectedKeys);
    },[selectedKeys])

    const getAllSelect = () => {
        if(treeData && treeData.length <= 30 && isSingleLevel(treeData)){
            return <>
                <Divider style={{ margin: 0 }} />
                <div
                    className={'disable-select'}
                    style={{
                        display: 'flex',
                        alignItems: 'right',
                        marginTop:'5px',
                        padding: '3px 15px',
                    }}
                >
                    <Tag checkable onCheck={handleChangeAll}>全选</Tag>
                </div>
            </>
        }else{
            return null;
        }
    }

    return (
        <TreeSelect className={'disable-select'} size={"small"}
                    onChange={handleSelectChange}
                    placeholder={placeholder}
                    multiple={true}
                    treeCheckable={true}
                    treeProps={{
                        renderTitle: (props) => {
                            return (
                                <span style={{ whiteSpace: 'nowrap', }} >
                                            {props.title}
                                        </span>
                            );
                        },
                    }}
                    dropdownMenuStyle={{
                        maxHeight: 300,
                        display: 'flex',
                        flexDirection: 'column',
                    }}
                    dropdownRender={(menu) => (
                        <>
                            <div style={{ flex: 1, width:'100%',overflow: 'auto' }}>{menu}</div>
                            {getAllSelect()}
                        </>
                    )}
                    value={selectedKeys}
                    treeCheckStrictly={false}
                    allowClear={true}
                    showSearch={true}
                    treeData={treeData} />
    );
}