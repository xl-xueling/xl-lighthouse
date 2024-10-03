import React, {useEffect, useMemo, useState} from 'react';
import {
    Breadcrumb,
    Button,
    Card,
    Grid,
    Message,
    Notification,
    PaginationProps,
    Radio,
    Space,
} from '@arco-design/web-react';
import useLocale from '@/utils/useLocale';
import SearchForm from './form';
import locale from './locale';
import {getColumns} from './constants';
import {requestDeleteById, requestList} from "@/api/project";
import {Project, TreeNode} from "@/types/insights-web";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {useDispatch, useSelector} from "react-redux";
import {IconHome} from "@arco-design/web-react/icon";
import {GlobalState} from "@/store";
import ProjectListPanel from "@/pages/project/list/ProjectListPanel";
import ProjectCreatePanel from "@/pages/project/create";

const BreadcrumbItem = Breadcrumb.Item;

export default function ProjectList() {
    const t = useLocale(locale);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<TreeNode>}) => state.allDepartInfo);
    const [listData, setListData] = useState<Project[]>([]);
    const [selectedProject,setSelectedProject] = useState<Project>(null);
    const [form] = useForm();
    const [createVisible, setCreateVisible] = React.useState(false);
    const [updateVisible, setUpdateVisible] = React.useState(false);
    const [detailVisible, setDetailVisible] = React.useState(false);
    const [bindedVisible,setBindedVisible] = React.useState(false);
    const [applyVisible,setApplyVisible] = React.useState(false);
    const [reloadTime,setReloadTime] = useState<number>(Date.now);
    const userInfo = useSelector((state: GlobalState) => state.userInfo);
    const [owner,setOwner] = useState(1);
    const dispatch = useDispatch();
    const [formParams, setFormParams] = useState<any>({ownerId:userInfo.id});

    const hideCreateModal = () => {
        setCreateVisible(false);
    };

    const hideUpdateModal = () => {
        setUpdateVisible(false);
    };

    const [loading, setLoading] = useState(true);

    function handleSearch(params) {
        setFormParams({...params,t:Date.now()});
    }

    function handleReset(){
        form.resetFields();
        handleSearch({})
    }

    function handleChangeOwnerType(p){
        setOwner(p);
    }

    const handlerReloadList = () => {
        setFormParams({...formParams,t:Date.now()});
    }

    return (
            <Card>
                <SearchForm onSearch={handleSearch} onClear={handleReset} allDepartInfo={allDepartInfo} form={form}/>
                <Grid.Row justify="space-between" align="center" style={{marginBottom:'15px'}}>
                    <Grid.Col span={16} style={{ textAlign: 'left' }}>
                        <Space>
                            <Radio.Group defaultValue={"1"} name='button-radio-group' onChange={handleChangeOwnerType}>
                                {[{value:"1",label:t['projectList.operations.my.projects']},{value:"0",label:t['projectList.operations.all.projects']}].map((item) => {
                                    return (
                                        <Radio key={item.value} value={item.value}>
                                            {({ checked }) => {
                                                return (
                                                    <Button size={"small"} tabIndex={-1} key={item.value} shape='round' style={checked ? {color:'rgb(var(--primary-6)',fontWeight:500}:{fontWeight:500}}>
                                                        {item.label}
                                                    </Button>
                                                );
                                            }
                                            }
                                        </Radio>
                                    );
                                })}
                            </Radio.Group>
                        </Space>
                    </Grid.Col>
                    <Grid.Col span={8} style={{ textAlign: 'right' }}>
                        <Button size={"small"} type="primary" onClick={() => setCreateVisible(true)}>{t['projectList.operations.create.project']}</Button>
                    </Grid.Col>
                </Grid.Row>
                <ProjectListPanel formParams={formParams} owner={owner}/>
                {createVisible && <ProjectCreatePanel allDepartInfo={allDepartInfo} onClose={() => setCreateVisible(false)} onSuccess={handlerReloadList}/>}
            </Card>
    );

}