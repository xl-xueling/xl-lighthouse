import React, { useState } from 'react';
import {Button, Menu, Trigger} from '@arco-design/web-react';
const MenuItem = Menu.Item;
import styles from './style/index.module.less';
import { MdOutlineNavigation } from "react-icons/md";
import {getIcon} from "@/desc/base";
import ProjectNavModal from "@/pages/project/common/ProjectNavModal";
import MetricNavModal from "@/pages/metricset/common/MetricNavModal";

export default function PopMenuBox (){
    const [popupVisibleOne, setPopupVisibleOne] = useState(false);
    const [showMetricNavModal,setShowMetricNavModal] = useState(false);
    const [showProjectNavModal,setShowProjectNavModal] = useState(false);

    const renderMenu = () => {
        return (<Menu
            tooltipProps={{ popupVisible:false}}
            mode={"popButton"}>
            <MenuItem key='1' onClick={() => setShowMetricNavModal(true)}>
                {getIcon('metric')}
            </MenuItem>
            <MenuItem key='2'onClick={() => setShowProjectNavModal(true)}>
                {getIcon('project')}
            </MenuItem>
        </Menu>);
    }

    return (
        <div style={{zIndex:'9999'}}>
            <div className={styles['floating-button']}>
                <Trigger
                    popup={renderMenu}
                    trigger={['click', 'hover']}
                    position='top'
                    clickToClose={false}
                    onVisibleChange={(v) => setPopupVisibleOne(v)}
                >
                    <div className={`button-trigger ${popupVisibleOne ? 'button-trigger-active' : ''}`}>
                        <Button size={"default"} type={"primary"}  shape={"round"} icon={<MdOutlineNavigation/>}/>
                    </div>
                </Trigger>
            </div>
            {showMetricNavModal && <MetricNavModal onClose={() => setShowMetricNavModal(false)}/>}
            {showProjectNavModal && <ProjectNavModal onClose={() => setShowProjectNavModal(false)}/>}
        </div>
    );
}
