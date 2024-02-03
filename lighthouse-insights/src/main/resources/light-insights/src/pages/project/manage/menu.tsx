import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {Button, Grid} from "@arco-design/web-react";
const { Row, Col } = Grid;
import { Menu, Slider } from '@arco-design/web-react';
import {IconApps, IconBug, IconBulb, IconFile, IconFolder, IconTag, IconTags} from '@arco-design/web-react/icon';
const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;
import {TreeNode} from "@/types/insights-web";
import {getIcon} from "@/pages/common/desc/base";

export default function ProjectManageMenu({structure,callback}:{structure:TreeNode,callback:(id:number) => Promise<void>}) {

    const renderMenuItems = (items) => {
        const types = ['project','group'];
        return items?.filter(x => types.includes(x.type)).map((item) => {
            if (Array.isArray(item.children) && item.children.length > 0 && item.children.filter(x => types.includes(x.type))?.length > 0) {
                return (
                    <Menu.SubMenu key={item.key}
                                  title={
                        <span style={{display:"inline-flex",alignItems:"center"}}>{getIcon(item.type,'8px')}{item.label}</span>
                    }>
                        {renderMenuItems(item.children)}
                    </Menu.SubMenu>
                );
            }
            return <Menu.Item key={item.key}
            ><span style={{display:"inline-flex",alignItems:"center"}}>{getIcon(item.type,'8px')}{item.label}</span></Menu.Item>;
        });
    }

    return (
        <>
            <Menu
                autoOpen={true} style={{minHeight:'450px',userSelect:"none",overflow: "auto"}}
                onClickMenuItem = {(key, event, keyPath) => {
                    console.log("key is:" + key);
                    const keyArr = key.split("_");
                    if(keyArr[0] == 'group'){
                        callback(Number(keyArr[1]));
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