import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department, PrivilegeEnum, Project, Stat} from "@/types/insights-web";
import {CustomComponent, RenderTypeEnum} from "@/types/insights-common";
import {requestQueryDimensValue} from "@/api/group";
import {Button, Form, Grid, Select, TreeSelect} from "@arco-design/web-react";
import {translate, translateToTreeNodes} from "@/pages/department/common";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "@/pages/stat/display/style/index.module.less";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";
import {DatePicker} from "@arco-design/web-react";
import {requestQueryByIds} from "@/api/project";
import {requestQueryByIds as requestQueryComponentsByIds} from "@/api/component";
import {requestPrivilegeCheck} from "@/api/privilege";


export default function SearchForm({statInfo}:{statInfo:Stat}) {

    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const { Row, Col } = Grid;

    const [filterConfig,setFilterConfig] = useState(null);
    const [datePicker,setDatePicker] = useState<RenderTypeEnum>(null);

    useEffect(() => {
        if(!statInfo){
            return;
        }
        const customConfig = statInfo.customConfig;
        const timeparam = statInfo.timeparam;
        let datePicker;
        if(customConfig?.datepickerConfig){
            datePicker = customConfig.datepickerConfig.renderType;
        } else if(timeparam.endsWith("day")){
            datePicker = RenderTypeEnum.DATEPICKER_DATE_RANGE_SELECT;
        }else{
            datePicker = RenderTypeEnum.DATEPICKER_DATE_SELECT;
        }
        setDatePicker(datePicker);
        console.log("datePicker is:" + JSON.stringify(datePicker) + ",timeparam is:" + timeparam)

        fetchDimens().then();
    },[statInfo])



    const fetchDimens = async () => {
        const filterConfig = statInfo.customConfig?.filterConfig;
        const filterConfData:Record<string, Array<ArcoTreeNode>> = {};
        if(filterConfig){
            const componentIds = filterConfig.filter(x => x.componentId).map(x => x.componentId);
            const componentsData = await fetchComponentsInfo(componentIds);
            filterConfig.filter(x => x.componentId).forEach(z => {
                const componentId = z.componentId;
                const dimens = z.dimens;
                const component = componentsData[componentId];
                if(component){
                    filterConfData[dimens] = translateToTreeNodes(component.config);
                }
            })
        }
        const customDimensArray = Object.entries(filterConfData).map(([k,v]) => k)
            .flatMap(x => x.split(";"));
        const defaultDimensArray = statInfo.dimensArray.filter(x => !customDimensArray.includes(x));


        // Promise.all([fetchComponentsInfo()])
        //     .then(([r1]) => {
        //         console.log("r1 is:" + JSON.stringify(r1))
        //         // Object.entries(r1).map(([key, value]) => ({
        //         //     dimensDataMap.
        //         // }));
        //         dimensData = {...dimensData, ...r1};
        //         console.log("dimensData is:" + JSON.stringify(dimensData))
        //         setDimensData(dimensData);
        //     }).catch((error) => {
        //     console.log(error)
        // })
    }


    const [dimensData,setDimensData] = useState<Record<string,Array<ArcoTreeNode>>>({});

    const Option = Select.Option;

    const [options,setOptions] = useState<Record<string,Array<ArcoTreeNode>>>(null);


    const fetchComponentsInfo = async(ids) => {
        return new Promise<Record<number,CustomComponent>>((resolve,reject) => {
            requestQueryComponentsByIds(ids).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }

    const fetchDimensValue = async (arr) => {
        return new Promise<Record<number,CustomComponent>>((resolve,reject) => {
            requestQueryDimensValue({"groupId":0,"dimensArray":arr}).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                reject(error);
            })
        })
    }


    // const fetchDimensInfo:Promise<Record<string,Array<ArcoTreeNode>>> = new Promise<Record<string,Array<ArcoTreeNode>>>((resolve) => {
    //     if(!statInfo){
    //         return;
    //
    //
    //     // const params = statInfo.customConfig.filterConfig;
    //     // console.log("params is====:" + JSON.stringify(params));
    //     //
    //     // const dimensArray = params.filter(x => x. == RenderTypeEnum.FILTER_SELECT)
    //     //     ?.filter(x => !x.config.componentId && x.config).map(x => x.config.dimens);
    //     // console.log("dimensArray==== is:" + JSON.stringify(dimensArray));
    //     // const proc = async () => {
    //     //     const groupId = 0;
    //     //     const result:ResultData<Record<string,Array<ArcoTreeNode>>> = await requestQueryDimensValue({groupId,dimensArray});
    //     //     console.log("result is:" + JSON.stringify(result));
    //     //     resolve(result.data);
    //     // }
    //     // proc().then();
    // })

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
                {/*{*/}
                {/*    params.map((option,index) => {*/}
                {/*        const config = option.config;*/}

                {/*        return (*/}
                {/*            <Col span={12} key={index}>*/}
                {/*                <Form.Item label={t['projectList.columns.department']}>*/}
                {/*                    <TreeSelect*/}
                {/*                        placeholder={"Department"}*/}
                {/*                        multiple={true}*/}
                {/*                        allowClear={true}*/}
                {/*                        treeData={translate(allDepartInfo)}*/}
                {/*                    />*/}
                {/*                </Form.Item>*/}
                {/*            </Col>*/}
                {/*        );*/}
                {/*    })*/}
                {/*}*/}
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