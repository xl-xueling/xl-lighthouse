import React, {useCallback, useEffect, useLayoutEffect, useMemo, useRef, useState} from 'react';
import {Input, Menu} from "@arco-design/web-react";
import {IconBug, IconBulb} from "@arco-design/web-react/icon";
const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;

export default function MetricTree() {

    return (
        <>
            <Menu
                style={{overflow: 'auto' }}
            >
                <Input.Search allowClear placeholder='Search Items'/>
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
                <SubMenu
                    key='3'
                    title={
                        <>
                            <IconBug /> Navigation 2
                        </>
                    }
                >
                    <MenuItem key='3_0'>Menu 1</MenuItem>
                    <MenuItem key='3_1'>Menu 2</MenuItem>
                    <MenuItem key='3_2'>Menu 3</MenuItem>
                </SubMenu>
                <SubMenu
                    key='4'
                    title={
                        <>
                            <IconBulb /> Navigation 3
                        </>
                    }
                >
                    <MenuItem key='4_0'>Menu 1</MenuItem>
                    <MenuItem key='4_1'>Menu 2</MenuItem>
                    <MenuItem key='4_2'>Menu 3</MenuItem>
                </SubMenu>
                <SubMenu
                    key='5'
                    title={
                        <>
                            <IconBug /> Navigation 2
                        </>
                    }
                >
                    <MenuItem key='5_0'>Menu 1</MenuItem>
                    <MenuItem key='5_1'>Menu 2</MenuItem>
                    <MenuItem key='5_2'>Menu 3</MenuItem>
                </SubMenu>
                <SubMenu
                    key='6'
                    title={
                        <>
                            <IconBulb /> Navigation 3
                        </>
                    }
                >
                    <MenuItem key='6_0'>Menu 1</MenuItem>
                    <MenuItem key='6_1'>Menu 2</MenuItem>
                    <MenuItem key='6_2'>Menu 3</MenuItem>
                </SubMenu>
            </Menu>
        </>
    );
}