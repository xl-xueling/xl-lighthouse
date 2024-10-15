import React, {useContext, useEffect, useState} from "react";
import {CallerManageContext} from "@/pages/common/context";
import {Card,Notification} from "@arco-design/web-react";
import {requestQueryByIds} from "@/api/stat";

export default function CallerPreviewPanel({}){

    const {callerInfo} = useContext(CallerManageContext);

    const [statsInfo,setStatsInfo] = useState<Map<string,any>>(new Map());

    const [loading,setLoading] = useState<boolean>(false);

    const [statsData,setStatsData] = useState(new Map());

    const statIds = [1031,1032,1033,1034,1035,1036];

    const fetchStatInfo = async () => {
        setLoading(true);
        await requestQueryByIds({ids:statIds}).then((response) => {
            const {code, data ,message} = response;
            console.log("data is:" + JSON.stringify(data));
            if(code == '0'){

            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }


    useEffect(() => {
        fetchStatInfo().then();
    },[])

    return (
        <Card>

        </Card>
    )
}