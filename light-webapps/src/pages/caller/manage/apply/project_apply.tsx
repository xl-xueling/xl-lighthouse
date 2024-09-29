import React, {useCallback, useEffect, useRef, useState} from 'react';
import {Caller} from "@/types/caller";
import {Avatar, Form, Input, Message, Select, Spin} from "@arco-design/web-react";
const FormItem = Form.Item;
const Option = Select.Option;
const TextArea = Input.TextArea;

import debounce from 'lodash/debounce';
import {LabelValue} from "@/types/insights-common";
import {requestList} from "@/api/project";
export interface Props {
    callerInfo?:Caller,
}

const ProjectApply : React.FC<Props> = ({
                                            callerInfo= null,
    }) => {

    const periodOptions:Array<LabelValue> = [{label:'一个月',value:2592000},{label:'三个月',value:7776000}
            ,{label:'六个月',value:15552000},{label:'一年',value:31104000}]
    const [options, setOptions] = useState([]);
    const [fetching, setFetching] = useState(false);
    const refFetchId = useRef<any>(null);

    const debouncedFetchProject = useCallback(debounce(function (inputValue) {
        refFetchId.current = Date.now();
        const fetchId = refFetchId.current;
        setFetching(true);
        setOptions([]);
        const combineParams:any = {};
        combineParams.search = inputValue;
        requestList({
            queryParams: combineParams,
            pagination: {
                pageSize: 50,
                pageNum: 1,
            }
        }).then(function (response) {
            const code = response.code, data = response.data, message = response.message;
            if (refFetchId.current === fetchId) {
                const options = data.list.map(function (project) { return ({
                    label: <div> {project.title + " ["+project.id+"]"}</div>,
                    value: project.id,
                }); });
                setFetching(false);
                setOptions(options);
            }
        });
    }, 500), []);

    return (
        <div style={{ width: '95%', margin: '0 auto' }}>
                <Form.Item field='project' label={'统计工程'} rules={[{ required: true }]}>
                    <Select
                        showSearch
                        options={options}
                        placeholder='Search title'
                        filterOption={false}
                        renderFormat={(option) => {
                            return option.children.props.children[1];
                        }}
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
                        onSearch={debouncedFetchProject}
                    />
                </Form.Item>
                <FormItem field={'expired'} label={'有效期'} rules={[{ required: true }]}>
                    <Select
                        placeholder='Select period' defaultValue={periodOptions[0].value}>
                        {periodOptions.map((option, index) => (
                            <Option key={option.value} value={option.value}>
                                {option.label}
                            </Option>
                        ))}
                    </Select>
                </FormItem>

                <FormItem field={'reason'} label={'申请原因'} rules={[{ required: true }]}>
                    <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
                </FormItem>
        </div>
    );
};

export default ProjectApply;

