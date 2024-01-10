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
    Checkbox,
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
import {isJSON, stringifyObj} from "@/utils/util";
import {calculateMaxLevel, validateNode} from "@/pages/components/common";
const FormItem = Form.Item;
const Option = Select.Option;
export default function FilterAddPanel({onClose}) {

    const form = useRef(null);

    // const [children,setChildren] = useState([]);

    function handlerSubmit(){
        console.log("create submit!")
        const values = form.current.getFieldsValue();
        console.log("values is:" + JSON.stringify(values));
    }

    const [formElements, setFormElements] = useState([]);

    const verify = () => {
        const values = form.current.getFieldsValue();
        const configuration = values.configuration;
        if(!isJSON(configuration)){
            Message.error("格式校验错误!");
            return;
        }
        const obj = JSON.parse(configuration);
        if(!validateNode(obj,new Set())){
            Message.error("格式校验错误!");
            return;
        }
        const level = calculateMaxLevel(obj,0);
        console.log("level is:" + level);
        if(Array.isArray(obj)){
            setFormElements([{"type":values.type,"options":obj}]);
        }else{
            setFormElements([{"type":values.type,"options":[obj]}]);
        }
    };

    return (
        <Modal
            title='Create Statistic'
            visible={true}
            onCancel={onClose}
            onOk={handlerSubmit}
            style={{ width:'800px' }}
        >
            <Form ref={form} layout={"vertical"} autoComplete='off'>
                <Typography.Text
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Title'}
                </Typography.Text>
                <FormItem field={'title'} rules={[{ required: true }]}>
                    <Input placeholder='please enter your username...' />
                </FormItem>
                <Typography.Text
                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                >
                    {'Type'}
                </Typography.Text>
                <FormItem field='type' rules={[{ required: true }]}>
                    <Select
                        placeholder='please select' defaultValue={1}
                        options={[
                            {
                                label: '多选下拉框',
                                value: 1,
                            },
                            {
                                label: '单选下拉框',
                                value: 2,
                            },
                            {
                                label: '级联下拉框',
                                value: 3,
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
                            {'Configuration'}
                        </Typography.Text>
                    </Grid.Col>
                    <Grid.Col span={8} style={{ textAlign: 'right' }}>
                        <Button type={"secondary"} size={"mini"} onClick={verify}>Verify</Button>
                    </Grid.Col>
                </Grid.Row>
                <FormItem field={'configuration'} rules={[{ required: true }]}>
                    <Input.TextArea rows={18} />
                </FormItem>
            </Form>

            {formElements.map((element, index) => {
                const {type,options} = element;
                switch (type){
                    case 1:
                        return(
                            <div key={index}>
                            <Typography.Text
                                style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                            >
                                {'Display'}
                            </Typography.Text>
                            <Select mode='multiple' key={index}  allowClear showSearch>
                            {options.map((option:any, index) => {
                                return <Option key={option.value} value={option.value}>
                                    {option.label}
                                </Option>
                            })}
                        </Select></div>);
                    case 2:
                        return (
                            <div key={index}>
                                <Typography.Text
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {'Display'}
                                </Typography.Text>
                            <Select key={index} allowClear showSearch>
                            {options.map((option:any, index) => {
                                return <Option key={option.value} value={option.value}>
                                    {option.label}
                                </Option>
                            })}
                        </Select></div>);
                    case 3:
                        return (
                            <div key={index}>
                                <Typography.Text
                                    style={{ marginTop: 0, marginBottom: 15 ,fontSize:14}}
                                >
                                    {'Display'}
                                </Typography.Text>
                                <Cascader key={index} allowClear showSearch options={options} mode='multiple'
                                          checkedStrategy='parent'>
                                </Cascader></div>);
                    default:
                        break;
                }
            }
            )}
        </Modal>

    );
}