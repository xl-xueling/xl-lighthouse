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
import {
    DateTimeFormat,
    formatTimeStamp,
    getDayBefore,
    getDayEndTimestamp,
    getDayStartTimestamp, getYearBefore, getYearEndTimestamp,
    getYearStartTimestamp, MonthFormat, YearFormat
} from "@/utils/date";
import {getRandomString} from "@/utils/util";

const { useForm } = Form;


export default function SearchForm({size,statInfo,onSearch}:{size:string,statInfo:Stat,onSearch:(values: Record<string, any>) => void}) {

    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const { Row, Col } = Grid;

    useEffect(() => {
        setFiltersConfig(statInfo?.renderConfig?.filters);
    },[statInfo])

    const Option = Select.Option;

    const [filtersConfig,setFiltersConfig] = useState<Array<RenderFilterConfig>>([]);

    const [form] = useForm();

    const handleSubmit = () => {
        const values = form.getFieldsValue();
        onSearch(values);
    };

    const handleReset = () => {
        form.resetFields();
    };

    const handleTreeSelectChange =  (key) => (selectedValue, selectedLabel) => {
        console.log("key:" + key);
    }

    const getFilterRender = (renderFilterConfig:RenderFilterConfig) => {
        if(renderFilterConfig.componentType == ComponentTypeEnum.FILTER_INPUT){
            return (
                <Input size={"small"} allowClear={true} placeholder={size == 'mini' ? renderFilterConfig.label : "Search Value"}  autoComplete={'off'}/>
            )
        }if(renderFilterConfig.componentType == ComponentTypeEnum.FILTER_SELECT){
            return (
                <TreeSelect size={"small"}
                            onChange={handleTreeSelectChange}
                            placeholder={size == 'mini' ? renderFilterConfig.label : "Please Select"}
                            multiple={true}
                            treeCheckable={true}
                            treeCheckStrictly={false}
                            allowClear={true}
                            treeData={translateToTreeNodes(renderFilterConfig.configData)} />
            )
        }else{
            return (
                <TreeSelect size={"small"}
                            placeholder={size == 'mini' ? renderFilterConfig.label : "Please Select"}
                            multiple={true}
                            treeCheckable={true}
                            treeCheckStrictly={false}
                            allowClear={true}
                            treeData={translateToTreeNodes(renderFilterConfig.configData)} />
            )
        }
    }

    const getInitDateParam = () => {
        let startDate;
        let endDate;
        if(statInfo.timeparam.endsWith("second")){
            startDate = endDate = formatTimeStamp(Date.now(),DateTimeFormat);
        }else if(statInfo.timeparam.endsWith("minute")){
            startDate = endDate = formatTimeStamp(Date.now(),DateTimeFormat);
        }else if(statInfo.timeparam.endsWith("hour")){
            startDate = endDate = formatTimeStamp(Date.now(),DateTimeFormat);
        }else if(statInfo.timeparam.endsWith("day")){
            const startTimestamp = getDayStartTimestamp(getDayBefore(Date.now(),14));
            startDate = formatTimeStamp(startTimestamp,DateTimeFormat);
            const endTimeStamp = getDayEndTimestamp(Date.now());
            endDate = formatTimeStamp(endTimeStamp,DateTimeFormat);
        }else if(statInfo.timeparam.endsWith("month")){
            const startTimestamp = getYearStartTimestamp(Date.now());
            const endTimeStamp = getYearEndTimestamp(Date.now());
            startDate = formatTimeStamp(startTimestamp,MonthFormat);
            endDate = formatTimeStamp(endTimeStamp,MonthFormat);
        }else if(statInfo.timeparam.endsWith("year")){
            const endTimeStamp = getYearStartTimestamp(Date.now());
            const startTimeStamp = getYearBefore(endTimeStamp,3);
            startDate = formatTimeStamp(startTimeStamp,YearFormat);
            endDate = formatTimeStamp(endTimeStamp,YearFormat);
        }
        return [startDate,endDate];
    }

    const getDatePicker = () => {
        if(statInfo.timeparam.endsWith("second")){
            return <DatePicker.RangePicker mode={"date"} style={{width:'100%'}} format="YYYY-MM-DD"/>;
        }else if(statInfo.timeparam.endsWith("minute")){
            return <DatePicker.RangePicker mode={"date"} style={{width:'100%'}} format="YYYY-MM-DD"/>;
        }else if(statInfo.timeparam.endsWith("hour")){
            return <DatePicker.RangePicker mode={"date"} style={{width:'100%'}} format="YYYY-MM-DD"/>;
        }else if(statInfo.timeparam.endsWith("day")){
            return <DatePicker.RangePicker style={{width:'100%'}} format="YYYY-MM-DD"/>;
        }else if(statInfo.timeparam.endsWith("month")){
            return <DatePicker.RangePicker mode={"month"} style={{width:'100%'}}/>;
        }else if(statInfo.timeparam.endsWith("year")){
            return <DatePicker.RangePicker mode={"year"} style={{width:'100%'}}/>;
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
            initialValues={{
                date: getInitDateParam(),
            }}
            labelCol={{ span: size == 'mini' ? 0 : 5 }}
            wrapperCol={{ span: size == 'mini' ? 24:19 }}
        >
            <Row gutter={24}>
                <Col span={12}>
                    <Form.Item label={'Date'} field={"date"}>
                        {getDatePicker()}
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
                <Button size={"small"} type="primary" icon={<IconSearch />} onClick={handleSubmit}>
                    {t['basic.form.button.search']}
                </Button>
                {
                    statInfo?.renderConfig?.filters.length > 1 ?
                        <Button size={"small"} type="secondary" icon={<IconSearch />} onClick={handleReset}>
                            {t['basic.form.button.reset']}
                        </Button>
                        : null
                }
            </div>
        </div>
    );
}