import {Form, Grid, Input, Message, Modal, Radio, Space, Tabs, Tree, Typography} from "@arco-design/web-react";
import React, {useState} from "react";
import StatisticalListPanel from "@/pages/stat/list/stat_list";

export default function BindedStatisticListPanel({metricSetInfo,onClose = null}) {

    const [formParams,setFormParams] = useState<any>({});

    const handlerSearch = (search) => {
        setFormParams({search});
    }

    const pagination = {
        sizeOptions: [15],
        sizeCanChange: false,
        showTotal: true,
        pageSize: 15,
        current: 1,
        pageSizeChangeResetCurrent: true,
    };

    return (
        <>
        <Form
            autoComplete={'off'}
            labelCol={{span: 4, offset: 0}}>
            <Form.Item field={'search'} style={{marginBottom:'10px'}}>
                <Input.Search size={"small"} style={{width:'320px'}} allowClear={true} onSearch={handlerSearch}/>
            </Form.Item>
        </Form>
        <StatisticalListPanel formParams={formParams} from={'bind'} extend={metricSetInfo} defaultPagination={pagination}/>
        </>
    );
}