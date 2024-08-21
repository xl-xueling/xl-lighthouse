import React, {useEffect, useRef, useState} from 'react';
import {Department, TreeNode, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import {Props} from "@/pages/department/common/depart";


export default function ViewLabel({viewInfo}) {

    const getLabel = () => {
        return (
            <Popover trigger={"click"} content={viewInfo?.title}>
                <span>{viewInfo?.title}</span>
            </Popover>
        );
    }

    return (
        <>
            {
                getLabel()
            }
        </>
    )
}