import React, {useEffect, useMemo, useState} from 'react';
import Exception403 from "@/pages/exception/403";
import Exception404 from "@/pages/exception/404";
import Exception500 from "@/pages/exception/500";
import Exception100 from "@/pages/exception/100";

export default function ErrorPage({errorCode,fromExternalEmbedding = false,errorMessage = null}) {

    const getErrorPage = () => {
        if(errorCode == '100'){
            return <Exception100 fromExternalEmbedding={fromExternalEmbedding} errorMessage={errorMessage}/>
        }else if(errorCode == '401' || errorCode == '403'){
            return <Exception403 fromExternalEmbedding={fromExternalEmbedding} errorMessage={errorMessage}/>
        }else if(errorCode == '404'){
            return <Exception404 fromExternalEmbedding={fromExternalEmbedding} errorMessage={errorMessage}/>
        }else {
            return <Exception500 fromExternalEmbedding={fromExternalEmbedding} errorMessage={errorMessage}/>
        }
    }

    return (
        <div className={'disable-select'}>
            {getErrorPage()}
        </div>
    );
}