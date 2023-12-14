import {
    Card, Modal, Skeleton,
} from '@arco-design/web-react';
import React, {useState} from 'react';
import useLocale from '@/utils/useLocale';
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "./locale";
import SearchForm from "@/pages/stat/list/form";

export default function StatUpdateModal({statInfo,onClose}) {

    return (
        <Modal
            title= {"修改统计项"}
            visible={true}
            style={{ width:'750px',top:'20px' }}
            onCancel={onClose}>

            <Skeleton
                text={{
                    rows:3,
                    width: ['100%'],
                }}
                animation
            />
            ssssss
        </Modal>

    );
}