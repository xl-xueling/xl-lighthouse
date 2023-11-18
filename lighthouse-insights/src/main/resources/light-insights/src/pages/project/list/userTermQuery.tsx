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
    Rate,
    DatePicker, Message, Checkbox, Upload,
} from '@arco-design/web-react';
import PermissionWrapper from '@/components/PermissionWrapper';
import {IconCheck, IconClose, IconDownload, IconPlus, IconRefresh, IconSearch} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import styles from './style/index.module.less';
import '../mock';
import {getColumns} from './constants';
import {requestQueryList} from "@/api/project";
import {ResultData} from "@/types/insights-common";
import {Department, PrivilegeEnum, Project, ProjectPagination} from "@/types/insights-web";
import {requestPrivilegeCheck} from "@/api/privilege";
import {getDataWithLocalCache} from "@/utils/localCache";
import {fetchAllData as fetchAllDepartmentData, translateToTreeStruct} from "@/pages/department/common";
import InfoForm from "@/pages/user/setting/info";
import Security from "@/pages/user/setting/security";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {stringifyObj} from "@/utils/util";
import {useSelector} from "react-redux";
import {Slider} from "bizcharts";
import { useCallback } from 'react';
import { Spin, Avatar } from '@arco-design/web-react';
import debounce from 'lodash/debounce';


function UserTermQuery() {
    const [options, setOptions] = useState([]);
    const [fetching, setFetching] = useState(false);
    const refFetchId = useRef(null);
    const debouncedFetchUser = useCallback(
        debounce((inputValue) => {
            refFetchId.current = Date.now();
            const fetchId = refFetchId.current;
            setFetching(true);
            setOptions([]);
            fetch('https://randomuser.me/api/?results=5')
                .then((response) => response.json())
                .then((body) => {
                    if (refFetchId.current === fetchId) {
                        const options = body.results.map((user) => ({
                            label: (
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <Avatar size={24} style={{ marginLeft: 6, marginRight: 12 }}>
                                        <img alt='avatar' src={user.picture.thumbnail} />
                                    </Avatar>
                                    {`${user.name.first} ${user.name.last}`}
                                </div>
                            ),
                            value: user.email,
                        }));
                        setFetching(false);
                        setOptions(options);
                    }
                });
        }, 500),
        []
    );

    return (
        <Select
            style={{ width: 345 }}
            showSearch
            mode='multiple'
            options={options}
            placeholder='Search by name'
            filterOption={false}
            // renderFormat={(option) => {
            //     return option.children.props.children[1];
            // }}
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
    );
}

export default UserTermQuery;