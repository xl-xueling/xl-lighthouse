import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';
import {
    Breadcrumb,
    Card,
    Input,
    Table,
    Grid,
    Notification,
    TableColumnProps,
    Button,
    PaginationProps,
    Spin, Divider, Link
} from "@arco-design/web-react";
import {IconHome} from "@arco-design/web-react/icon";
const BreadcrumbItem = Breadcrumb.Item;
import locale from "./locale/index"
import CallerCreateModal from "@/pages/caller/create";
import {requestDeleteById, requestList} from "@/api/caller";
import {Caller} from "@/types/caller";
import {getColumns} from "./Constants";
import CallerUpdateModal from "@/pages/caller/update";
import useLocale from "@/utils/useLocale";
import KeepAlive, {useActivate, useUnactivate} from "react-activation";
const InputSearch = Input.Search;
import { useLocation, useHistory } from 'react-router-dom';
import CallerListPanel from "@/pages/caller/list/list";

export default function CallerListPage(){

    const t = useLocale(locale);

    return (
        <>
            <Breadcrumb style={{fontSize: 12,marginBottom:'10px'}}>
                <BreadcrumbItem>
                    <IconHome />
                </BreadcrumbItem>
                <BreadcrumbItem style={{fontWeight:20}}>{t['callList.breadcrumb.title']}</BreadcrumbItem>
            </Breadcrumb>
            <KeepAlive name="CallerListKeepAlive" cacheKey={"CallerListKeepAlive"} id={"CallerListKeepAlive"} when={true} autoFreeze={true}>
                <CallerListPanel />
            </KeepAlive>
        </>
    );
}