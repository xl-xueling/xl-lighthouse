import React, {useContext, useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {
    Card,
    Typography,
    Grid,
    Space,
    Tabs,
    Spin,
    Button
} from '@arco-design/web-react';
import styles from "./style/index.module.less";
import StatPreviewPanel from "@/pages/stat/display/preview";
import StructurePanel from "@/pages/metricset/structure/structure";
import {MetricSetPreviewContext} from "@/pages/metricset/preview";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import { MdOutlineNewLabel } from "react-icons/md";
import { RiDeleteBin3Line } from "react-icons/ri";
export default function MetricSetStructure() {

    const [loading,setLoading] = useState<boolean>(false);
    const { metricSetInfo, setMetricSetInfo } = useContext(MetricSetPreviewContext);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);
    const handlerCallback = async (type,record) => {
        if(type == 'clickStatMenu'){
            setSelectedStatId(Number(record));
        }
    }

    return (
        <Spin loading={loading} style={{display:'block'}} className={styles['ss']}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <div className={styles.wrapper}>
                    <Space size={16} direction="vertical" className={styles.left}>
                        <Card>
                            <StructurePanel structure={metricSetInfo?.structure} menuCallback={handlerCallback}/>
                        </Card>
                        <Card>
                            <Grid.Row justify="end">
                                <Grid.Col span={16}>
                                    <Space className={styles.right} size={16} direction="horizontal">
                                        <Button size={"mini"} type="secondary" icon={<MdOutlineNewLabel/>}>待新增(16)</Button>
                                        <Button size={"mini"} type={"secondary"} icon={<RiDeleteBin3Line/>}>已删除(30)</Button>
                                    </Space>
                                </Grid.Col>
                                <Grid.Col span={8}>
                                    <Space className={styles.right} size={16} direction="horizontal">
                                        <Button size={"small"} type={"primary"} status={"danger"}>重置</Button>
                                        <Button size={"small"} type="primary">确认</Button>
                                    </Space>
                                </Grid.Col>
                            </Grid.Row>
                        </Card>
                    </Space>
                    <Space className={styles.right} size={16} direction="vertical">
                        {selectedStatId && <StatPreviewPanel size={'small'} id={selectedStatId}/>}
                    </Space>
                </div>
            </Space>
        </Spin>
    );
}