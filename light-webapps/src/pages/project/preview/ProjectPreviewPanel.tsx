import React, {useEffect, useState} from 'react';
import styles from "./style/index.module.less";
import {
    Card,
    Grid,
    Space,
    Spin,
    Notification,
    Breadcrumb, Typography
} from "@arco-design/web-react";
import ProjectMenu from "@/pages/project/preview/menu";
import {useRouter} from "next/router";
const {Row, Col} = Grid;
import PreviewHeader from "@/pages/project/preview/head";
import {Project} from "@/types/insights-web";
import {requestQueryById} from "@/api/project";
import {IconHome} from "@arco-design/web-react/icon";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import StatPreviewPanel from "@/pages/stat/preview/StatPreviewPanel";


export default function ProjectPreviewPanel({id}) {

    const router = useRouter();
    const { Text } = Typography;
    const t = useLocale(locale);
    const [loading, setLoading] = useState<boolean>(true);
    const [projectInfo, setProjectInfo] = useState<Project>(null);
    const [selectedStatId, setSelectedStatId] = useState<number>(null);

    const handlerCallback = async (type, record) => {
        if (type == 'clickStatMenu') {
            setSelectedStatId(Number(record));
        }
    }

    const fetchProjectInfo = async (): Promise<void> => {
        setLoading(true);
        await requestQueryById({id}).then((response) => {
            const {code, data, message} = response;
            if (code == '0') {
                setProjectInfo(data);
            } else {
                Notification.warning({style: {width: 420}, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }


    useEffect(() => {
        fetchProjectInfo().then();
    }, [])

    return (
        <>
            <Row>
                <Col span={16}>
                    <Breadcrumb style={{fontSize: 12, marginBottom: '10px'}}>
                        <Breadcrumb.Item>
                            <IconHome/>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item style={{fontWeight: 20}}>{t['projectPreview.breadcrumb']}</Breadcrumb.Item>
                    </Breadcrumb>
                </Col>
                <Col span={8} style={{textAlign:'right',fontSize:'13px',color:'#43454a'}}>
                    <Text style={{cursor:'pointer'}} onClick={() => router.back()}>[{t['basic.route.back']}]</Text>
                </Col>
            </Row>
            <Spin loading={loading} style={{display: 'block'}}>
                <Space size={16} direction="vertical" style={{width: '100%'}}>
                    <Card>
                        <PreviewHeader projectInfo={projectInfo}/>
                    </Card>
                    <div className={styles.wrapper}>
                        <Space size={16} direction="vertical" className={styles.left}>
                            <Row>
                                <ProjectMenu projectInfo={projectInfo} callback={handlerCallback}/>
                            </Row>
                        </Space>
                        <Space className={styles.right} size={16} direction="vertical">
                            {selectedStatId && <StatPreviewPanel id={selectedStatId} size={'small'}/>}
                        </Space>
                    </div>
                </Space>
            </Spin>
        </>
    );
}