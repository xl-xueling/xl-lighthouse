import {Button, Form, Grid, Input, Modal, Notification, Select, Typography,} from '@arco-design/web-react';
import React, {useContext, useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
// import "ace-builds";
// import 'ace-builds/src-noconflict/ace'
// import 'ace-builds/src-noconflict/theme-textmate';
// import 'ace-builds/src-noconflict/theme-sqlserver';
// import 'ace-builds/src-noconflict/theme-dawn';
// import 'ace-builds/src-noconflict/theme-kuroir';
// import 'ace-builds/src-noconflict/theme-gruvbox';
// import 'ace-builds/src-noconflict/mode-xml';
// import "ace-builds/webpack-resolver";
// import 'ace-builds/src-noconflict/ext-language_tools';
// import "brace/mode/xml";
// import "brace/theme/textmate";
import Draggable from 'react-draggable';
import {MdOutlineDragIndicator} from "react-icons/md";
import {Group, Project, Stat} from "@/types/insights-web";
import {requestUpdate} from "@/api/stat";
import {StatExpiredEnum, StatTimeParamEnum} from "@/types/insights-common";
import {getStatExpiredEnumDescription} from "@/desc/base";
import {StatInfoPreviewContext} from "@/pages/common/context";

const { Row, Col } = Grid;

export default function StatUpdateModal({onClose,listCallback}) {

    const [loading,setLoading] = useState<boolean>(false);
    const t = useLocale(locale);
    const { statInfo, setStatInfo } = useContext(StatInfoPreviewContext);
    const formRef = useRef(null);

    async function handlerSubmit(){
        try{
            await formRef.current.validate();
        }catch (error){
            console.log(error)
            return;
        }
        const values = formRef.current.getFieldsValue();
        const updateParam:Stat = {
            id:statInfo.id,
            template:values.template,
            groupId:statInfo.groupId,
            projectId:statInfo.projectId,
            expired:values.expired,
            title:values.title,
            timeparam:values.timeparam,
            desc:values.desc,
        }
        setLoading(true);
        requestUpdate(updateParam).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['statUpdate.form.submit.success']});
                const parser = new DOMParser();
                const xmlDoc = parser.parseFromString(updateParam.template, "text/xml");
                const configNode = xmlDoc.documentElement;
                configNode.setAttribute("title",updateParam.title);
                updateParam.template = new XMLSerializer().serializeToString(xmlDoc);
                const newObject = {...statInfo,...updateParam}
                listCallback(newObject,'updateCallBack');
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
            setTimeout(() => {
                onClose();
            },0)
        }).catch((error) => {
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Warning', content: t['system.error']});
        }).finally(() => {
            setLoading(false);
        })
    }

    const FormItem = Form.Item;

    return (
        <>
            <Draggable>
                <Modal
                    title= {<>
                        <Row>
                            <Grid.Col span={2} style={{textAlign:"left"}}>
                                <Button className={"modal-draggable-handle"} size={"mini"} shape={"circle"} icon={<MdOutlineDragIndicator/>} />
                            </Grid.Col>
                            <Grid.Col span={20}>
                                <div style={{ display: 'flex', alignItems: 'center', height: '100%' }}>
                                    <p style={{ margin: '0 auto' }}>{t['statUpdate.modal.title']}</p>
                                </div>
                            </Grid.Col>
                        </Row>
                    </>}
                    visible={true}
                    confirmLoading={loading}
                    onCancel={onClose}
                    onOk={handlerSubmit}
                    modalRender={(modal) => <Draggable bounds="parent" handle=".modal-draggable-handle" disabled={false}>{modal}</Draggable>}
                    alignCenter={false}
                    style={{width:'960px',maxWidth:'80%', top: '130px' }}
                >
                    <Form
                        ref={formRef}
                        autoComplete='off'
                        labelCol={{ span: 4 }}
                        wrapperCol={{ span: 20 }}
                        layout={"vertical"}
                        initialValues={{
                            title:statInfo?.title,
                            group:statInfo?.projectTitle + ' > ' + statInfo?.token,
                            timeparam:statInfo?.timeparam,
                            template:statInfo?.template,
                            expired:statInfo?.expired,
                            desc:statInfo?.desc,
                        }}
                    >
                        <Typography.Title
                            style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                        >
                            {'Title: '}
                        </Typography.Title>
                        <FormItem field='title' rules={[{ required: true }]}>
                            <Input />
                        </FormItem>
                        <Typography.Title
                            style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                        >
                            {'Template: '}
                        </Typography.Title>
                        <FormItem rules={[{ required: true }]} field={"template"}>
                            <Input.TextArea disabled={true}/>
                        </FormItem>

                        <Typography.Title
                            style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                        >
                            {'TimeParam: '}
                        </Typography.Title>
                        <FormItem field='timeparam' rules={[{ required: true }]} disabled={true}>
                            <Select placeholder='Please Select' allowClear>
                                {
                                    Object.keys(StatTimeParamEnum).filter(key => Number.isNaN(Number(key))).map((option,index) => {
                                        return <Select.Option key={index} value={option}>
                                            {option}
                                        </Select.Option>
                                    })
                                }
                            </Select>
                        </FormItem>

                        <Typography.Title
                            style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                        >
                            {'Expired: '}
                        </Typography.Title>
                        <FormItem field='expired' rules={[{ required: true }]}>
                            <Select placeholder='Please Select' allowClear>
                                {
                                    Object.keys(StatExpiredEnum).filter(key => !Number.isNaN(Number(key))).map((option,index) => {
                                        return <Select.Option key={index} value={Number(option)}>
                                            {getStatExpiredEnumDescription(option)}
                                        </Select.Option>
                                    })
                                }
                            </Select>
                        </FormItem>
                        <Typography.Title
                            style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                        >
                            {'Group: '}
                        </Typography.Title>
                        <FormItem disabled={true} field='group' rules={[{ required: true }]}>
                            <Input type={"text"}/>
                        </FormItem>
                        <Typography.Title
                            style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                        >
                            {'Description: '}
                        </Typography.Title>
                        <Form.Item field="desc">
                            <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
                        </Form.Item>
                    </Form>
                </Modal>
            </Draggable>
        </>
    );
}