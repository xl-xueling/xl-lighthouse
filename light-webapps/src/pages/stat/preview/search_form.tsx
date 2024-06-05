import React, {useEffect, useState} from 'react';
import {Department, TreeNode, Stat} from "@/types/insights-web";
import {ComponentTypeEnum, RenderDateConfig, RenderFilterConfig} from "@/types/insights-common";
import {Button, DatePicker, Form, Grid, Input, Notification, Select, TreeSelect} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "./locale";
import styles from "./style/index.module.less";
import {translateToCascadeTreeNodes, translateToTreeNodes} from "@/pages/department/base";
import {
    DateTimeFormat,
    formatTimeStamp,
    getDayBefore,
    getDayEndTimestamp,
    getDayStartTimestamp, getYearBefore, getYearEndTimestamp,
    getYearStartTimestamp, MonthFormat, YearFormat
} from "@/utils/date";
import {formatString, getRandomString} from "@/utils/util";
import StructurePanel from "@/pages/metricset/structure/structure";

const { useForm } = Form;


const SearchForm = React.forwardRef(( props:{size,statInfo,onSearch},ref) => {

    const t = useLocale(locale);
    const {size = 'default',statInfo,onSearch} = props;
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const { Row, Col } = Grid;

        React.useImperativeHandle(ref,() => ({
            getData
        }));

        const getData = () => {
            return form.getFieldsValue();
        }

    useEffect(() => {
        setFiltersConfig(statInfo?.renderConfig?.filters);
    },[statInfo])

    useEffect(() => {
        handleReset();
    },[statInfo.id])

    const Option = Select.Option;

    const [filtersConfig,setFiltersConfig] = useState<Array<RenderFilterConfig>>([]);

    const [form] = useForm();

    const handleSubmit = () => {
        const values = form.getFieldsValue();
        const dateValue = values.date;
        if(!values.date){
            Notification.warning({style: { width: 420 }, title: 'Warning', content: t['statDisplay.filterConfig.warning.dateParam']});
            return;
        }
        for (let i = 0; i < filtersConfig.length; i++) {
            const dimens = filtersConfig[i].dimens;
            const dimensParam = values[dimens];
            if(!dimensParam || dimensParam.length == 0){
                Notification.warning({style: { width: 420 }, title: 'Warning', content: formatString(t['statDisplay.filterConfig.warning.otherParam'],dimens)});
                return;
            }
        }
        onSearch(values);
    };

    const handleReset = () => {
        form.resetFields();
        const initDateParam = getInitDateParam();
        form.setFieldValue("date",initDateParam);
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
                            treeProps={{
                                height: 200,
                                renderTitle: (props) => {
                                    return (
                                        <span style={{ whiteSpace: 'nowrap', }} >
                                            {props.title}
                                        </span>
                                    );
                                },
                            }}
                            treeCheckStrictly={false}
                            allowClear={true}
                            showSearch={true}
                            treeData={translateToCascadeTreeNodes(renderFilterConfig.configData)} />
            )
        }else{
            return (
                <TreeSelect size={"small"}
                            placeholder={size == 'mini' ? renderFilterConfig.label : "Please Select"}
                            multiple={true}
                            treeCheckable={true}
                            treeProps={{
                                height: 200,
                                renderTitle: (props) => {
                                    return (
                                        <span style={{ whiteSpace: 'nowrap', }} >
                                            {props.title}
                                        </span>
                                    );
                                },
                            }}
                            treeCheckStrictly={false}
                            allowClear={true}
                            showSearch={true}
                            treeData={translateToCascadeTreeNodes(renderFilterConfig.configData)} />
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
            const startTimestamp = getDayStartTimestamp(getDayBefore(Date.now(),13));
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
            return <DatePicker.RangePicker mode={"date"} style={{width:'100%'}} format="YYYY-MM-DD" allowClear={false}/>;
        }else if(statInfo.timeparam.endsWith("minute")){
            return <DatePicker.RangePicker mode={"date"} style={{width:'100%'}} format="YYYY-MM-DD" allowClear={false}/>;
        }else if(statInfo.timeparam.endsWith("hour")){
            return <DatePicker.RangePicker mode={"date"} style={{width:'100%'}} format="YYYY-MM-DD" allowClear={false}/>;
        }else if(statInfo.timeparam.endsWith("day")){
            return <DatePicker.RangePicker style={{width:'100%'}} format="YYYY-MM-DD" allowClear={false}/>;
        }else if(statInfo.timeparam.endsWith("month")){
            return <DatePicker.RangePicker mode={"month"} style={{width:'100%'}} allowClear={false}/>;
        }else if(statInfo.timeparam.endsWith("year")){
            return <DatePicker.RangePicker mode={"year"} style={{width:'100%'}} allowClear={false}/>;
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
                <Button size={size == 'mini' ? "mini" :"small"} type="primary" onClick={handleSubmit}>
                    {t['basic.form.button.preview']}
                </Button>
                {
                    statInfo?.renderConfig?.filters.length > 1 ?
                        <Button size={size == 'mini' ? "mini" :"small"} type="secondary" onClick={handleReset}>
                            {t['basic.form.button.reset']}
                        </Button>
                        : null
                }
            </div>
        </div>
    );
}
)
export default SearchForm;
