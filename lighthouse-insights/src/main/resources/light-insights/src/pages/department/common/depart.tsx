import React, {useEffect, useRef, useState} from 'react';
import {Department, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import {getCascadeDepartment, getDepartment} from "@/pages/department/common";
export interface Props {
    department?:Department;
    departmentId?:number;
}

export default function DepartmentLabel(props:Props) {
    const { department,departmentId } = props;
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const getDepartmentInfo = (props) => {
        let currentDepartment:Department = null;
        const { department,departmentId } = props;
        if(departmentId){
            currentDepartment = getDepartment(departmentId,allDepartInfo);
        }else if(department){
            currentDepartment = department;
        }
        if(currentDepartment){
            const cascadeDepartments:Department[] = getCascadeDepartment(currentDepartment,allDepartInfo);
            return (
                <Popover trigger={"click"} content={cascadeDepartments.map(z => z?.name).join(" > ")}>
                    <span>{currentDepartment?.name}</span>
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