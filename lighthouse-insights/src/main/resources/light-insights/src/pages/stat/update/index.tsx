import {Button, Form, Grid, Input, Modal, Notification, Select, Typography,} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import AceEditor from "react-ace";
import "ace-builds";
import 'ace-builds/src-noconflict/ace'
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/theme-sqlserver';
import 'ace-builds/src-noconflict/theme-dawn';
import 'ace-builds/src-noconflict/theme-kuroir';
import 'ace-builds/src-noconflict/theme-gruvbox';
import 'ace-builds/src-noconflict/mode-xml';
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/ext-language_tools';
import "brace/mode/xml";
import "brace/theme/textmate";
import Draggable from 'react-draggable';
import {MdOutlineDragIndicator} from "react-icons/md";
import {Group, Project, Stat} from "@/types/insights-web";
import {requestUpdate} from "@/api/stat";
import {getStatExpiredEnumDescription, StatExpiredEnum, StatTimeParamEnum} from "@/types/insights-common";

const { Row, Col } = Grid;

export default function StatUpdateModal({statInfo,onClose,listCallback}) {

    const [loading,setLoading] = useState<boolean>(false);
    const t = useLocale(locale);
    const [groupInfo,setGroupInfo] = useState<Group>(null);
    const [projectInfo,setProjectInfo] = useState<Project>(null);
    const formRef = useRef(null);
    const [size, setSize] = useState('default');

    const editorRef = useRef<any>();

    const customCompletions = [
        { caption: 'stat-item',name: '<stat-item',value: "stat-item    />" ,meta: "Structure" ,score:10000},
        { caption: 'title',name: 'title',value: "title=\"\"", meta: "Attribute" ,score:1000},
        { caption: 'stat',name: 'stat',value: "stat=\"\"", meta: "Attribute" ,score:1000 },
        { caption: 'dimens',name: 'dimens',value: "dimens=\"\"", meta: "Attribute" ,score:1000 },
        { caption: 'limit',name: 'limit',value: "limit=\"\"", meta: "Attribute" ,score:1000},
        { caption: 'count()',name: 'count()',value: "count()", meta: "Function" ,score:100},
        { caption: 'sum()',name: 'sum()',value: "sum()", meta: "Function" ,score:100},
        { caption: 'avg()',name: 'avg()',value: "avg()", meta: "Function" ,score:100},
        { caption: 'bitcount()',name: 'bitcount()',value: "bitcount()", meta: "Function" ,score:100},
        { caption: 'max()',name: 'max()',value: "max()", meta: "Function" ,score:100},
        { caption: 'min()',name: 'min()',value: "min()", meta: "Function" ,score:100},
        { caption: 'seq()',name: 'seq()',value: "seq()", meta: "Function" ,score:100},
        { caption: 'top',name: 'top',value: "top", meta: "Keyword" ,score:10},
        { caption: 'last',name: 'last',value: "last", meta: "Keyword" ,score:10},
    ];

    const initAceEditor = () => {
        groupInfo?.columns?.forEach(z => {
            const columnCompletion = { caption: z.name,name: z.name,value: z.name, meta: "GroupColumn" ,score:10};
            customCompletions.push(columnCompletion);
        })
        setTimeout(() => {
            editorRef.current.editor.completers = [{
                getCompletions: function(editor, session, pos, prefix, callback) {
                    callback(null, customCompletions);
                },
            }];
            editorRef.current.editor.setValue(statInfo.template);
            editorRef.current.editor.clearSelection();
        },0)
    };

    const handleEditorChange = (newValue, event) => {
        const selectedText = editorRef.current.editor.getSelectedText();
        const action = event.action;
        const lines = event.lines[0];
        const editor = editorRef.current.editor;
        const matchArray = ['title=\"\"','stat=\"\"','dimens=\"\"','limit=\"\"','count()','sum()','max()','min()','avg()','bitcount()','seq()'];
        const row = event.end.row + 1;
        if(action == 'insert' && lines == "stat-item    />"){
            const endColumn = event.end.column;
            setTimeout(() => {
                editor.gotoLine(row, endColumn - 4, false);
            }, 5)
        }
        else if(action == 'insert' && matchArray.includes(lines)){
            const endColumn = event.end.column;
            setTimeout(() => {
                editor.gotoLine(row, endColumn - 1, false);
            }, 5)
        }
    };

    async function handlerSubmit(){
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const template = editorRef.current.editor.getValue();
        const updateParam:Stat = {
            id:statInfo.id,
            template:template,
            groupId:groupInfo.id,
            projectId:groupInfo.projectId,
            expired:values.expired,
            title:values.title,
            timeparam:values.timeparam,
            desc:values.desc,
        }
        console.log("updateParam is:" + JSON.stringify(updateParam));
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
            onClose();
        }).catch((error) => {
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Warning', content: t['system.error']});
        }).finally(() => {
            setLoading(false);
        })
    }

    useEffect(() => {
        // initAceEditor();
        setGroupInfo(statInfo.group);
        setProjectInfo(statInfo.project);
    },[])

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
                    style={{ width:'50%',top:'20px' }}
                >
                    <Form
                        ref={formRef}
                        autoComplete='off'
                        labelCol={{ span: 4 }}
                        wrapperCol={{ span: 20 }}
                        layout={"vertical"}
                        initialValues={{
                            title:statInfo?.title,
                            group:projectInfo?.title + ' : ' + groupInfo?.token,
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
                            {/*<AceEditor*/}
                            {/*    style={{ height:'60px',backgroundColor:"var(--color-fill-2)",width:'100%'}}*/}
                            {/*    ref={editorRef}*/}
                            {/*    readOnly={true}*/}
                            {/*    mode="xml"*/}
                            {/*    theme="kuroir"*/}
                            {/*    name="code-editor"*/}
                            {/*    editorProps={{ $blockScrolling: true }}*/}
                            {/*    enableLiveAutocompletion={true}*/}
                            {/*    enableSnippets={true}*/}
                            {/*    highlightActiveLine={false}*/}
                            {/*    showPrintMargin={false}*/}
                            {/*    showGutter={false}*/}
                            {/*    enableBasicAutocompletion={true}*/}
                            {/*    onChange={handleEditorChange}*/}
                            {/*    setOptions={{*/}
                            {/*        enableBasicAutocompletion: true,*/}
                            {/*        enableSnippets:true,*/}
                            {/*        enableLiveAutocompletion:true,*/}
                            {/*    }}*/}
                            {/*/>*/}
                            <Input.TextArea disabled={true}/>
                        </FormItem>

                        <Typography.Title
                            style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                        >
                            {'TimeParam: '}
                        </Typography.Title>
                        <FormItem field='timeparam' rules={[{ required: true }]}>
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