import React, { useState, useEffect } from 'react';
import {
    Button,
    Card,
    Divider,
    Grid,
    Link,
    Menu,
    Modal,
    Pagination,
    Space,
    Spin, Tabs,
    Trigger,
    Typography
} from '@arco-design/web-react';
import {
    IconMessage,
    IconClose,
    IconBug,
    IconBulb,
    IconUser,
    IconArrowRight,
    IconCalendar, IconClockCircle, IconPlus
} from '@arco-design/web-react/icon';
const MenuItem = Menu.Item;
import { RiNavigationFill } from "react-icons/ri";
import { TbNavigationPlus } from "react-icons/tb";
import { MdOutlineNavigation } from "react-icons/md";
import {useSelector} from "react-redux";
import {MetricSet} from "@/types/insights-web";
import Overview from "@/pages/dashboard/workplace/overview";
import MetricSetCardBox from "@/pages/metricset/list/MetricSetCardBox";
import styles from "./style/index.module.less";
import {Avatar} from "@arco-design/web-react/lib";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import { PiSortAscending } from "react-icons/pi";
import {getMetricPreviewHistory} from "@/pages/metricset/preview/history";
import cs from "classnames";


export default function MetricNavModal ({onClose}){

    const t = useLocale(locale);
    const staredMetricInfo = useSelector((state: {staredMetricInfo:Array<MetricSet>}) => state.staredMetricInfo);
    const [listData,setListData] = useState<Array<MetricSet>>([]);
    const pageSize = 16;

    const handleChangePage = (v) => {
        const startRow = (v - 1) * pageSize;
        const endRow = Math.min(v * pageSize - 1, staredMetricInfo.length - 1);
        setListData(staredMetricInfo.slice(startRow,endRow + 1));
    }

    const getPreviewHistory = () => {
        return getMetricPreviewHistory();
    }

    useEffect(() => {
        handleChangePage(1);
    },[staredMetricInfo])

    return (
        <Modal
            title={t['MetricNavModal.modal.title']}
            visible={true}
            onCancel={onClose}
            alignCenter={false}
            cancelText={t['basic.form.button.close']}
            footer={(cancelBtn,okBtn) => {
                return cancelBtn;
            }}
            style={{ width:'1500px',maxWidth:'90%'
                ,top:'30px',maxHeight:'95%',overflow:'auto',
            }}>
            <Space size={8} direction="vertical" style={{width:'100%'}}>
                <Row gutter={12}>
                    {listData.map((item, index) => (
                        <Col span={6} key={index}>
                            <MetricSetCardBox size={'small'} key={index} item={item}/>
                        </Col>
                    ))}
                </Row>
            </Space>
            <Space style={{justifyContent: 'center', padding:'0px',marginTop:'0px',width: '100%'}}>
                <Pagination
                    hideOnSinglePage={true}
                    total={staredMetricInfo.length} current={1} pageSize={pageSize} onChange={handleChangePage} />
            </Space>
        </Modal>
    );
}