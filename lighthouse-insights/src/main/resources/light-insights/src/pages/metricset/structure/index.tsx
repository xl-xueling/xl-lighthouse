import React, {useContext, useRef, useState} from 'react';
import {Button, Card, Grid, Notification, Space, Spin, Tabs, Typography} from '@arco-design/web-react';
import styles from "./style/index.module.less";
import StructurePanel from "@/pages/metricset/structure/structure";
import {MetricSetPreviewContext} from "@/pages/metricset/preview";
import {MdOutlineNewLabel} from "react-icons/md";
import {requestResetStructure, requestUpdateStructure} from "@/api/metricset";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import MetricSetPendAddModal from "@/pages/metricset/structure/PendAddModal";
import {TreeNode} from "@/types/insights-web";

const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
export default function MetricSetStructure() {

    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const { metricSetInfo, setMetricSetInfo } = useContext(MetricSetPreviewContext);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);
    const [showPendAddModal,setShowPendAddModal] = useState<boolean>(false);
    const structureRef = useRef(null);
    const [listNodes,setListNodes] = useState<TreeNode[]>([metricSetInfo?.structure]);

    const handlerCallback = async (type,record) => {
        if(type == 'clickStatMenu'){
            setSelectedStatId(Number(record));
        }
    }

    const handleShowPendAddModal = () => {
        setShowPendAddModal(true);
    }

    const handleAddNode = (treeNode) => {
        const children = listNodes[0].children;
        listNodes[0].children = [...children, treeNode];
        setListNodes(listNodes);
    }

    const handlerSubmit = async () => {
        const treeData = structureRef.current.getData()[0];
        const updateParam = {
            id:metricSetInfo?.id,
            structure:treeData,
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
                            {listNodes && <StructurePanel ref={structureRef} structure={listNodes} menuCallback={handlerCallback}/>}
                        </Card>
                        <Card>
                            <Grid.Row justify="end">
                                <Grid.Col span={18}>
                                    <Space className={styles.right} size={16} direction="horizontal">
                                        <Button size={"mini"} type="secondary" icon={<MdOutlineNewLabel/>} onClick={handleShowPendAddModal}>待添加(16)</Button>
                                    </Space>
                                </Grid.Col>
                                <Grid.Col span={6}>
                                    <Space className={styles.right} size={16} direction="horizontal">
                                        <Button size={"small"} type={"primary"} status={"danger"}>重置</Button>
                                        <Button size={"small"} type="primary" onClick={handlerSubmit}>确认</Button>
                                    </Space>
                                </Grid.Col>
                            </Grid.Row>
                        </Card>
                    </Space>
                    {showPendAddModal && <MetricSetPendAddModal id={metricSetInfo?.id} callback={handleAddNode} onClose={() => setShowPendAddModal(false)} />}
                </div>
            </Space>
        </Spin>
    );
}