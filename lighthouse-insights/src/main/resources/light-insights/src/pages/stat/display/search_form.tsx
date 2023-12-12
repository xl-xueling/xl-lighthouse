import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {RenderConfig, RenderTypeEnum, ResultData} from "@/types/insights-common";
import {requestQueryDimensValue} from "@/api/group";
import {Button, Form, Grid, Select, TreeSelect} from "@arco-design/web-react";
import {translate} from "@/pages/department/common";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "@/pages/stat/display/style/index.module.less";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";
import {DatePicker} from "@arco-design/web-react";


export default function SearchForm({statInfo}) {

    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const { Row, Col } = Grid;

    const [filterConfig,setFilterConfig] = useState(null);
    const [datePicker,setDatePicker] = useState<RenderTypeEnum>(null);

    useEffect(() => {
        if(!statInfo){
            return;
        }
        console.log("----");
        const customConfig = statInfo.custom_config;
        const timeparam = statInfo.timeparam;
        let datePicker;
        if(customConfig?.datepicker_config){
            console.log("-----A")
            datePicker = customConfig.datepicker_config.render_type;
        } else if(timeparam.endsWith("day")){
            console.log("-----B")
            datePicker = RenderTypeEnum.DATEPICKER_DATE_RANGE_SELECT;
        }else{
            console.log("-----C")
            datePicker = RenderTypeEnum.DATEPICKER_DATE_SELECT;
        }
        setDatePicker(datePicker);
        console.log("datePicker is:" + JSON.stringify(datePicker) + ",timeparam is:" + timeparam)
    },[statInfo])


    const [dimensData,setDimensData] = useState<Record<string,Array<ArcoTreeNode>>>({});

    const Option = Select.Option;

    const [options,setOptions] = useState([]);

    const params:Array<RenderConfig> = [
                {
                    renderType:5,
                    config:{
                        label:'城市',
                        dimens:'province;city',
                        componentId:209,
                    },
                },
                {
                    renderType:5,
                    config:{
                        label:'行为类型',
                        dimens:'behaviorType',
                    },
                },
                {
                    renderType:5,
                    config:{
                        label:'召回号',
                        dimens:'recallno',
                    },
                }
            ];

    const fetchDimensInfo:Promise<Record<string,Array<ArcoTreeNode>>> = new Promise<Record<string,Array<ArcoTreeNode>>>((resolve) => {
        const dimensArray = params.filter(x => x.renderType == RenderTypeEnum.FILTER_SELECT)
            ?.filter(x => !x.config.componentId && x.config).map(x => x.config.dimens);
        console.log("dimensArray is:" + JSON.stringify(dimensArray));
        const proc = async () => {
            const groupId = 0;
            const result:ResultData<Record<string,Array<ArcoTreeNode>>> = await requestQueryDimensValue({groupId,dimensArray});
            console.log("result is:" + JSON.stringify(result));
            resolve(result.data);
        }
        proc().then();
    })

    const getDatePicker = () => {
        switch (datePicker){
            case RenderTypeEnum.DATEPICKER_DATE_SELECT:
                return <DatePicker />
            case RenderTypeEnum.DATEPICKER_DATE_RANGE_SELECT:
                return <DatePicker.RangePicker showTime={true}/>
            case RenderTypeEnum.DATEPICKER_DATE_TIME_RANGE_SELECT:
                return <DatePicker.RangePicker showTime={true} />
            default:
                return null;
        }
    }

    useEffect(() => {
        let dimensData:Record<string,Array<ArcoTreeNode>> = {};
        Promise.all([fetchDimensInfo])
            .then(([r1]) => {
                console.log("r1 is:" + JSON.stringify(r1))
                // Object.entries(r1).map(([key, value]) => ({
                //     dimensDataMap.
                // }));
                dimensData = {...dimensData, ...r1};
                console.log("dimensData is:" + JSON.stringify(dimensData))
                setDimensData(dimensData);
            }).catch((error) => {
                console.log(error)
            })
    },[])


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
                        {getDatePicker()}
                    </Form.Item>
                </Col>
                {
                    params.map((option,index) => {
                        return (
                            <Col span={12} key={index}>
                                <Form.Item label={t['projectList.columns.department']}>
                                    <TreeSelect
                                        placeholder={"Department"}
                                        multiple={true}
                                        allowClear={true}
                                        treeData={translate(allDepartInfo)}
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