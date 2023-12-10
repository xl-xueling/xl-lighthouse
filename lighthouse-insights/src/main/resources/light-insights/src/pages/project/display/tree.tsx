import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Grid, Space} from "@arco-design/web-react";
import Overview from "@/pages/dashboard/workplace/overview";
import PopularContents from "@/pages/dashboard/workplace/popular-contents";
import ContentPercentage from "@/pages/dashboard/workplace/content-percentage";
import Shortcuts from "@/pages/dashboard/workplace/shortcuts";
import Carousel from "@/pages/dashboard/workplace/carousel";
import Announcement from "@/pages/dashboard/workplace/announcement";
import Docs from "@/pages/dashboard/workplace/docs";
const { Row, Col } = Grid;
import { Menu, Slider } from '@arco-design/web-react';
import { IconApps, IconBug, IconBulb } from '@arco-design/web-react/icon';
const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;

export default function ProjectTree() {
    return (
        <Menu
            style={{height: 'calc(100% - 28px)' }}
            defaultOpenKeys={['0']}
            defaultSelectedKeys={['0_1']}
        >
            <MenuItem key='0_0'>Menu 1</MenuItem>
            <MenuItem key='0_1'>Menu 2</MenuItem>
            <MenuItem key='0_2' disabled>
                Menu 3
            </MenuItem>
            <SubMenu
                key='1'
                title={
                    <>
                        <IconBug /> Navigation 2
                    </>
                }
            >
                <MenuItem key='1_0'>Menu 1</MenuItem>
                <MenuItem key='1_1'>Menu 2</MenuItem>
                <MenuItem key='1_2'>Menu 3</MenuItem>
            </SubMenu>
            <SubMenu
                key='2'
                title={
                    <>
                        <IconBulb /> Navigation 3
                    </>
                }
            >
                <MenuItem key='2_0'>Menu 1</MenuItem>
                <MenuItem key='2_1'>Menu 2</MenuItem>
                <MenuItem key='2_2'>Menu 3</MenuItem>
            </SubMenu>
        </Menu>
    );
}