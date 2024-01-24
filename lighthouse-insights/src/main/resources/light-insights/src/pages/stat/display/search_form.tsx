import React, {useEffect, useState} from 'react';
import {Department, TreeNode, Stat} from "@/types/insights-web";
import {ComponentTypeEnum, RenderDateConfig, RenderFilterConfig} from "@/types/insights-common";
import {Button, DatePicker, Form, Grid, Input, Select, TreeSelect} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "./style/index.module.less";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";
import {translateToTreeNodes} from "@/pages/department/common";

const { useForm } = Form;


export default function SearchForm({size,statInfo,onSearch}:{size:string,statInfo:Stat,onSearch:(values: Record<string, any>) => void}) {

    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
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
        onSearch(values);
    };

    const handleReset = () => {
        form.resetFields();
    };

    const getFilterRender = (renderFilterConfig:RenderFilterConfig) => {
        if(renderFilterConfig.componentType == ComponentTypeEnum.FILTER_INPUT){
            return (
                <Input size={"small"} placeholder={size == 'mini' ? renderFilterConfig.label : "Search Value"}  autoComplete={'off'}/>
            )
        }if(renderFilterConfig.componentType == ComponentTypeEnum.FILTER_SELECT){
            return (
                <TreeSelect size={"small"}
                            placeholder={size == 'mini' ? renderFilterConfig.label : "Please Select"}
                            multiple={true}
                            treeCheckable={true}
                            treeCheckStrictly={false}
                            treeData={translateToTreeNodes(renderFilterConfig.configData)} />
            )
        }else{
            return (
                <TreeSelect size={"small"}
                            placeholder={size == 'mini' ? renderFilterConfig.label : "Please Select"}
                            multiple={true}
                            treeCheckable={true}
                            treeCheckStrictly={false}
                            treeData={translateToTreeNodes(renderFilterConfig.configData)} />
            )
        }
    }

    return (
        <div className={styles['search-form-wrapper']}>
        <Form
            form={form}
            size={"small"}
            className={styles['search-form']}
            labelAlign="left"
            colon={": "}
            labelCol={{ span: size == 'mini' ? 0 : 5 }}
            wrapperCol={{ span: size == 'mini' ? 24:19 }}
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
                                    {getFilterRender(option)}
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