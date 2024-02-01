import React, { useState, useEffect } from 'react';
import {Button, Card, Menu, Spin, Trigger, Typography} from '@arco-design/web-react';
import {IconMessage, IconClose, IconBug, IconBulb, IconUser} from '@arco-design/web-react/icon';
const MenuItem = Menu.Item;
import styles from './style/index.module.less';
import { RiNavigationFill } from "react-icons/ri";
import { TbNavigationPlus } from "react-icons/tb";
import { MdOutlineNavigation } from "react-icons/md";


export default function PopMenuBox (){
    const [isVisible, setIsVisible] = useState(true);

    useEffect(() => {
        // 监听页面滚动事件，当滚动到一定位置时显示按钮
        const handleScroll = () => {
            const scrollY = window.scrollY || document.documentElement.scrollTop;

            if (scrollY > 100) {
                setIsVisible(true);
            } else {
                setIsVisible(false);
            }
        };
        window.addEventListener('scroll', handleScroll);
        // 清除监听器，防止内存泄漏
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    const [popupVisibleOne, setPopupVisibleOne] = useState(false);

    const renderMenu = () => {
        return (<Menu
            style={{ marginBottom: -4 }}
            mode='popButton'
            tooltipProps={{ position: 'left' }}
            hasCollapseButton
        >
            <MenuItem key='1'>
                <IconBug />
                Bugs
            </MenuItem>
            <MenuItem key='2'>
                <IconBulb />
                Ideas
            </MenuItem>
        </Menu>);
    }

    return (
        <div>
            <div className={styles['floating-button']}>
                <Trigger
                    popup={renderMenu}
                    trigger={['click', 'hover']}
                    clickToClose
                    position='top'
                    onVisibleChange={(v) => setPopupVisibleOne(v)}
                >
                    <div className={`button-trigger ${popupVisibleOne ? 'button-trigger-active' : ''}`}>
                        {popupVisibleOne ?
                            <Button size={"default"} type={"primary"} shape={"round"} icon={<IconClose/>}/>
                            :
                            <Button size={"default"} type={"primary"}  shape={"round"} icon={<MdOutlineNavigation/>}/>
                        }
                    </div>
                </Trigger>
            </div>
        </div>
    );
}
