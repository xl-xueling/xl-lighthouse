import React, {useCallback, useEffect, useRef, useState} from 'react';
import {Caller} from "@/types/caller";
import {Avatar, Form, Input, Message, Select, Spin} from "@arco-design/web-react";
const FormItem = Form.Item;
const Option = Select.Option;
const TextArea = Input.TextArea;
import debounce from 'lodash/debounce';
import {LabelValue} from "@/types/insights-common";
import {requestList} from "@/api/stat";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/caller/manage/locale";
export interface Props {
    callerInfo?:Caller,
}

const StatApply : React.FC<Props> = ({
                                            callerInfo= null,
    }) => {

    const t = useLocale(locale);
    const periodOptions:Array<LabelValue> = [{label:t['callerManage.authAdd.expired.3month'],value:7776000}
        ,{label:t['callerManage.authAdd.expired.6month'],value:15552000},{label:t['callerManage.authAdd.expired.1year'],value:31104000}
        ,{label:t['callerManage.authAdd.expired.2year'],value:62208000}]
    const [options, setOptions] = useState([]);
    const [fetching, setFetching] = useState(false);
    const refFetchId = useRef<any>(null);

    const debouncedFetchStat = useCallback(debounce(function (inputValue) {
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
                const options = data.list.map(function (stat) { return ({
                    label: <div> {stat.projectTitle + ' > ' + stat.token + ' > ' + stat.title + " ["+stat.id+"]"}</div>,
                    value: stat.id,
                }); });
                setFetching(false);
                setOptions(options);
            }
        });
    }, 500), []);

    return (
        <div style={{ width: '95%', margin: '0 auto' }}>
            <Form.Item field='stat' label={t['callerManage.authAdd.label.stat']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning']}]}>
                <Select
                    showSearch
                    options={options}
                    placeholder='Search title'
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
                    onSearch={debouncedFetchStat}
                />
            </Form.Item>

            <FormItem field={'expired'} label={t['callerManage.authAdd.label.expired']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                <Select
                    placeholder='Select period' defaultValue={periodOptions[0].value}>
                    {periodOptions.map((option, index) => (
                        <Option key={option.value} value={option.value}>
                            {option.label}
                        </Option>
                    ))}
                </Select>
            </FormItem>

            <FormItem field={'reason'} label={t['callerManage.authAdd.label.reason']} rules={[{ required: true ,message: t['basic.form.verification.empty.warning'] }]}>
                <TextArea placeholder='Enter something' style={{ minHeight: 64 }} />
            </FormItem>
        </div>
    );
};

export default StatApply;

