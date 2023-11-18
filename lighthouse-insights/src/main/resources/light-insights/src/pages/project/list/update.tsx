import React, {useEffect, useMemo, useState} from 'react';
import {Form, Input, Modal, Radio, TreeSelect} from "@arco-design/web-react";
import UserTermQuery from "@/pages/project/list/userTermQuery";
import {useSelector} from "react-redux";
import {Department} from "@/types/insights-web";
import {translateToTreeStruct} from "@/pages/department/common";
import useLocale from "@/utils/useLocale";
import locale from "@/pages/project/list/locale";

function ProjectUpdate({updateId,updateVisible,onHide}){

    const t = useLocale(locale);

    const [departData, setDepartData] = useState([]);

    const allDepartInfo = useSelector((state: {allDepartInfo:Array<Department>}) => state.allDepartInfo);

    useEffect(() => {
        setDepartData(translateToTreeStruct(allDepartInfo,'0'));
    },[])

    function handlerSubmit(){
        console.log("create submit!")
    }

    return (
        <Modal
            title='修改工程'
            visible={updateVisible}
            style={{ width:'650px' }}
            className='modal-demo-without-content-spacing'
            onOk={handlerSubmit}
            onCancel={onHide}
        >
                <Form
                    autoComplete='off'
                    initialValues={{
                        slider: 20,
                        'a.b[0].c': ['b'],
                    }}
                    scrollToFirstError
                >
                    <Form.Item label='Name' field='name' rules={[{ required: true }]}>
                        <Input placeholder='please enter...' />
                    </Form.Item>
                    <Form.Item label={t['projectList.columns.department']} field="department" rules={[{ required: true }]}>
                        <TreeSelect
                            placeholder={"Please select"}
                            multiple={true}
                            allowClear={true}
                            treeData={departData}
                            style={{ width: '100%'}}
                        />
                    </Form.Item>
                    <Form.Item label={'Description'} field="desc" rules={[{ required: true }]}>
                        <Input.TextArea placeholder='Please enter ...' style={{ minHeight: 64}} />
                    </Form.Item>
                    <Form.Item label={'IsPrivate'} field="isPrivate" rules={[{ required: true }]}>
                        <Radio.Group defaultValue='a' style={{ marginBottom: 20 }}>
                            <Radio value='0'>Private</Radio>
                            <Radio value='1'>Public</Radio>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item label={'Admins'} field="admins" rules={[{ required: true }]}>
                        <UserTermQuery/>
                    </Form.Item>
                </Form>
        </Modal>
    );
}

export default ProjectUpdate;