import React, {useEffect, useRef, useState} from 'react';
import {Department, TreeNode, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import {getFullPathNodes} from "@/pages/department/base";
export interface Props {
    departmentId?:number;
}

export default function DepartmentLabel(props:Props) {
    const { departmentId } = props;
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);

    const getDepartmentInfo = (props) => {
        const { departmentId } = props;
        const fullPathNodes:TreeNode[] = getFullPathNodes(departmentId,allDepartInfo);
        if(departmentId){
            return (
                <Popover trigger={"click"} content={fullPathNodes.map(z => z?.label).join(" > ")}>
                    <span>{fullPathNodes[fullPathNodes.length - 1]?.label}</span>
                </Popover>
            );
        }
    }

    return (
        <>
        {
            getDepartmentInfo(props)
        }
        </>
    );
}