import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";

export default function ProjectDisplay() {

    const { id } = useParams();

    useEffect(() => {
        console.log("project display..")
    },[])

    return (
        <div>
        </div>);
}