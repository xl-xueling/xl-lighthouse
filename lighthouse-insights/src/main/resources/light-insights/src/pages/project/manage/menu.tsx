import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Button, Grid, Input, Skeleton, Space, Tree, Typography} from "@arco-design/web-react";
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
import {ArcoTreeNode, BackUpTreeNode, TreeNode} from "@/types/insights-web";
import {RiAppsLine} from "react-icons/ri";
import {stringifyObj} from "@/utils/util";
import {getIcon} from "@/pages/common/desc/base";

export default function ProjectManageMenu({structure,callback}:{structure:TreeNode,callback:(id:number) => Promise<void>}) {

    const renderMenuItems = (items) => {
        const types = ['project','group'];
        return items?.filter(x => types.includes(x.type)).map((item) => {
            if (Array.isArray(item.children) && item.children.length > 0 && item.children.filter(x => types.includes(x.type))?.length > 0) {
                return (
                    <Menu.SubMenu key={item.key}
                                  title={
                        <span style={{display:"inline-flex",alignItems:"center"}}>{getIcon(item.type)}{item.label}</span>
                    }>
                        {renderMenuItems(item.children)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item key={item.key}
            ><span style={{display:"inline-flex",alignItems:"center"}}>{getIcon(item.type)}{item.label}</span></Menu.Item>;
        });
    }

    return (
        <>
            <Menu
                autoOpen={true} style={{minHeight:'450px',userSelect:"none",overflow: "auto"}}
                onClickMenuItem = {(key, event, keyPath) => {
                    if(keyPath.length == 2){
                        callback(Number(key));
                    }
                }}
            >
                {
                    renderMenuItems(structure == null?[]:[structure])
                }
            </Menu>
        </>
    );
}