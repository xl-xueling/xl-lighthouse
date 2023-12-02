import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
    Form,
    Input,
    Button,
    Grid, TreeSelect,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import {translate} from "@/pages/department/common";
import ProjectTermQuery from "@/pages/project/common/projectTermQuery";

const { Row, Col } = Grid;
const { useForm } = Form;

function SearchForm(props: {onSearch: (values: Record<string, any>) => void;}):any {
    const t = useLocale(locale);
    const { lang } = useContext(GlobalContext);
    const [form] = useForm();
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
                        <Form.Item label={t['statList.table.columns.name']} field="name">
                            <Input
                                allowClear
                                placeholder={"Name"}
                            />
                        </Form.Item>
                    </Col>
                    <Col span={colSpan}>
                        <Form.Item label={t['statList.table.columns.department']} field="department">
                            <TreeSelect
                                placeholder={"Please Select"}
                                multiple={true}
                                allowClear={true}
                                treeData={translate(allDepartInfo)}
                            />
                        </Form.Item>
                    </Col>
                    <Col span={colSpan}>
                        <Form.Item label={t['statList.table.columns.project']} field="project">
                            <ProjectTermQuery />
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
            <div className={styles['right-button']}>
                <Button size={"small"} type="primary" icon={<IconSearch />} onClick={handleSubmit}>
                    {t['statList.table.form.button.search']}
                </Button>
                <Button size={"small"} icon={<IconRefresh />} onClick={handleReset}>
                    {t['statList.table.form.button.reset']}
                </Button>
            </div>
        </div>
    );
}

export default SearchForm;
