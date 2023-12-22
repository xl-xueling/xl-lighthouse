import React, {useEffect, useRef, useState} from 'react';
import {Department, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";

export interface Props {
    department: Department;
}

export default function DepartmentLabel(props:Props) {
    const { department } = props;
    return (
        <span>{department.name}</span>
    );
}