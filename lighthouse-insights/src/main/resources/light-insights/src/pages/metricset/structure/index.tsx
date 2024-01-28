import React, {useContext, useEffect, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {
    Card,
    Typography,
    Grid,
    Space,
    Tabs,
    Spin,
    Button, Notification
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
import {requestDeleteById} from "@/api/project";
import {requestResetStructure, requestUpdateStructure} from "@/api/metricset";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import MetricSetPendAddModal from "@/pages/metricset/structure/PendAddModal";
export default function MetricSetStructure() {

    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const { metricSetInfo, setMetricSetInfo } = useContext(MetricSetPreviewContext);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);
    const [showPendAddModal,setShowPendAddModal] = useState<boolean>(false);
    const structureRef = useRef(null);
    const handlerCallback = async (type,record) => {
        console.log("data is:" + JSON.stringify(structureRef.current.getData()));
        if(type == 'clickStatMenu'){
            setSelectedStatId(Number(record));
        }
    }

    const handleShowPendAddModal = () => {
        setShowPendAddModal(true);
    }

    const handlerSubmit = async () => {
        console.log("handler submit..")
        const updateParam = {
            id:metricSetInfo?.id,
            structure:structureRef.current.getData(),
        }
        await requestUpdateStructure(updateParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.delete.submit.success']});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerReset = async () => {
        console.log("handler reset..")
        const id = metricSetInfo?.id;
        await requestResetStructure({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['projectList.operations.delete.submit.success']});
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        })
    }

    return (
        <Spin loading={loading} style={{display:'block'}} className={styles['ss']}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <div className={styles.wrapper}>
                    <Space size={16} direction="vertical" className={styles.left}>
                        <Card>
                            <StructurePanel ref={structureRef} structure={metricSetInfo?.structure} menuCallback={handlerCallback}/>
                        </Card>
                        <Card>
                            <Grid.Row justify="end">
                                <Grid.Col span={16}>
                                    <Space className={styles.right} size={16} direction="horizontal">
                                        <Button size={"mini"} type="secondary" icon={<MdOutlineNewLabel/>} onClick={handleShowPendAddModal}>待添加(16)</Button>
                                    </Space>
                                </Grid.Col>
                                <Grid.Col span={8}>
                                    <Space className={styles.right} size={16} direction="horizontal">
                                        <Button size={"small"} type={"primary"} status={"danger"}>重置</Button>
                                        <Button size={"small"} type="primary" onClick={handlerSubmit}>确认</Button>
                                    </Space>
                                </Grid.Col>
                            </Grid.Row>
                        </Card>
                    </Space>
                    <Space className={styles.right} size={16} direction="vertical">
                        {selectedStatId && <StatPreviewPanel size={'small'} id={selectedStatId}/>}
                    </Space>
                    {showPendAddModal && <MetricSetPendAddModal id={metricSetInfo?.id} onClose={() => setShowPendAddModal(false)} />}
                </div>
            </Space>
        </Spin>
    );
}