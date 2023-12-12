import React, {useEffect, useState} from 'react';
import {Button, Card, Grid, Space, Typography} from "@arco-design/web-react";
import {IconTag} from "@arco-design/web-react/icon";
import SearchForm from "@/pages/stat/display/search_form";
import ChartPanel from "@/pages/stat/display/chart_panel";
import BasicInfo from "@/pages/stat/display/basic";
import {Project, Stat} from "@/types/insights-web";
import {requestQueryByIds} from "@/api/stat";
const { Row, Col } = Grid;

export default function StatDisplayMode1({statId = 0}) {

    const fetchStatInfo:Promise<Stat> = new Promise<Stat>((resolve,reject) => {
        const proc = async () => {
            const result = await requestQueryByIds({"ids":[statId]});
            resolve(result.data[statId]);
        }
        proc().then();
    })

    const fetchData = async (): Promise<void> => {
        const result = await Promise.all([fetchStatInfo]);
    }

    useEffect(() => {
        fetchData().then();
     }
    ,[statId])

    return (
        <>
            <Card>
                <Row style={{marginBottom:'15px'}}>
                    <Col span={12}>
                        <Button icon={<IconTag/>} shape={"circle"} size={"mini"} style={{marginRight:'10px'}}/>
                        <Typography.Text style={{fontSize:'14px'}}>
                            {'每分钟uv数据统计'}
                        </Typography.Text>
                    </Col>
                </Row>
                <Row>
                    <SearchForm statId={statId}/>
                </Row>

                <ChartPanel statId={statId}/>
            </Card>
            <Card>
                <Row style={{marginBottom:'15px'}}>
                    <Typography.Text style={{fontSize:'14px'}}>
                        {'Metric Information'}
                    </Typography.Text>
                </Row>
                <BasicInfo />
            </Card>
        </>
    );
}