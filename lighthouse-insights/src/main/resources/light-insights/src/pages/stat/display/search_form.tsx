import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {Department, Stat} from "@/types/insights-web";
import {DatePickerConfigParam, FilterConfigParam, RenderTypeEnum} from "@/types/insights-common";
import {Button, Form, Grid, Select, TreeSelect} from "@arco-design/web-react";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "@/pages/stat/display/style/index.module.less";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";
import {DatePicker} from "@arco-design/web-react";
import {translateToTreeNodes} from "@/pages/department/common";


export default function SearchForm({statInfo}:{statInfo:Stat}) {

    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const { Row, Col } = Grid;

    useEffect(() => {
        if(!statInfo){
            return;
        }
        setDatePickerConfig(statInfo.renderConfig.datepicker);
        setFiltersConfig(statInfo.renderConfig.filters);
    },[statInfo])


    const Option = Select.Option;

    const [datePickerConfig,setDatePickerConfig] = useState<DatePickerConfigParam>(null);
    const [filtersConfig,setFiltersConfig] = useState<Array<FilterConfigParam>>([]);

    const getDatePicker = (datePickerConfig:DatePickerConfigParam) => {
        switch (datePickerConfig?.renderType){
            case RenderTypeEnum.DATEPICKER_DATE_SELECT:
                return <DatePicker />
            case RenderTypeEnum.DATEPICKER_DATE_RANGE_SELECT:
                return <DatePicker.RangePicker />
            case RenderTypeEnum.DATEPICKER_DATE_TIME_RANGE_SELECT:
                return <DatePicker.RangePicker showTime={true} />
            default:
                return null;
        }
    }


    return (
        <div className={styles['search-form-wrapper']}>
        <Form
            size={"small"}
            className={styles['search-form']}
            labelAlign="left"
            labelCol={{ span: 5 }}
            wrapperCol={{ span: 19 }}
        >
            <Row gutter={24}>
                <Col span={12}>
                    <Form.Item label={'Date'}>
                        {getDatePicker(datePickerConfig)}
                    </Form.Item>
                </Col>
                {
                    filtersConfig.map((option,index) => {
                        return (
                            <Col span={12} key={index}>
                                <Form.Item label={option.label}>
                                    <TreeSelect
                                        placeholder={"Search " + option.dimens}
                                        multiple={true}
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
                <Button type="primary" icon={<IconSearch />} >
                    {'搜索'}
                </Button>
                <Button icon={<IconRefresh />} >
                    {'重置'}
                </Button>
            </div>
        </div>
    );
}