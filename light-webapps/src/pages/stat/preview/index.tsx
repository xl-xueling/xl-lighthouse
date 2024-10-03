import React, {useEffect, useState} from 'react';
import {useParams,useHistory} from "react-router-dom";
import {Breadcrumb, Button, Card, Divider, Grid, Notification, Space, Spin, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {IconDashboard, IconHome, IconTag, IconTags} from "@arco-design/web-react/icon";
const {Row, Col} = Grid;
import StatPreviewPanel from "@/pages/stat/preview/preview";

export default function StatPreviewPage() {

    const {id} = useParams();
    const t = useLocale(locale);
    const history = useHistory();
    const { Text } = Typography;
    
    return (
        <>
            <Row>
                <Col span={16}>
                    <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                        <Breadcrumb.Item>
                            <IconHome/>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item style={{fontWeight: 20}}>{t['statDisplay.breadcrumbItem']}</Breadcrumb.Item>
                    </Breadcrumb>
                </Col>
                <Col span={8} style={{textAlign:'right',fontSize:'13px',color:'#43454a'}}>
                    <Text style={{cursor:'pointer'}} onClick={() => history.goBack()}>[{t['basic.route.back']}]</Text>
                </Col>
            </Row>
            <StatPreviewPanel id={id}/>
        </>
    );
}