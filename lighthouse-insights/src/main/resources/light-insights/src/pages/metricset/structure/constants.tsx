import React, {useContext, useEffect, useMemo, useRef, useState} from 'react';
import {useParams} from "react-router-dom";
import {
    Card,
    Typography,
    Grid,
    Space,
    Tabs,
    Spin,
    Button, Notification, Modal, PaginationProps
} from '@arco-design/web-react';
import styles from "./style/index.module.less";
import StatPreviewPanel from "@/pages/stat/display/preview";
import StructurePanel from "@/pages/metricset/structure/structure";
import {MetricSetPreviewContext} from "@/pages/metricset/preview";
const { Title } = Typography;
const { Row, Col } = Grid;
const TabPane = Tabs.TabPane;
import {formatTimeStampBackUp, getRandomString} from "@/utils/util";
import {getResourceTypeDescription} from "@/pages/common/desc/base";
import {ResourceTypeEnum} from "@/types/insights-common";
import {TreeNode} from "@/types/insights-web";
import {treeCheckContainsNode} from "@/pages/department/common";
const { Text } = Typography;

export function getColumns(t: any,listNodes:TreeNode[], callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: t['pendAddModal.column.label.id'],
            dataIndex: 'resourceId',
            render: (value, record) =>
                <Text>{value}</Text>
            ,
        },
        {
            title: t['pendAddModal.column.label.title'],
            dataIndex: 'title',
            render: (value, record) =>
                <Text>{record?.extend?.title}</Text>
            ,
        },

        {
            title: t['pendAddModal.column.label.resourceType'],
            dataIndex: 'resourceType',
            render: (value, record) => {
                return getResourceTypeDescription(t,value);
            },
        },
        {
            title: t['pendAddModal.column.label.relationShip'],
            dataIndex: 'relationship',
            render: (value, record) => {
                let relationship;
                if(record.resourceType == ResourceTypeEnum.Stat){
                    const projectTitle = record?.extend.projectTitle;
                    const token = record?.extend.token;
                    relationship = projectTitle + " > " + token;
                }
                return relationship;
            },
        },
        {
            title: t['pendAddModal.column.label.operations'],
            dataIndex: 'operations',
            render: (value, record) => {
                console.log("listNodes data is:" + JSON.stringify(listNodes));
                let button;
                let type;
                if(record.resourceType == ResourceTypeEnum.Stat){
                    type = 'stat';
                }else if(record.resourceType == ResourceTypeEnum.Project){
                    type = 'project';
                }
                if(!treeCheckContainsNode(listNodes,record.resourceId,type)){
                    button = <Button key={getRandomString()}
                                     type="text"
                                     onClick={() => callback(record,'add')}
                                     size="mini">
                        {t['pendAddModal.column.label.operations.add']}
                    </Button>;
                }else{
                    button = <Button disabled={true} key={getRandomString()}
                                     type="secondary"
                                     size="mini">
                        {t['pendAddModal.column.label.operations.added']}
                    </Button>;
                }
                return  <Space key={getRandomString()} size={0} direction="horizontal">{[button]}</Space>;
            }
        }
    ];
}