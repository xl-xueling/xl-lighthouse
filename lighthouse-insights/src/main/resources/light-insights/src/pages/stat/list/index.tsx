import {
    Card,
    Typography,
    Avatar,
    Space,
    Grid,
    Table,
    TableColumnProps,
    Popconfirm,
    Message, Button, Form, Input, InputTag, Select, Skeleton, Spin, Tag, Icon, Tabs, PaginationProps
} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "@/pages/project/list/locale";
import SearchForm from "@/pages/stat/list/form";
import useForm from "@arco-design/web-react/es/Form/useForm";

export default function StatisticalListIndex() {

    const t = useLocale(locale);

    const [data, setData] = useState([]);

    const [formParams, setFormParams] = useState({});

    const [form] = useForm();

    function handleSearch(params) {
        setFormParams(params);
    }

    function handleReset(){
        form.resetFields();
        handleSearch({});
    }

    function onClickRadio(p){
        handleReset();
    }



    return (
        <Card>
            <SearchForm onSearch={handleSearch} onClear={handleReset} form={form}/>
            <StatisticalListPanel formParams={formParams}/>
        </Card>
    );
}