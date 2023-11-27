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
import locale from './locale';
import styles from './style/index.module.less';
import {requestList} from "@/api/project";
import {ResultData} from "@/types/insights-common";
import {Department, PrivilegeEnum, Project, ProjectPagination} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import {getDataWithLocalCache} from "@/utils/localCache";
import {fetchAllData as fetchAllDepartmentData, translateToTreeStruct} from "@/pages/department/common";
import InfoForm from "@/pages/user/setting/info";
import Security from "@/pages/user/setting/security";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {isJSON, stringifyObj} from "@/utils/util";
import {useSelector} from "react-redux";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import ProjectCreate from "@/pages/project/list/create";
import ProjectUpdate from "@/pages/project/list/update";
import {requestDeleteById} from "@/api/project";
import {FormInstance} from "@arco-design/web-react/lib";
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
            style={{ width:'800px',marginTop:"-30px" }}
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
                        placeholder='please select'
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
                    <Input.TextArea rows={5} />
                </FormItem>
            </Form>

            {formElements.map((element, index) => {
                const {type,options} = element;
                switch (type){
                    case 1:
                        return <Select mode='multiple' key={index}  allowClear showSearch>
                            {options.map((option:any, index) => {
                                return <Option key={option.value} value={option.value}>
                                    {option.label}
                                </Option>
                            })}
                        </Select>
                    case 2:
                        return <Select key={index} allowClear showSearch>
                            {options.map((option:any, index) => {
                                return <Option key={option.value} value={option.value}>
                                    {option.label}
                                </Option>
                            })}
                        </Select>
                    default:
                        return <Input />
                        break;
                }
            })
            }
        </Modal>

    );
}