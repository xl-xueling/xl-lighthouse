import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Button, Grid, Input, Skeleton, Space, Tree} from "@arco-design/web-react";
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
import {ArcoTreeNode} from "@/types/insights-web";
import {RiAppsLine} from "react-icons/ri";

export default function ProjectManageMenu({structure,callback}:{structure:Array<ArcoTreeNode>,callback:(id: string) => Promise<void>}) {

    const renderMenuItems = (items) => {
        const levels = [1,2];
        return items?.filter(x => levels.includes(x.level)).map((item) => {
            if (Array.isArray(item.children) && item.children.length > 0 && item.children.filter(x => levels.includes(x.level))?.length > 0) {
                return (
                    <Menu.SubMenu key={item.key} title={
                        <span style={{display:"inline-flex",alignItems:"center"}}><RiAppsLine style={{marginRight:'10px'}}/>{item.title}</span>
                    }>
                        {renderMenuItems(item.children)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item key={item.key}><CiViewTable style={{marginRight:'10px'}}/>{item.title}</Menu.Item>;
        });
    }


    return (
        <>
            <Menu
                style={{height: 'calc(100% - 50px)' ,minHeight:'400px',userSelect:"none"}}
            >
                {
                    structure ?
                        renderMenuItems([structure])
                        :
                        <Skeleton
                            text={{
                                rows:5,
                                width: ['100%','100%','100%','100%','60%'],
                            }}
                            animation
                        />
                }
            </Menu>
        </>
    );
}