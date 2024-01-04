import React, {useEffect, useMemo, useState} from 'react';
import Exception403 from "@/pages/exception/403";
import Exception404 from "@/pages/exception/404";
import Exception500 from "@/pages/exception/500";


export default function ErrorPage({errorCode}) {

    const getErrorPage = () => {
        if(errorCode == '403'){
            return <Exception403 />
        }else if(errorCode == '404'){
            return <Exception404 />
        }else {
            return <Exception500 />
        }
    }

    return (
        <div>
            {getErrorPage()}
        </div>
    );
}