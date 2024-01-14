import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Grid, Input, Skeleton, Space, Spin, Tree} from "@arco-design/web-react";
import Overview from "@/pages/dashboard/workplace/overview";
import PopularContents from "@/pages/dashboard/workplace/popular-contents";
import ContentPercentage from "@/pages/dashboard/workplace/content-percentage";
import Shortcuts from "@/pages/dashboard/workplace/shortcuts";
import Carousel from "@/pages/dashboard/workplace/carousel";
import Announcement from "@/pages/dashboard/workplace/announcement";
import Docs from "@/pages/dashboard/workplace/docs";
const { Row, Col } = Grid;
import { Menu, Slider } from '@arco-design/web-react';
import {IconApps, IconBug, IconBulb, IconFile, IconFolder, IconTag, IconTags} from '@arco-design/web-react/icon';
const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;
import { CiViewTable } from "react-icons/ci";
import {ArcoTreeNode, BackUpTreeNode, Project, TreeNode} from "@/types/insights-web";

export default function ProjectMenu({projectInfo,callback}:{projectInfo:Project,callback:(id: string) => Promise<void>}) {

    const renderMenuItems = (items) =>
        items?.map((item) => {
            if (Array.isArray(item.children) && item.children.length > 0) {
                return (
                    <Menu.SubMenu key={item.type + "_" + item.value} title={
                        <span style={{display:"inline-flex",alignItems:"center"}}><CiViewTable style={{marginRight:'10px'}}/>{item.label}</span>
                    }>
                        {renderMenuItems(item.children)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item key={item.type + "_" + item.value}><IconTag/>{item.label}</Menu.Item>;
        });

    return (
        <>
        <Menu
            className={'disable-select'}
            style={{height: 'calc(100% - 28px)' ,minHeight:'500px',overflow: "auto"}}
            onClickMenuItem={callback}>
        {
            renderMenuItems(projectInfo?.structure[0].children)
        }
        </Menu>
        </>
    );
}