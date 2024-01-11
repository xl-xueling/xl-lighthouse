import React, {useEffect, useMemo, useRef, useState} from 'react';
import {
    Radio,
    Button,
    Card,
    Grid,
    PaginationProps,
    Space,
    Table,
    Tabs,
    Typography,
    Modal,
    Divider,
    Steps,
    AutoComplete,
    Select,
    Cascader,
    Form,
    Input,
    InputNumber,
    TreeSelect,
    Switch,
    Message,
    Checkbox, Notification,
} from '@arco-design/web-react';
import PermissionWrapper from '@/components/PermissionWrapper';
import {
    IconCheck,
    IconClose,
    IconDown,
    IconDownload, IconEdit, IconPenFill,
    IconPlus,
    IconRefresh, IconRight,
    IconSearch
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import {getTextBlenLength, isJSON, stringifyObj} from "@/utils/util";
import {calculateMaxLevel, validateNode} from "@/pages/components/common";
import {getComponentTypeDescription} from "@/pages/common/desc/base";
import locale from "./locale";
import {ComponentTypeEnum} from "@/types/insights-common";
import {TEXT_BASE_PATTERN_2} from "@/utils/constants";
const FormItem = Form.Item;
const Option = Select.Option;
import AceEditor from "react-ace";
import "ace-builds";
import 'ace-builds/src-noconflict/ace'
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/theme-gruvbox';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/mode-json5';
import 'ace-builds/src-noconflict/mode-jsoniq';
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/ext-language_tools';
import "brace/mode/xml";
import "brace/mode/json";
import "brace/mode/jsoniq";
import "brace/theme/textmate";
import {requestCreate} from "@/api/component";
import {requestVerify} from "@/api/component";
import {translate, translateResponse} from "@/pages/department/common";


export default function FilterAddPanel({onClose}) {

    const t = useLocale(locale);
    const [editorTheme,setEditorTheme] = useState('textmate');
    const [loading,setLoading] = useState<boolean>();
    const editorRef = useRef<any>();
    const formRef = useRef(null);

    async function handlerSubmit(){
        setLoading(true);
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const configuration = values.configuration;
        const verifyData = {
            title:values.title,
            type:values.type,
            configuration:values.configuration,
        }
        const obj = JSON.parse(configuration);
        requestCreate(verifyData).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['componentCreate.form.create.submit.success']});
                setFormElements([{"type":values.type,"options":obj}]);
            }else{
                Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Warning', content: t['system.error']});
        }).finally(() => {
            setLoading(false);
        })
    }

    async function verifySubmit(){
        setLoading(true);
        await formRef.current.validate();
        const values = formRef.current.getFieldsValue();
        const configuration = values.configuration;
        const verifyData = {
            type:values.type,
            configuration:values.configuration,
        }
        const obj = JSON.parse(configuration);
        requestCreate(verifyData).then((response) => {
            const {code, data ,message} = response;
            if(code == '0'){
                Notification.info({style: { width: 420 }, title: 'Notification', content: t['componentCreate.form.verify.submit.success']});
                setFormElements([{"type":values.type,"options":obj}]);
            }else{
               Notification.warning({style: { width: 420 }, title: 'Warning', content: message || t['system.error']});
            }
        }).catch((error) => {
            console.log(error);
            Notification.error({style: { width: 420 }, title: 'Warning', content: t['system.error']});
        }).finally(() => {
            setLoading(false);
        })
    }

    const [formElements, setFormElements] = useState([]);

    return (
        <Modal
            title={t['componentCreate.modal.title']}
            visible={true}
            onCancel={onClose}
            onOk={handlerSubmit}
            style={{ width:'800px' }}
        >
            <Form ref={formRef} layout={"vertical"}
                  initialValues={{
                      type:5,
                  }}
                  autoComplete='off'>
                <Typography.Text
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {t['componentCreate.form.label.title']}
                </Typography.Text>
                <FormItem field={'title'} rules={[
                    { required: true, message: t['componentCreate.form.title.errMsg'] , validateTrigger : ['onSubmit']},
                    { required: true, match: new RegExp(TEXT_BASE_PATTERN_2,"g"),message: t['componentCreate.form.title.validate.errMsg'] , validateTrigger : ['onSubmit']},
                    {
                        required:true,
                        validator: (v, cb) => {
                            if (getTextBlenLength(v) < 5) {
                                return cb(t['componentCreate.form.title.less.limit'])
                            }else if (getTextBlenLength(v) > 25) {
                                return cb(t['componentCreate.form.title.exceeds.limit'])
                            }
                            cb(null);
                        }
                        , validateTrigger : ['onSubmit']
                    }]}>
                    <Input/>
                </FormItem>
                <Typography.Text
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {t['componentCreate.form.label.type']}
                </Typography.Text>
                <FormItem field='componentType' rules={[{ required: true }]}>
                    <Select
                        placeholder='please select' defaultValue={1}
                        options={[
                            {
                                label: getComponentTypeDescription(t,ComponentTypeEnum.FILTER_SELECT),
                                value: 5,
                            },
                        ]}
                        allowClear
                    />
                </FormItem>
                <Grid.Row style={{ marginBottom:'10px' }}>
                    <Grid.Col span={16}>
                        <Typography.Text
                            style={{ marginTop: 0 ,fontSize:14}}
                        >
                            {t['componentCreate.form.label.configuration']}
                        </Typography.Text>
                    </Grid.Col>
                    <Grid.Col span={8} style={{ textAlign: 'right' }}>
                        <Button type={"secondary"} size={"mini"} onClick={verifySubmit}>{t['componentCreate.form.button.verify']}</Button>
                    </Grid.Col>
                </Grid.Row>
                <FormItem field={'configuration'} rules={[{ required: true }]}>
                    <AceEditor
                        style={{ height:'400px',backgroundColor:"var(--color-fill-2)",width:'100%'}}
                        ref={editorRef}
                        mode="json"
                        theme={editorTheme}
                        name="code-editor"
                        editorProps={{ $blockScrolling: true }}
                        enableLiveAutocompletion={true}
                        enableSnippets={true}
                        highlightActiveLine={false}
                        showPrintMargin={false}
                        showGutter={true}
                        enableBasicAutocompletion={true}
                        setOptions={{
                            enableBasicAutocompletion: true,
                            enableSnippets:true,
                            enableLiveAutocompletion:true,
                        }}
                    />
                </FormItem>
                <Typography.Text
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {t['componentCreate.form.label.privateType']}
                </Typography.Text>
                <FormItem field='privateType' rules={[{ required: true }]}>
                    <Radio.Group defaultValue={0}>
                        <Radio value={0}>{t['componentCreate.form.label.privateType.private']}</Radio>
                        <Radio value={1}>{t['componentCreate.form.label.privateType.public']}</Radio>
                    </Radio.Group>
                </FormItem>
            </Form>

            {formElements.map((element, index) => {
                const {type,options} = element;
                switch (type){
                    case ComponentTypeEnum.FILTER_SELECT:
                        return (
                            <div key={index}>
                                <Typography.Text
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {'Display'}
                                </Typography.Text>
                                <TreeSelect
                                    placeholder={"Please Select"}
                                    multiple={true}
                                    allowClear={true}
                                    treeData={translateResponse(options)}
                                />
                            </div>);
                    default:
                        break;
                }
            }
            )}
        </Modal>

    );
}