import React, { useState, useEffect } from 'react';
import {Divider, Tag, TreeSelect} from "@arco-design/web-react";
import {isSingleLevel} from "@/pages/department/base";
import {ArcoTreeNode} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "./locale/index";

export default function TreeSelectorExtend ({value = [] , treeData,onChange,placeholder = 'Please Select'}){

    const t = useLocale(locale);

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

    const toggleChangeAll = (checked:boolean) => {
        if(checked){
            const allKeys = flattenTree(treeData);
            onChange(allKeys);
        }else{
            onChange([]);
        }
    };

    const getAllSelect = () => {
        if(treeData && treeData.length > 0 && treeData.length <= 50 && isSingleLevel(treeData)){
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
                    <Tag checkable onCheck={toggleChangeAll}>{t['treeSelector.label.selectAll']}</Tag>
                </div>
            </>
        }else{
            return null;
        }
    }

    return (
        <TreeSelect className={'disable-select'} size={"small"}
                    onChange={onChange}
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
                    value={value}
                    treeCheckStrictly={false}
                    allowClear={true}
                    showSearch={true}
                    treeData={treeData} />
    );
}