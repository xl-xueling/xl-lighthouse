import React, {useContext, useEffect, useRef, useState} from 'react';
import {Form, Input, Modal, Radio, Tabs, Notification, Button} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/stat/preview/settings/locale";
import {ChartTypeEnum} from "@/types/insights-common";
import {requestRenderConfig, requestRenderReset} from "@/api/stat";
import {getRandomString} from "@/utils/util";
import {StatInfoPreviewContext} from "@/pages/common/context";
import {IconRefresh, IconSearch} from "@arco-design/web-react/icon";
export default function StatPreviewSettingsModal({functionIndex = 0,onClose}) {

    const t = useLocale(locale);
    const { statInfo, setStatInfo } = useContext(StatInfoPreviewContext);
    const formRef = useRef();
    const FormItem = Form.Item;
    const TabPane = Tabs.TabPane;
    const RadioGroup = Radio.Group;
    const [selectedFunctionIndex,setSelectedFunctionIndex] = useState<number>(functionIndex);
    const [submitLoading,setSubmitLoading] = useState<boolean>(false);
    const [resetLoading,setResetLoading] = useState<boolean>(false);
    const [initialValues, setInitialValues] = useState({});

    const changeFunction = (v) => {
        setSelectedFunctionIndex(v);
    }

    const getRatios = () => {
        const templateEntity = statInfo.templateEntity;
        const stateList = templateEntity.statStateList;
        return  (
            stateList.map((option) => {
                return <Radio key={option.functionIndex} checked={option.functionIndex == 0} value={option.functionIndex}>
                    {"Function-" + (option.functionIndex)}
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
                            <FormItem style={{display:z.functionIndex == selectedFunctionIndex ? '':'none'}} label={t['statPreviewSettings.form.label.chartTitle']} field={z.functionIndex + "_chartTitle"}>
                                <Input />
                            </FormItem>
                            <FormItem style={{display:z.functionIndex == selectedFunctionIndex ? '':'none'}} field={z.functionIndex + "_chartType"} label={t['statPreviewSettings.form.label.chartType']} rules={[{ required: true}]}>
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
            chartsConfigs = statInfo.renderConfig.charts;
        }
        const templateEntity = statInfo.templateEntity;
        const stateList = templateEntity.statStateList;
        let obj={};
        stateList.map(z => {
           const functionIndex = z.functionIndex;
           const chartsConfig = chartsConfigs?.filter(item => item.functionIndex === functionIndex);
           obj[functionIndex + "_" + "chartTitle"] = chartsConfig?chartsConfig[0].title:z.stateBody;
           obj[functionIndex + "_" + "chartType"] = chartsConfig?chartsConfig[0].chartType:ChartTypeEnum.LINE_CHART;
        });
        setInitialValues(obj);
    },[statInfo])

    const handlerSubmit = async () => {
        setSubmitLoading(true);
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
                const renderConfig = {...statInfo.renderConfig,charts:chartsParams};
                const newStatInfo = {...statInfo,renderConfig:renderConfig};
                setStatInfo(newStatInfo);
                onClose();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setSubmitLoading(false);
        }).catch((error) => {
            console.log(error);
        })
    }

    const handlerReset = async () => {
        setResetLoading(true);
        await requestRenderReset({id:statInfo?.id}).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                const renderConfig = {...statInfo.renderConfig,charts:null};
                const newStatInfo = {...statInfo,renderConfig:renderConfig};
                setStatInfo(newStatInfo);
                onClose();
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setResetLoading(false);
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
            footer={
                <>
                    <Button loading={resetLoading} status={"danger"} type={"primary"} onClick={handlerReset}>
                        {t['basic.form.button.reset']}
                    </Button>
                    <Button loading={submitLoading} type="primary" onClick={handlerSubmit}>
                        {t['basic.form.button.submit']}
                    </Button>
                </>
            }
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
                            className={'disable-select'}
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