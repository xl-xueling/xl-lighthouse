import {
    AutoComplete, Button,
    Form, Grid, Icon, Input,
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

export default function StatAddPanel({onClose}) {

    const t = useLocale(locale);
    const formRef = useRef();
    const [size, setSize] = useState('default');

    const onValuesChange = (changeValue, values) => {
        console.log('onValuesChange: ', changeValue, values);
    };

    const editorRef = useRef<any>();

    const addCustomCompletion = () => {
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
            maskClosable={false}
            onCancel={onClose}
            modalRender={(modal) => <Draggable bounds="parent" handle=".modal-draggable-handle" disabled={false}>{modal}</Draggable>}
            style={{ width:'50%',top:'20px' }}
        >
            <Form
                ref={formRef}
                autoComplete='off'
                labelCol={{ span: 4 }}
                wrapperCol={{ span: 20 }}
                layout={"vertical"}
                onValuesChange={onValuesChange}
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
                <FormItem rules={[{ required: true }]}>
                    <Select placeholder='Please Select' allowClear defaultValue={1}>
                        <Select.Option key={1} value={1}>
                            1-minute
                        </Select.Option>
                        <Select.Option key={2} value={2}>
                            2-minute
                        </Select.Option>
                        <Select.Option key={3} value={3}>
                            5-minute
                        </Select.Option>
                        <Select.Option key={4} value={4}>
                            2-hour
                        </Select.Option>
                    </Select>
                </FormItem>

                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Expired: '}
                </Typography.Title>
                <FormItem field='expired' rules={[{ required: true }]}>
                    <Select placeholder='Please Select' allowClear defaultValue={1}>
                        <Select.Option key={1} value={1}>
                            7 Day
                        </Select.Option>
                        <Select.Option key={2} value={2}>
                            14 Day
                        </Select.Option>
                        <Select.Option key={3} value={3}>
                            1 Month
                        </Select.Option>
                        <Select.Option key={4} value={4}>
                            2 Month
                        </Select.Option>
                    </Select>
                </FormItem>
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Project: '}
                </Typography.Title>
                <FormItem disabled={true} field='project' rules={[{ required: true }]} defaultValue={'首页用户行为数据统计'}>
                    <Input type={"text"} />
                </FormItem>
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Group: '}
                </Typography.Title>
                <FormItem disabled={true} field='group' rules={[{ required: true }]} defaultValue={'homepage_behavior_stat'}>
                    <Input type={"text"} />
                </FormItem>
                <Typography.Title
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Description: '}
                </Typography.Title>
                <Form.Item field="desc" rules={[
                    { required: true, message: t['register.form.password.errMsg'], validateTrigger : ['onSubmit'] },
                    { required: true, match: new RegExp(/^[^￥{}【】#@=^&|《》]{0,200}$/,"g"),message: t['register.form.userName.validate.errMsg'] , validateTrigger : ['onSubmit']},
                ]}>
                    <Input.TextArea maxLength={200} rows={3}  showWordLimit={true}/>
                </Form.Item>
            </Form>
        </Modal>
            </Draggable>
        </>
    );

}