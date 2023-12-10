import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {ArcoTreeNode, Department} from "@/types/insights-web";
import {RenderConfig, RenderTypeEnum, ResultData} from "@/types/insights-common";
import {requestQueryDimensValue} from "@/api/group";
import {Button, Form, Grid, Select, TreeSelect} from "@arco-design/web-react";
import {Col} from "antd";
import {translate} from "@/pages/department/common";
import {useSelector} from "react-redux";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import styles from "@/pages/stat/display/style/index.module.less";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";


export default function SearchForm({groupId = 0}) {

    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const { Row, Col } = Grid;

    // const b = {
    //     datepicker_config:{
    //         render_type:1,
    //     },
    //     filter_config:[
    //         {
    //             render_type:5,
    //             custom_config:{
    //                 label:'省份',
    //                 dimens:'province',
    //                 component_id:209,
    //             },
    //         },
    //         {
    //             render_type:6,
    //             custom_config: {
    //                 label:'省份',
    //                 dimens:'province',
    //                 remote_url: 'http://xxxxx.shtml'
    //             }
    //         },
    //     ]
    // }
    //
    // const a = [
    //     {
    //         render_type:5,
    //         custom_config:{
    //             label:'省份',
    //             dimens:'province',
    //             component_id:209,
    //         },
    //     },
    //     {
    //         render_type:6,
    //         custom_config: {
    //             label:'省份',
    //             dimens:'province',
    //             remote_url: 'http://xxxxx.shtml'
    //         }
    //     },
    // ]
    //
    // const filterParams = [
    //     {
    //         type:4, // 1,日期选择框，type:2，日期区间选择框，type:3,日期时间选择框,type:4 输入框，type:5,下拉框，type:6：远程搜索下拉框
    //         data:[
    //             {
    //                 "label":"山东",
    //                 "value":"1001",
    //                 "children":[
    //                     {
    //                         "label":"济南",
    //                         "value":"10011",
    //                     },
    //                     {
    //                         "label":"青岛",
    //                         "value":"10012",
    //                     }
    //                 ]
    //             },
    //         ]
    //     },
    // ]

    const [dimensData,setDimensData] = useState<Record<string,Array<ArcoTreeNode>>>({});

    const param =
        [
            {
                render_type:5,
                custom_config:{
                    label:'省份',
                    dimens:'province',
                    component_id:209,
                }
            },
            {
                render_type:6,
                custom_config:{
                    label:'类型',
                    dimens:'type',
                }
            }
        ]

    // const fetchPrivilegeInfo:Promise<Record<number,PrivilegeEnum[]>> = new Promise<Record<number,PrivilegeEnum[]>>((resolve) => {
    //     const projectIds = projectsInfo?.map(z => z.id);
    //     const proc = async () => {
    //         const result:ResultData<Record<number,PrivilegeEnum[]>> = await requestPrivilegeCheck({type:"project",items:projectIds});
    //         resolve(result.data);
    //     }
    //     proc().then();
    // })

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
            const result:ResultData<Record<string,Array<ArcoTreeNode>>> = await requestQueryDimensValue({groupId,dimensArray});
            console.log("result is:" + JSON.stringify(result));
            resolve(result.data);
        }
        proc().then();
    })

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
            className={styles['search-form']}
            labelAlign="left"
            labelCol={{ span: 5 }}
            wrapperCol={{ span: 19 }}
        >
            <Row gutter={24}>
                {
                    params.map((option,index) => {
                        return (
                            <Col span={12} key={index}>
                                <Form.Item label={t['projectList.columns.department']}>
                                    <TreeSelect
                                        placeholder={"Search Department"}
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