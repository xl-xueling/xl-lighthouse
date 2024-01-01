import React, {useEffect, useState} from 'react';
import {Button, Card, Grid, Skeleton, Space, Spin, Typography} from "@arco-design/web-react";
import {IconTag} from "@arco-design/web-react/icon";
import SearchForm from "@/pages/stat/display/search_form";
import ChartPanel from "@/pages/stat/display/chart_panel";
import BasicInfo from "@/pages/stat/display/basic";
import {Project, Stat} from "@/types/insights-web";
import {requestQueryByIds} from "@/api/stat";
import {requestPrivilegeCheck} from "@/api/privilege";
import FilterPanel from "@/pages/stat/display/filter/filter_panel";
const { Row, Col } = Grid;

export default function StatDisplayMode1({statId = 0}) {

    const [statInfo,setStatInfo] = useState<Stat>(null);
    const [loading,setLoading] = useState<boolean>(true);

    const fetchStatInfo:Promise<Stat> = new Promise<Stat>((resolve,reject) => {
        const proc = async () => {
            const result = await requestQueryByIds({"ids":[statId]});
            resolve(result.data[statId]);
        }
        proc().then();
    })

    // const fetchPrivilegeInfo = async(ids) => {
    //     return new Promise<Record<number,PermissionsEnum[]>>((resolve,reject) => {
    //         requestPrivilegeCheck({type:"stat",ids:ids}).then((response) => {
    //             resolve(response.data);
    //         }).catch((error) => {
    //             reject(error);
    //         })
    //     })
    // }

    const fetchData = async (): Promise<void> => {
        setLoading(true);
        const result = await Promise.all([fetchStatInfo]);
        const statInfo = result[0];
        // Promise.all([fetchPrivilegeInfo([statId])])
        //     .then(([r1]) => {
        //         const combinedItem = { ...statInfo, ...{"permissions":r1[statInfo.id]}};
        //         console.log("combinedItem:" + JSON.stringify(combinedItem))
        //         setStatInfo(combinedItem);
        //         setLoading(false);
        //     }).catch((error) => {
        //     console.log(error);
        // })
    }

    useEffect(() => {
        fetchData().then();
     }
    ,[statId])

    return (

        <>
            <Card>
                {
                    statInfo?
                        <>
                            <Row style={{marginBottom:'15px'}}>
                                <Col span={12}>
                                    <Button icon={<IconTag/>} shape={"circle"} size={"mini"} style={{marginRight:'10px'}}/>
                                    <Typography.Text style={{fontSize:'14px'}}>
                                        {'每分钟uv数据统计'}
                                    </Typography.Text>
                                </Col>
                            </Row>
                            <Row>
                                <SearchForm statInfo={statInfo}/>
                            </Row>
                        </>
                        :
                        <Skeleton
                            text={{
                                rows:3,
                                width: ['100%','100%','60%'],
                            }}
                            animation
                        />
                }
                <ChartPanel statInfo={statInfo}/>
            </Card>
            <Card>
                {
                    statInfo?
                <>
                    <Row style={{marginBottom:'15px'}}>
                        <Typography.Text style={{fontSize:'14px'}}>
                            {'Metric Information'}
                        </Typography.Text>
                    </Row>
                    <BasicInfo statInfo={statInfo}/>
                </>
                :
                <Skeleton
                    text={{
                        rows:5,
                        width: ['100%'],
                    }}
                    animation
                />}
            </Card>
            {/*<FilterPanel />*/}
        </>
    );
}