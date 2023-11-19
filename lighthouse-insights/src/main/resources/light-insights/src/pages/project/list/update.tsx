import React, {useEffect, useMemo, useRef, useState} from 'react';
import {Card, Form, Input, Link, Modal, Radio, Skeleton, Spin, TreeSelect} from "@arco-design/web-react";
import UserTermQuery from "@/pages/user/common/userTermQuery";
import {useSelector} from "react-redux";
import {Department, Project} from "@/types/insights-web";
import {translateToTreeStruct} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";
import {requestQueryById, requestUpdateById} from "@/api/project";
import useForm from "@arco-design/web-react/es/Form/useForm";
import {FormInstance} from "@arco-design/web-react/lib";
import {IconLoading} from "@arco-design/web-react/icon";

function ProjectUpdate({updateId,updateVisible,onHide}){

    const t = useLocale(locale);
    const [departData, setDepartData] = useState([]);
    const [form] = useForm();
    const [loading] = useState(true);
    const [loadingTermCompleted,setLoadingTermCompleted] = useState(false);
    const [loadingFormCompleted,setLoadingFormCompleted] = useState(false);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);
    const [isRed, setIsRed] = useState(false);

    const fetchProjectInfo = async () => {
        await requestQueryById({"id":updateId}).then((result) => {
            form.setFieldsValue(result.data);
            form.setFieldValue("departmentId",result.data.departmentId.toString());
            setLoadingFormCompleted(true);
        });
    }

    useEffect(() => {
        setDepartData(translateToTreeStruct(allDepartInfo,'0'));
    },[])

    useEffect(() => {
        setIsRed(false);
        setLoadingTermCompleted(false);
        setLoadingFormCompleted(false);
        fetchProjectInfo().then();
    },[updateId])

    useEffect(() => {
        if(loadingFormCompleted && loadingTermCompleted){
            setIsRed(true);
        }
    },[loadingTermCompleted,loadingFormCompleted])

    function handlerSubmit(){
        console.log("create submit!")
    }

    const loadingNode = (rows = 1) => {
        return (
            <Skeleton
                style={{ marginTop:15 }}
                text={{
                    rows,
                    width: ['100%'],
                }}
                animation
            />
        );
    };

    function loadingTermCompletedCallback(){
        setLoadingTermCompleted(true);
    }

    return (
        <Modal
            title='修改工程'
            visible={updateVisible}
            style={{ width:'650px'}}
            className='modal-demo-without-content-spacing'
            onOk={handlerSubmit}
            onCancel={onHide}>
            <Skeleton
                style={{ marginTop:15,display:isRed ? 'none' : 'block' }}
                text={{
                    rows:1,
                    width: ['100%'],
                }}
                animation
            />
            <Form
                style={{ display:isRed ? 'block' : 'none' }}
                form={form}
                autoComplete='off'
                scrollToFirstError
            >
                <Form.Item label='Name' field='id' rules={[{ required: true }]}>
                    <Input placeholder='Please enter...'/>
                </Form.Item>
                <Form.Item label={t['projectList.columns.department']}
                           field="departmentId" rules={[{ required: true }]}>
                    <TreeSelect
                        placeholder={"Please select"}
                        showSearch={true}
                        filterTreeNode={(inputText,node) => {
                            return node.props.title.toLowerCase().indexOf(inputText.toLowerCase()) > -1;
                        }}
                        allowClear={true}
                        treeData={departData}
                        style={{ width: '100%'}}
                    />
                </Form.Item>
                <Form.Item label={'Description'} field="desc" rules={[{ required: true }]}>
                    <Input.TextArea placeholder='Please enter ...' maxLength={200} showWordLimit={true} draggable={"false"} style={{ minHeight: 100}} />
                </Form.Item>
                <Form.Item label={'Is Private'} field="isPrivate" rules={[{ required: true }]}>
                    <Radio.Group defaultValue={0}>
                        <Radio value={0}>Private</Radio>
                        <Radio value={1}>Public</Radio>
                    </Radio.Group>
                </Form.Item>
                <Form.Item label={'Admins'} field="admins" rules={[{ required: true }]}>
                    <UserTermQuery initValues={[updateId]} completeCallBack={loadingTermCompletedCallback}/>
                </Form.Item>
            </Form>
        </Modal>
    );
}

export default ProjectUpdate;