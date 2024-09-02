import React, {useEffect, useImperativeHandle, useRef, useState} from 'react';
import {
    Form,
    Message,
    Select, TreeSelect,
} from '@arco-design/web-react';
import { useCallback } from 'react';
import { Spin } from '@arco-design/web-react';
import debounce from 'lodash/debounce';
import {requestList, requestTermList} from "@/api/project";
import useLocale from "@/utils/useLocale";

const ProjectTermQuery = React.forwardRef(( props:{},ref) => {

    const [selectedValues, setSelectedValues] = useState([]);
    const [options, setOptions] = useState([]);
    const [values, setValues] = useState([]);
    const [fetching, setFetching] = useState(false);
    const refFetchId = useRef(null);
    const selectRef = useRef();
    const debouncedFetchProject = useCallback(
        debounce((inputValue) => {
            refFetchId.current = Date.now();
            const fetchId = refFetchId.current;
            setFetching(true);
            setOptions([]);
            const combineParams:any = {}
            combineParams.search = inputValue;
            requestList({
                queryParams:combineParams,
                pagination:{
                    pageSize:50,
                    pageNum:1,
                }
            }).then((response) => {
                const {code, data ,message} = response;
                if(refFetchId.current === fetchId){
                    const options = data.list.map((project) => ({
                        label: <div> {`${project.title}`}</div>,
                        value: project.id,
                    }))
                    setFetching(false);
                    setOptions(options);
                }
            });
        }, 500),
        []
    );

    const handlerChange = (v) => {
        setSelectedValues(v);
    }

    useImperativeHandle(ref, () => ({
        getValue: () => {
            return selectedValues.length == 0 ? null : selectedValues;
        },
        reset: () => {
            setSelectedValues([]);
        }
    }));


    return (
        <Select
            ref={selectRef}
            showSearch
            mode='multiple'
            bordered={true}
            value={selectedValues}
            options={options}
            placeholder='Search Project'
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
            onSearch={debouncedFetchProject}
            onChange={handlerChange}
        />
    );
})

export default ProjectTermQuery;