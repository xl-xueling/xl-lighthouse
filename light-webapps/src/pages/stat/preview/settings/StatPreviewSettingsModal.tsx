import React, {useContext, useEffect, useRef, useState} from 'react';
import {Form, FormInstance, Input, Modal, Radio, Tabs, Typography} from "@arco-design/web-react";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/stat/preview/settings/locale";
export default function StatPreviewSettingsModal({statInfo,onClose}) {

    const t = useLocale(locale);
    const formRef = useRef();
    const FormItem = Form.Item;
    const TabPane = Tabs.TabPane;
    const RadioGroup = Radio.Group;
    const [functionIndex,setFunctionIndex] = useState<number>(-1);

    return (
        <Modal
            title={t['statPreviewSettings.breadcrumbItem']}
            visible={true}
            onCancel={onClose}
            alignCenter={false}
            style={{ width:'900px',verticalAlign:'top', top: '130px'}}>
            <div style={{display:"flex",justifyContent:"center",paddingTop:'20px'}}>
                <Form
                    style={{width:'80%'}}
                    colon={true}
                    labelCol={{ span: 5 }}
                    wrapperCol={{ span: 19 }}
                >
                    <FormItem label={t['statPreviewSettings.form.label.function']}>
                        <RadioGroup
                            size='small'
                            type='button' defaultValue='a' style={{ marginBottom: 20 }}>
                            <Radio value='a'>Function-1</Radio>
                            <Radio value='b'>Function-2</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label={t['statPreviewSettings.form.label.chartTitle']}>
                        <Input value={'sum(score,behavior_type == \'1\')'} />
                    </FormItem>
                    <FormItem label={t['statPreviewSettings.form.label.chartType']}>
                        <RadioGroup defaultValue='a'>
                            <Radio value='a'>{t['statPreviewSettings.form.label.chartType.lineChart']}</Radio>
                        </RadioGroup>
                    </FormItem>
                </Form>
            </div>
        </Modal>
    )
}