import React, {useContext, useEffect, useRef, useState} from 'react';
import {
    Button,
    Card,
    Grid,
    Modal,
    Notification,
    Popconfirm,
    Space,
    Spin,
    Tabs,
    Tag,
    Typography
} from '@arco-design/web-react';
import styles from "./style/index.module.less";
import StructurePanel from "@/pages/metricset/structure/structure";
import {MetricSetPreviewContext} from "@/pages/metricset/preview";
import {requestResetStructure, requestUpdateStructure} from "@/api/metricset";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import MetricSetRepositoryModal from "@/pages/metricset/structure/MetricRepositoryModal";
import {TreeNode} from "@/types/insights-web";

const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;

import { RiPlayListAddLine } from "react-icons/ri";
import {IconCheckCircleFill} from "@arco-design/web-react/icon";
import { VscSync } from "react-icons/vsc";
import { FiRefreshCcw } from "react-icons/fi";
import md5 from 'md5';
import {areJsonObjectsEqual, getRandomString} from "@/utils/util";

export const MetricSetStructureContext = React.createContext(null);

export default function MetricSetStructure() {

    function deepCopyObject(originalObject) {
        if (typeof originalObject !== 'object' || originalObject === null) {
            return originalObject;
        }
        const copiedObject = Array.isArray(originalObject) ? [] : {};
        for (const key in originalObject) {
            if (originalObject.hasOwnProperty(key)) {
                copiedObject[key] = deepCopyObject(originalObject[key]);
            }
        }
        return copiedObject;
    }

    const t = useLocale(locale);
    const [loading,setLoading] = useState<boolean>(false);
    const {metricSetInfo, setMetricSetInfo } = useContext(MetricSetPreviewContext);
    const {activeKey, setActiveKey } = useContext(MetricSetPreviewContext);
    const {reloadTime, setReloadTime } = useContext(MetricSetPreviewContext);
    const [selectedStatId,setSelectedStatId] = useState<number>(null);
    const [showRepositoryModal,setShowRepositoryModal] = useState<boolean>(false);
    const structureRef = useRef(null);
    const [listNodes,setListNodes] = useState<TreeNode[]>([deepCopyObject(metricSetInfo?.structure)]);
    const [needSync,setNeedSync] = useState<boolean>(false);
    const [showDeleteConfirm,setShowDeleteConfirm] = useState(false);

    const handlerCallback = async (type,record) => {
        if(type == 'clickStatMenu'){
            setSelectedStatId(Number(record));
        }
    }

    useEffect(() => {
        setListNodes([deepCopyObject(metricSetInfo?.structure)])
    },[metricSetInfo?.structure])

    const handleShowRepositoryModal = () => {
        setShowRepositoryModal(true);
    }


    const handlerSubmit = async () => {
        setLoading(true);
        const treeData = structureRef.current.getData()[0];
        const updateParam = {
            id:metricSetInfo?.id,
            structure:treeData,
        }
        await requestUpdateStructure(updateParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['structure.operations.update.success']});
                setReloadTime(Date.now());
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
        }).finally(() => {
            setLoading(false);
        })
    }

    const handlerReset = async () => {
        setLoading(true);
        const id = metricSetInfo?.id;
        await requestResetStructure({id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['structure.operations.reset.success']});
                setReloadTime(Date.now());
                setLoading(false);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
                setLoading(false);
            }
        }).catch((error) => {
            console.log(error);
            setLoading(false);
        })
    }

    return (
        <MetricSetStructureContext.Provider value={{listNodes,setListNodes,needSync,setNeedSync}}>
        <Spin loading={loading} style={{display:'block'}} className={styles['ss']}>
            <Space size={16} direction="vertical" style={{ width: '100%'}}>
                <div className={styles.wrapper}>
                    <Space size={16} direction="vertical" className={styles.left}>
                        <Card>
                            {listNodes && <StructurePanel ref={structureRef}  menuCallback={handlerCallback}/>}
                        </Card>
                        <Card>
                            <Grid.Row justify={"end"}>
                                <Grid.Col span={4}>
                                    <Space className={styles.right} size={10} direction="horizontal">
                                        <Button size={"mini"} type="secondary" icon={<RiPlayListAddLine/>} onClick={handleShowRepositoryModal}>{t['repositoryModal.button.label.repository']}</Button>
                                    </Space>
                                </Grid.Col>
                                <Grid.Col span={20} style={{textAlign:'right' }}>
                                    <Space size={18} direction="horizontal">
                                        {needSync && <Tag size={"small"}>[{t['structure.label.pendSync']}]</Tag>}
                                        <Popconfirm
                                                    focusLock
                                                    position={"tr"}
                                                    title='Confirm'
                                                    content={t['repositoryModal.button.label.submit.confirm']}
                                                    onOk={handlerSubmit}
                                        >
                                            <Button size={"small"} type="primary">{t['repositoryModal.button.label.submit']}</Button>
                                        </Popconfirm>
                                        <Popconfirm
                                            focusLock
                                            position={"tr"}
                                            title='Confirm'
                                            content={t['repositoryModal.button.label.reset.confirm']}
                                            okText={t['repositoryModal.button.label.reset']}
                                            okButtonProps={{ status:"danger" }}
                                            onOk={handlerReset}
                                        >
                                            <Button size={"small"} type={"primary"} status={"danger"}>{t['repositoryModal.button.label.reset']}</Button>
                                        </Popconfirm>
                                    </Space>
                                </Grid.Col>
                            </Grid.Row>
                        </Card>
                    </Space>
                    {showRepositoryModal && <MetricSetRepositoryModal id={metricSetInfo?.id} onClose={() => setShowRepositoryModal(false)} />}
                </div>
            </Space>
        </Spin>
        </MetricSetStructureContext.Provider>
    );
}