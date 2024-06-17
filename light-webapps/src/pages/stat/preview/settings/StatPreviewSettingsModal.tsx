import React, {useContext, useEffect, useRef, useState} from 'react';
import {Form, FormInstance, Input, Modal, Radio, Tabs, Typography,Notification} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/stat/preview/settings/locale";
import {Stat} from "@/types/insights-web";
import {ChartTypeEnum} from "@/types/insights-common";
import {requestAuthActivation} from "../../../../../../../light-pro-webapps/.yalc/light-webapps/build/api/authorize";
import {requestRenderConfig} from "@/api/stat";
import {getRandomString} from "@/utils/util";
export default function StatPreviewSettingsModal({statInfo,functionIndex = 0,onClose}:{statInfo:Stat}) {

    const t = useLocale(locale);
    const formRef = useRef();
    const FormItem = Form.Item;
    const TabPane = Tabs.TabPane;
    const RadioGroup = Radio.Group;
    const [selectedFunctionIndex,setSelectedFunctionIndex] = useState<number>(functionIndex);
    const [loading,setLoading] = useState<boolean>(false);
    const [initialValues, setInitialValues] = useState({});

    const changeFunction = (v) => {
        setSelectedFunctionIndex(v);
    }

    const getRatios = () => {
        const templateEntity = statInfo.templateEntity;
        const stateList = templateEntity.statStateList;
        return  (
            stateList.map((option) => {
                return <Radio key={option.functionIndex} checked={option.functionIndex == 1} value={option.functionIndex}>
                    {"Function-" + (option.functionIndex + 1)}
                </Radio>
                }
            )
        )
    }

    const getFormItems = () => {
        const templateEntity = statInfo.templateEntity;
        const stateList = templateEntity.statStateList;
           return stateList.map(z => {
                return <div key={getRandomString()}>
                    <FormItem style={{display:z.functionIndex == selectedFunctionIndex ? 'none':''}} label={t['statPreviewSettings.form.label.chartTitle']} field={z.functionIndex + "_chartTitle"}>
                        <Input />
                    </FormItem>
                    <FormItem style={{display:z.functionIndex == selectedFunctionIndex ? 'none':''}} field={z.functionIndex + "_chartType"} label={t['statPreviewSettings.form.label.chartType']} rules={[{ required: true}]}>
                        <Radio.Group defaultValue={ChartTypeEnum.LINE_CHART}>
                            <Radio checked={true} value={ChartTypeEnum.LINE_CHART}>{t['statPreviewSettings.form.label.chartType.lineChart']}</Radio>
                        </Radio.Group>
                    </FormItem>
                </div>
            });
    }

    useEffect(() => {
        let chartsConfigs;
        if(statInfo?.renderConfig?.charts){
            console.log("statInfo charts is:" + JSON.stringify(statInfo.renderConfig.charts));
            chartsConfigs = statInfo.renderConfig.charts;
        }
        const templateEntity = statInfo.templateEntity;
        const stateList = templateEntity.statStateList;
        let obj={};
        stateList.map(z => {
           const functionIndex = z.functionIndex;
           const chartsConfig = chartsConfigs?.filter(item => item.functionIndex === functionIndex);
           console.log("chartsConfig is:" + JSON.stringify(chartsConfig));
           obj[functionIndex + "_" + "chartTitle"] = chartsConfig?chartsConfig[0].title:z.stateBody;
           obj[functionIndex + "_" + "chartType"] = chartsConfig?chartsConfig[0].chartType:ChartTypeEnum.LINE_CHART;
        });
        console.log("obj is:" + JSON.stringify(obj));
        setInitialValues(obj);
    },[statInfo])

    const handlerSubmit = async () => {
        setLoading(true);
        try{
            await formRef.current.validate();
        }catch (error){
            console.log(error)
            return;
        }
        const values = formRef.current.getFieldsValue();
        const templateEntity = statInfo.templateEntity;
        const stateList = templateEntity.statStateList;
        const chartsParams=[];
        for (let i = 0; i < stateList.length; i++) {
            const functionIndex = stateList[i].functionIndex;
            const renderObject = {};
            renderObject['functionIndex']=functionIndex;
            renderObject['title']=values[functionIndex+"_chartTitle"];
            renderObject['chartType']=values[functionIndex+"_chartType"];
            chartsParams.push(renderObject);
        }
        const renderConfig = {
            id:statInfo?.id,
            charts:chartsParams,
        }
        await requestRenderConfig(renderConfig).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }
    
    return (
        <Modal
            title={t['statPreviewSettings.breadcrumbItem']}
            visible={true}
            onCancel={onClose}
            alignCenter={false}
            onOk={handlerSubmit}
            style={{ width:'900px',verticalAlign:'top', top: '130px'}}>
            <div style={{display:"flex",justifyContent:"center",paddingTop:'20px'}}>
                <Form
                    ref={formRef}
                    style={{width:'80%'}}
                    colon={true}
                    labelCol={{ span: 5 }}
                    wrapperCol={{ span: 19 }}
                    initialValues={initialValues}
                >
                    <FormItem label={t['statPreviewSettings.form.label.function']}>
                        <RadioGroup
                            size='small' onChange={changeFunction}
                            type='button' defaultValue={0} style={{ marginBottom: 20 }}>
                            {getRatios()}
                        </RadioGroup>
                    </FormItem>
                    {getFormItems()}
                </Form>
            </div>
        </Modal>
    )
}