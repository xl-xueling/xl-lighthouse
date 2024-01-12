import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import { Line } from '@ant-design/plots';
import { Chart, Line as Line2, Point, Tooltip,getTheme } from "bizcharts";
import { LineAdvance} from 'bizcharts';
import {IconEdit, IconList, IconPublic, IconPushpin} from "@arco-design/web-react/icon";
import {requestQueryById} from "@/api/stat";
import {Notification} from "@arco-design/web-react";


export default function ChartPanel({searchForm={},statInfo}) {

    const [loading,setLoading] = useState<boolean>(false);
    const [chartData,setChartData] = useState(null);

    const data = [
        { year: '1991', value: 3 },
        { year: '1992', value: 4 },
        { year: '1993', value: 3.5 },
        { year: '1994', value: 5 },
        { year: '1995', value: 4.9 },
        { year: '1996', value: 6 },
        { year: '1997', value: 7 },
        { year: '1998', value: 9 },
        { year: '1999', value: 13 },
    ];

    const config = {
        data,
        xField: 'year',
        yField: 'value',
        point: {
            size: 5,
        },

        theme:'light',
    };

    const fetchData = async () => {
        setLoading(true);
        await requestQueryById({id:id}).then((response) => {
            const {code, data ,message} = response;
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
        console.log("------load chart,searchForm is:" + JSON.stringify(searchForm));
        fetchData().then();
    },[JSON.stringify(searchForm)])

    return (<>
        <Line style={{ height:'300px'}} {...config} />
    </>);
}