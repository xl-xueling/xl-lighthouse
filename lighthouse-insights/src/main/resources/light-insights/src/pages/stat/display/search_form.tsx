import React, {useEffect, useState} from 'react';
import {Department, TreeNode, Stat} from "@/types/insights-web";
import {ComponentTypeEnum, RenderDateConfig, RenderFilterConfig} from "@/types/insights-common";
import {Button, DatePicker, Form, Grid, Select, TreeSelect} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "./style/index.module.less";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";
import {translateToTreeNodes} from "@/pages/department/common";

const { useForm } = Form;


export default function SearchForm({statInfo,onSearch}:{statInfo:Stat,onSearch:(values: Record<string, any>) => void}) {

    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const { Row, Col } = Grid;

    const [datePickerConfig,setDatePickerConfig] = useState<RenderDateConfig>(null);

    useEffect(() => {
        const dateConfig = statInfo?.renderConfig?.datepicker;
        setDatePickerConfig(dateConfig ? dateConfig : {
            componentType: ComponentTypeEnum.DATEPICKER_DATE_RANGE_SELECT,
            label: 'Date'
        });
        setFiltersConfig(statInfo?.renderConfig?.filters);
    },[statInfo])

    const Option = Select.Option;

    const [filtersConfig,setFiltersConfig] = useState<Array<RenderFilterConfig>>([]);

    const getDatePicker = (datePickerConfig:RenderDateConfig) => {
        switch (datePickerConfig?.componentType){
            case ComponentTypeEnum.DATEPICKER_DATE_SELECT:
                return <DatePicker style={{width:'100%'}}/>
            case ComponentTypeEnum.DATEPICKER_DATE_RANGE_SELECT:
                return <DatePicker.RangePicker style={{width:'100%'}}/>
            case ComponentTypeEnum.DATEPICKER_DATE_TIME_RANGE_SELECT:
                return <DatePicker.RangePicker showTime={true} style={{width:'100%'}}/>
            default:
                return null;
        }
    }

    const [form] = useForm();

    const handleSubmit = () => {
        const values = form.getFieldsValue();
        console.log("values is:" + JSON.stringify(values));
        onSearch(values);
    };

    const handleReset = () => {
        form.resetFields();
    };

    return (
        <div className={styles['search-form-wrapper']}>
        <Form
            form={form}
            size={"small"}
            className={styles['search-form']}
            labelAlign="left"
            colon={": "}
            // style={{minHeight:'90px'}}
            labelCol={{ span: 5 }}
            wrapperCol={{ span: 19 }}
        >
            <Row gutter={24}>
                <Col span={12}>
                    <Form.Item label={'Date'} field={"date"}>
                        {getDatePicker(datePickerConfig)}
                    </Form.Item>
                </Col>
                {
                    filtersConfig.map((option,index) => {
                        return (
                            <Col span={12} key={index}>
                                <Form.Item label={option.label} field={option.dimens}>
                                    <TreeSelect
                                        placeholder={"Please Select"}
                                        multiple={true}
                                        treeCheckable={true}
                                        treeCheckStrictly={false}
                                        allowClear={true}
                                        treeData={translateToTreeNodes(option.configData)}
                                    />
                                </Form.Item>
                            </Col>
                        );
                    })
                }
            </Row>
        </Form>
            <div className={styles['right-button']}>
                <Button type="primary" icon={<IconSearch />} onClick={handleSubmit}>
                    {'搜索'}
                </Button>
                {/*<Button type="secondary" icon={<IconSearch />} onClick={handleReset}>*/}
                {/*    {'重置'}*/}
                {/*</Button>*/}
            </div>
        </div>
    );
}