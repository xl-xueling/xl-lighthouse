import React, {useEffect, useRef, useState} from 'react';
import {Department, TreeNode, User} from "@/types/insights-web";
import {Descriptions, Popover} from "@arco-design/web-react";
import {Props} from "@/pages/department/common/depart";


export default function StatLabel({statInfo}) {

    const getLabel = () => {
        return (
            <Popover trigger={"click"} content={statInfo?.project?.title + ' > '+ statInfo?.title}>
                <span>{statInfo?.title}</span>
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