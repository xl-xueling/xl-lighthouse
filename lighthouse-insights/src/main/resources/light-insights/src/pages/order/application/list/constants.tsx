import React, {useCallback, useEffect, useMemo, useState} from 'react';
import styles from "@/pages/list/search-table/style/index.module.less";
import dayjs from "dayjs";
import {Badge, Button, Space} from "@arco-design/web-react";
import {ContentType, FilterType, Status} from "@/pages/list/search-table/constants";

export function getColumns(t: any, callback: (record: Record<string, any>, type: string) => Promise<void>) {
    return [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'CreatedTime',
            dataIndex: 'createdTime',
        },
        {
            title: 'Operation',
            dataIndex: 'operation',
            headerCellStyle: { paddingLeft: '15px' },
            render: (_, record) => (
                <Space size={0} direction="horizontal">
                    <Button
                        type="text"
                        size="small"
                        onClick={() => callback(record, 'view')}
                    >
                        详情
                    </Button>
                </Space>
            ),
        },
    ];
}
