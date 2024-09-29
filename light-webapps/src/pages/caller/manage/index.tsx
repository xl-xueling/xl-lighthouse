import React, {useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import CallerManagePanel from "@/pages/caller/manage/CallerMangePanel";

export default function CallerManage(){

    const {id} = useParams();

    return (
        <CallerManagePanel id={id} />
    )
}