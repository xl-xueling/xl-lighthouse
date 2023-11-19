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
    const [itemInfo,setProjectInfo] = useState<Project>(null);
    const [form] = useForm();
    const [loading,setLoading] = useState(true);
    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    const fetchProjectInfo = async () => {
        await requestQueryById(updateId).then((result) => {
            setProjectInfo(result.data);
            form.setFieldsValue(result.data);
            setLoading(false);
        });
    }

    useEffect(() => {
        setLoading(true);
        setDepartData(translateToTreeStruct(allDepartInfo,'0'));
        fetchProjectInfo().then();
    },[updateId])

    function handlerSubmit(){
        console.log("create submit!")
    }

    const loadingNode = (rows = 1) => {
        return (
            <Skeleton
                style={{ marginTop:20 }}
                text={{
                    rows,
                    width: new Array(rows).fill('50%',50,50),
                }}
                animation
            />
        );

        // return (
        //     <Spin dot loading={true} size={3} style={{ width: '100%',textAlign:"center"}} >
        //     </Spin>
        // );
    };

    return (
        <Modal
            title='修改工程'
            visible={updateVisible}
            style={{ width:'650px'}}
            className='modal-demo-without-content-spacing'
            onOk={handlerSubmit}
            onCancel={onHide}
        >
            <Form
                form={form}
                autoComplete='off'
                scrollToFirstError
            >
                    {loading ? (
                        loadingNode()
                    ) : (
                        <Form.Item label='Name' field='name' rules={[{ required: true }]}>
                        <Input placeholder='please enter...' value={'sdgasdg'} />
                        </Form.Item>
                    )}



                    {loading ? (
                        loadingNode()
                    ) : (
                        <Form.Item label={t['projectList.columns.department']} field="department" rules={[{ required: true }]}>
                        <TreeSelect
                            placeholder={"Please select"}
                            multiple={true}
                            allowClear={true}
                            treeData={departData}
                            style={{ width: '100%'}}
                        />
                        </Form.Item>
                    )}

                    {loading ? (
                        loadingNode()
                    ) : (
                        <Form.Item label={'Description'} field="desc" rules={[{ required: true }]}>
                        <Input.TextArea placeholder='Please enter ...' style={{ minHeight: 64}} />
                        </Form.Item>
                    )}

                    {loading ? (
                        loadingNode()
                    ) : (
                        <Form.Item label={'IsPrivate'} field="isPrivate" rules={[{ required: true }]}>
                        <Radio.Group defaultValue='a' style={{ marginBottom: 20 }}>
                            <Radio value='0'>Private</Radio>
                            <Radio value='1'>Public</Radio>
                        </Radio.Group>
                        </Form.Item>
                    )}

                    {loading ? (
                        loadingNode()
                    ) : (
                        <Form.Item label={'Admins'} field="admins" rules={[{ required: true }]}>
                        <UserTermQuery/>
                        </Form.Item>
                    )}
            </Form>

        </Modal>
    );
}

export default ProjectUpdate;