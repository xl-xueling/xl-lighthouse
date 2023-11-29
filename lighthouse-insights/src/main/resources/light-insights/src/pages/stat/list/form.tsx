import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import dayjs from 'dayjs';
import {
    Form,
    Input,
    Select,
    DatePicker,
    Button,
    Grid, TreeSelect, Tabs, Avatar, Spin,
} from '@arco-design/web-react';
import { GlobalContext } from '@/context';
import locale from './locale';
import useLocale from '@/utils/useLocale';
import { IconRefresh, IconSearch } from '@arco-design/web-react/icon';
import styles from './style/index.module.less';
import {stringifyObj} from "@/utils/util";
import {useSelector} from "react-redux";
import {Department, User} from "@/types/insights-web";
import {translateToTreeStruct} from "@/pages/department/common";
import InfoForm from "@/pages/user/settings/info";
import Security from "@/pages/user/settings/security";
import project from "@/pages/user/info/blocks/project";
import {debounce} from "react-ace/lib/editorOptions";
import {requestTermList} from "@/api/project";

const { Row, Col } = Grid;
const { useForm } = Form;

function SearchForm(props: {
    onSearch: (values: Record<string, any>) => void;
    form;
    onClear;
}):any {
    const { lang } = useContext(GlobalContext);
    const departmentTreeRef = useRef(null);
    const refFetchId = useRef(null);
    const t = useLocale(locale);

    const [fetching, setFetching] = useState(false);

    const handleSubmit = () => {
        const values = props.form.getFieldsValue();
        props.onSearch(values);
    };

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const [departData, setDepartData] = useState([]);
    useEffect(() => {
        setDepartData(translateToTreeStruct(allDepartInfo,'0'));
    },[])

    const colSpan = lang === 'zh-CN' ? 8 : 12;

    const [options, setOptions] = useState([]);

    const debouncedFetchUser = useCallback(
        debounce((inputValue) => {
            refFetchId.current = Date.now();
            const fetchId = refFetchId.current;
            setFetching(true);
            setOptions([]);
            requestTermList(inputValue).then((v) => {
                const data = v.data.list;
                if(refFetchId.current === fetchId){
                    const options = data.map((project) => ({
                        label: <div> {`${project.name}`}</div>,
                        value: project.id,
                    }))
                    setFetching(false);
                    setOptions(options);
                }
            })

        }, 500),
        []
    );

    return (
        <div className={styles['search-form-wrapper']}>
            <Form
                form={props.form}
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
                                treeData={departData}
                                onChange={(e,v) => {
                                    if(!e || e.length == '0'){
                                        props.form.resetFields();
                                        return;
                                    }
                                }}
                                onClear={(z) => {
                                    console.log("----z is:" + stringifyObj(z));
                                }}
                                style={{ width: '100%'}}
                            />
                        </Form.Item>
                    </Col>
                    <Col span={colSpan}>
                        <Form.Item label={"Project"} field="project">
                            <Select
                                showSearch
                                mode='multiple'
                                options={options}
                                placeholder='Search by name'
                                filterOption={false}
                                notFoundContent={
                                    fetching ? (
                                        <div
                                            style={{
                                                display: 'flex',
                                                alignItems: 'center',
                                                justifyContent: 'center',
                                            }}
                                        >
                                            <Spin style={{ margin: 12 }} />
                                        </div>
                                    ) : null
                                }
                                onSearch={debouncedFetchUser}
                            />
                        </Form.Item>
                    </Col>
                    <Col span={colSpan}>
                        <Form.Item
                            label={"CreatedTime"}
                            field="createdTime"
                        >
                            <DatePicker.RangePicker
                                allowClear
                                style={{ width: '100%' }}
                                disabledDate={(date) => dayjs(date).isAfter(dayjs())}
                            />
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
            <div className={styles['right-button']}>
                <Button size={"small"} type="primary" icon={<IconSearch />} onClick={handleSubmit}>
                    {"搜索"}
                </Button>
                <Button size={"small"} icon={<IconRefresh />} onClick={props.onClear}>
                    {"重置"}
                </Button>
            </div>
        </div>
    );
}

export default SearchForm;
