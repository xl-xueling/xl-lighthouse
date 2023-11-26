import {
    AutoComplete,
    Button,
    Card, Cascader,
    Checkbox, DatePicker,
    Form,
    Grid,
    Input, InputNumber, Message,
    Modal,
    Radio, Rate,
    Select, Space, Switch,
    Tabs, TreeSelect,
    Typography
} from '@arco-design/web-react';
import React, {useEffect, useRef, useState} from 'react';
import useLocale from '@/utils/useLocale';
import locale from './locale';
import styles from './style/index.module.less';
import AceEditor from "react-ace";
import "ace-builds";
import 'ace-builds/src-noconflict/ace'
import 'ace-builds/src-noconflict/theme-tomorrow';
import 'ace-builds/src-noconflict/theme-clouds_midnight';
import 'ace-builds/src-noconflict/theme-ambiance';
import 'ace-builds/src-noconflict/theme-chaos';
import 'ace-builds/src-noconflict/theme-cloud9_night';
import 'ace-builds/src-noconflict/theme-cobalt';
import 'ace-builds/src-noconflict/theme-clouds';
import 'ace-builds/src-noconflict/theme-crimson_editor';
import 'ace-builds/src-noconflict/theme-dawn';
import 'ace-builds/src-noconflict/theme-twilight';
import 'ace-builds/src-noconflict/theme-nord_dark';
import 'ace-builds/src-noconflict/theme-kuroir';
import 'ace-builds/src-noconflict/theme-dracula';
import 'ace-builds/src-noconflict/theme-katzenmilch';
import 'ace-builds/src-noconflict/theme-dreamweaver';
import 'ace-builds/src-noconflict/theme-solarized_light';
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/theme-sqlserver';
import 'ace-builds/src-noconflict/mode-xml';
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/ext-language_tools';
import  brace from "brace";
import "brace/mode/xml";
import "brace/theme/textmate";


export default function StatAddPanel({onClose}) {

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

        console.log("editorRef.current1:" + editorRef.current);
        setTimeout(() => {
            console.log("editorRef.current2:" + editorRef.current);
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
        console.log("---ss")
    },[editorRef])

    const FormItem = Form.Item;
    const cascaderOptions = [
        {
            value: 'beijing',
            label: 'Beijing',
            children: [
                {
                    value: 'beijingshi',
                    label: 'Beijing',
                    children: [
                        {
                            value: 'chaoyang',
                            label: 'Chaoyang',
                            children: [
                                {
                                    value: 'datunli',
                                    label: 'Datunli',
                                },
                            ],
                        },
                    ],
                },
            ],
        },
        {
            value: 'shanghai',
            label: 'Shanghai',
            children: [
                {
                    value: 'shanghaishi',
                    label: 'Shanghai',
                    children: [
                        {
                            value: 'huangpu',
                            label: 'Huangpu',
                        },
                    ],
                },
            ],
        },
    ];
    const formItemLayout = {
        labelCol: {
            span: 4,
        },
        wrapperCol: {
            span: 20,
        },
    };
    const noLabelLayout = {
        wrapperCol: {
            span: 17,
            offset: 7,
        },
    };



    return (
        <Modal
            title='Create Statistic'
            visible={true}
            onCancel={onClose}
            style={{ width:'50%',top:'20px' }}
        >
            <Form
                ref={formRef}
                autoComplete='off'
                {...formItemLayout}
                layout={"horizontal"}
                onValuesChange={onValuesChange}
            >
                <FormItem label={'Template'} field='template' rules={[{ required: true }]}>
                    <AceEditor
                        style={{ height:'40px',backgroundColor:"var(--color-fill-2)",width:'100%'}}
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

                <FormItem label={'TimeParam'} field='timeparam' rules={[{ required: true }]}>
                    <AutoComplete placeholder='please enter' data={['1-minute', '2-minute', '3-minute', '3-hour']} />
                </FormItem>
                <FormItem label={'Expired'} field='expired' rules={[{ required: true }]}>
                    <AutoComplete placeholder='please enter' data={['1 minute', '2 minute', '3 minute', '3 hour']} />
                </FormItem>

                <FormItem disabled={true} label={'Project'} field='timeparam' rules={[{ required: true }]} defaultValue={'首页用户行为数据统计'}>
                    <AutoComplete placeholder='首页用户行为数据统计' data={['首页用户行为数据统计', '首页用户行为数据统计', '首页用户行为数据统计', '首页用户行为数据统计']} />
                </FormItem>
            </Form>
        </Modal>
    );

}