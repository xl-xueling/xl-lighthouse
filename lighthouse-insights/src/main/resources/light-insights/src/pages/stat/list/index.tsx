import {
    Card,
} from '@arco-design/web-react';
import React, {useState} from 'react';
import useLocale from '@/utils/useLocale';
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import locale from "./locale";
import SearchForm from "@/pages/stat/list/form";

export default function Index() {

    const t = useLocale(locale);
    const [formParams, setFormParams] = useState({});

    function handleSearch(params) {
        console.log("---Params is:" + JSON.stringify(params));
        setFormParams(params);
    }

    return (
        <Card>
            <SearchForm onSearch={handleSearch}/>
            <StatisticalListPanel formParams={formParams}/>
        </Card>
    );
}