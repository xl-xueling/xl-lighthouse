import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
    Form,
    Input,
    Select,
    DatePicker,
    Button,
    Grid, TreeSelect,Spin,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import {stringifyObj} from "@/utils/util";
import {useSelector} from "react-redux";
import {Department, User} from "@/types/insights-web";
import {translate, translateToTreeStruct} from "@/pages/department/common";
import {requestTermList} from "@/api/project";
import {debounce} from "react-ace/lib/editorOptions";
import ProjectTermQuery from "@/pages/project/common/projectTermQuery";

const { Row, Col } = Grid;
const { useForm } = Form;

function SearchForm(props: {onSearch: (values: Record<string, any>) => void;}):any {
    const t = useLocale(locale);
    const { lang } = useContext(GlobalContext);
    const departmentTreeRef = useRef(null);
    const refFetchId = useRef(null);
    const [fetching, setFetching] = useState(false);
    const [form] = useForm();
    const [options, setOptions] = useState([]);

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const handleSubmit = () => {
        const values = form.getFieldsValue();
        props.onSearch(values);
    };

    const handleReset = () => {
        form.resetFields();
        props.onSearch({});
    };

    const colSpan = 12;



    return (
        <div className={styles['search-form-wrapper']}>
            <Form
                form={form}
                className={styles['search-form']}
                labelAlign="left"
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 19 }}
            >
                <Row gutter={24}>
                    <Col span={colSpan}>
                        <Form.Item label={'ID'} field="id">
                            <Input placeholder={'ID'} allowClear />
                        </Form.Item>
                    </Col>
                    <Col span={colSpan}>
                        <Form.Item label={"Name"} field="name">
                            <Input
                                allowClear
                                placeholder={"Name"}
                            />
                        </Form.Item>
                    </Col>
                    <Col span={colSpan}>
                        <Form.Item label={"Department"} field="department">
                            <TreeSelect
                                ref={departmentTreeRef}
                                placeholder={"Please select"}
                                multiple={true}
                                allowClear={true}
                                treeData={translate(allDepartInfo)}
                                onChange={(e,v) => {
                                    if(!e || e.length == '0'){
                                        form.resetFields();
                                        return;
                                    }
                                }}
                            />
                        </Form.Item>
                    </Col>
                    <Col span={colSpan}>
                        <Form.Item label={"Project"} field="project">
                            <ProjectTermQuery />
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
            <div className={styles['right-button']}>
                <Button size={"small"} type="primary" icon={<IconSearch />} onClick={handleSubmit}>
                    {"搜索"}
                </Button>
                <Button size={"small"} icon={<IconRefresh />} onClick={handleReset}>
                    {"重置"}
                </Button>
            </div>
        </div>
    );
}

export default SearchForm;
