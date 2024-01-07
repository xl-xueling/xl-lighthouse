import {
    AutoComplete, Button,
    Form, Grid, Icon, Input, Message,
    Modal, Select, Typography,
} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import "ace-builds";
import 'ace-builds/src-noconflict/ace'
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/theme-sqlserver';
import 'ace-builds/src-noconflict/mode-xml';
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/ext-language_tools';
import "brace/mode/xml";
import "brace/theme/textmate";
const { Row, Col } = Grid;
import Draggable from 'react-draggable';
import { MdOutlineDragIndicator } from "react-icons/md";
import {Project, Stat} from "@/types/insights-web";
import {requestCreate} from "@/api/stat";

export default function StatAddPanel({groupInfo,onClose}) {

    const [loading,setLoading] = useState<boolean>(false);
    const t = useLocale(locale);
    const formRef = useRef(null);
    const [size, setSize] = useState('default');
    // const onValuesChange = (changeValue, values) => {
    //     console.log('onValuesChange: ', changeValue, values);
    // };

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

    const addCustomCompletion = () => {
        groupInfo.columns?.forEach(z => {
            const columnCompletion = { caption: z.name,name: z.name,value: z.name, meta: "GroupColumn" ,score:10};
            customCompletions.push(columnCompletion);
        })
        setTimeout(() => {
            editorRef.current.editor.completers = [{
                getCompletions: function(editor, session, pos, prefix, callback) {
                    callback(null, customCompletions);
                },
            }];
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
        const stat:Stat = {
            template:template,
            groupId:groupInfo.id,
            projectId:groupInfo.projectId,
            expired:values.expired,
            timeparam:values.timeparam,
            desc:values.desc,
        }
        console.log("stat is:" + JSON.stringify(stat));
        requestCreate(stat).then((result) => {
            if(result.code === '0'){
                Message.success(t['projectCreate.form.submit.success']);
                // setTimeout(() => {
                //     window.location.href = "/project/list";
                // },3000)
            }else{
                Message.error(result.message || t['system.error']);
            }
        }).catch((error) => {
            console.log(error);
            Message.error(t['system.error'])
        }).finally(() => {
            setLoading(false);
        })
    }

    useEffect(() => {
        addCustomCompletion();
    },[editorRef])

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
                            <p style={{ margin: '0 auto' }}>Create Statistics</p>
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
                    group:'['+groupInfo.project?.title +']' + groupInfo.token,
                    timeparam:'1-day',
                    expired:1209600,
                }}
            >
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Template: '}
                </Typography.Title>
                <FormItem rules={[{ required: true }]}>
                    <AceEditor
                        style={{ height:'60px',backgroundColor:"var(--color-fill-2)",width:'100%'}}
                        ref={editorRef}
                        mode="xml"
                        theme="textmate"
                        name="code-editor"
                        editorProps={{ $blockScrolling: true }}
                        enableLiveAutocompletion={true}
                        enableSnippets={true}
                        highlightActiveLine={false}
                        showPrintMargin={false}
                        showGutter={false}
                        enableBasicAutocompletion={true}
                        onChange={handleEditorChange}
                        setOptions={{
                            enableBasicAutocompletion: true,
                            enableSnippets:true,
                            enableLiveAutocompletion:true,
                        }}
                    />
                </FormItem>

                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'TimeParam: '}
                </Typography.Title>
                <FormItem field='timeparam' rules={[{ required: true }]}>
                    <Select placeholder='Please Select' allowClear>
                        <Select.Option value={"1-minute"}>
                            1-minute
                        </Select.Option>
                        <Select.Option value={"2-minute"}>
                            2-minute
                        </Select.Option>
                        <Select.Option value={"5-minute"}>
                            5-minute
                        </Select.Option>
                        <Select.Option value={"10-minute"}>
                            10-minute
                        </Select.Option>
                        <Select.Option value={"1-hour"}>
                            1-hour
                        </Select.Option>
                        <Select.Option value={"1-day"}>
                            1-day
                        </Select.Option>
                    </Select>
                </FormItem>

                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Expired: '}
                </Typography.Title>
                <FormItem field='expired' rules={[{ required: true }]}>
                    <Select placeholder='Please Select' allowClear>
                        <Select.Option value={604800}>
                            7 Day
                        </Select.Option>
                        <Select.Option value={1209600}>
                            14 Day
                        </Select.Option>
                        <Select.Option value={2592000}>
                            1 Month
                        </Select.Option>
                        <Select.Option value={7776000}>
                            3 Month
                        </Select.Option>
                        <Select.Option value={15552000}>
                            6 Month
                        </Select.Option>
                        <Select.Option value={31104000}>
                            12 Month
                        </Select.Option>
                        <Select.Option value={62208000}>
                            24 Month
                        </Select.Option>
                        <Select.Option value={93312000}>
                            36 Month
                        </Select.Option>
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