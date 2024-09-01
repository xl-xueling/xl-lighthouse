import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import styles from "./style/index.module.less";
import {Empty, Grid, Input, Skeleton, Space, Spin, Tree} from "@arco-design/web-react";
const { Row, Col } = Grid;
import { Menu, Slider } from '@arco-design/web-react';
import {IconApps, IconBug, IconBulb, IconFile, IconFolder, IconTag, IconTags} from '@arco-design/web-react/icon';
const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;
import { CiViewTable } from "react-icons/ci";
import {Project, TreeNode} from "@/types/insights-web";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import {getTreeResourceIcon} from "@/pages/common/desc/base";

export default function ProjectMenu({projectInfo,callback}:{projectInfo:Project,callback:(type: string,id:number) => Promise<void>}) {

    const t = useLocale(locale);

    const renderMenuItems = (items,level) =>
        items?.map((item) => {
            if (Array.isArray(item.children) && item.children.length > 0) {
                return (
                    <Menu.SubMenu key={item.type + "_" + item.value} title={
                        <span>{getTreeResourceIcon(item.type,level)}{item.label}</span>
                    }>
                        {renderMenuItems(item.children,level + 1)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item key={item.type + "_" + item.value}>{getTreeResourceIcon(item.type,level)}{item.label}</Menu.Item>;
        });



    return (
        <>
        <Menu
            className={'disable-select'}
            style={{height: 'calc(100% - 28px)' ,minHeight:'500px',overflow: "auto"}}
            onClickMenuItem = {(key, event, keyPath) => {
                const type = key.split("_")[0];
                const id = key.split("_")[1];
                if(type == 'stat'){
                    callback("clickStatMenu",Number(id));
                }
            }}
        >
        {
            (projectInfo && projectInfo.structure.children) ? renderMenuItems(projectInfo.structure.children,1)
                : <Empty style={{marginTop:'50px'}}/>
        }
        </Menu>
        </>
    );
}