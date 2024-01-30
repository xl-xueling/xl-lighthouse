import {Form, Grid, Input} from "@arco-design/web-react";
import React, {useState} from "react";
import StatisticalListPanel from "@/pages/stat/list/stat_list";
import ProjectListPanel from "@/pages/project/list/ProjectListPanel";

export default function BindedProjectListPanel({metricSetInfo}) {

    const [formParams,setFormParams] = useState<any>({});

    const handlerSearch = (search) => {
        setFormParams({search});
    }

    return (
        <>
        <Form
            autoComplete={'off'}
            labelCol={{span: 4, offset: 0}}>
            <Form.Item field={'search'} style={{marginBottom:'10px'}}>
                <Input.Search size={"small"} style={{width:'320px'}} allowClear={true} onSearch={handlerSearch}/>
            </Form.Item>
        </Form>
            <ProjectListPanel formParams={formParams} from={'bind'} extend={metricSetInfo}/>
        </>
    );
}